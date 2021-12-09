package com.chavaillaz.search.parser;

import com.chavaillaz.search.SearchException;
import com.chavaillaz.search.converter.SearchDataConverter;
import com.chavaillaz.search.converter.SearchDataConverterResult;
import com.chavaillaz.search.data.SearchDataProvider;
import com.chavaillaz.search.operation.SearchLogicalOperation;
import com.chavaillaz.search.operation.SearchOperation;
import com.chavaillaz.search.operation.SearchRelationalOperation;
import com.chavaillaz.search.operator.SearchLogicalOperator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static com.chavaillaz.search.converter.SearchDataConverterFactory.findConverter;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

/**
 * Parser of simple search expressions.
 */
@Slf4j
public class SearchParser {

    protected final SearchExpressionPlan plan;
    protected Deque<SearchLogicalOperation> stack = new ArrayDeque<>();
    protected SearchLogicalOperation currentLogical = new SearchLogicalOperation();
    protected SearchLogicalOperation headOperation = currentLogical;
    protected SearchRelationalOperation currentRelational = new SearchRelationalOperation();
    protected StringBuilder lastWord = new StringBuilder();
    protected Mode mode = Mode.FIELD;
    protected Character quote = null;

    /**
     * Creates a new parser with the given search plan.
     *
     * @param plan The search plan
     */
    public SearchParser(SearchExpressionPlan plan) {
        this.plan = plan;
    }

    /**
     * Gets the list of matching fields for the current expression.
     *
     * @param dataProvider The data provider
     * @return The list of all matching fields
     */
    public List<SearchExpressionField> getMatchingField(SearchDataProvider dataProvider) {
        return headOperation.getMatchingFields(dataProvider);
    }

    /**
     * Indicates if the current expression is matching with the given data from the provider.
     *
     * @param dataProvider The data provider
     * @return @link Boolean#TRUE} if the data matches the operation, {@link Boolean#FALSE} otherwise
     */
    public boolean match(SearchDataProvider dataProvider) {
        return this.headOperation.match(dataProvider);
    }

    /**
     * Parses the given expression.
     *
     * @param searchExpression The search expression
     * @return The final search operation created from the expression
     */
    public synchronized SearchOperation parse(String searchExpression) {
        log.info("Parsing expression '{}'", searchExpression);
        final char[] expression = searchExpression.toCharArray();

        for (char character : expression) {
            log.trace("Processing character '{}'", character);
            if (character == '(' && !quoteMode()) {
                checkOperator();
                saveRelationalOperation();
                saveCurrentOperation();
                clearLastWord();
                newRelationalOperation();
            } else if (character == ')' && !quoteMode()) {
                saveRelationalOperation();
                restoreParentOperation();
                clearLastWord();
                newRelationalOperation();
            } else if (character == ':' && !quoteMode()) {
                saveFieldType();
            } else if ((character == '"' || character == '\'') && (isEqual(quote, character) || !quoteMode())) {
                manageQuote(character);
            } else if (character == ' ' && !quoteMode()) {
                checkOperator();
                saveFieldValue();
                newRelationalOperation();
                clearLastWord();
            } else {
                lastWord.append(character);
            }
        }

        saveFieldValue();

        log.debug("Final tree {}", headOperation);
        return headOperation;
    }

    protected SearchDataConverterResult convert(SearchExpressionField field, String value) {
        log.trace("Converting the received value for the field {}", field != null ? field.getName() : "[all]");
        SearchDataConverter converter = Optional.ofNullable(field)
                .map(SearchExpressionField::getConverter)
                .or(() -> Optional.ofNullable(findConverter(field, String.class)))
                .orElseThrow(() -> new SearchException("Neither a converter for field nor for type of " + field + " was found."));

        return converter.asObject(plan.getOperatorResolver(), field, value);
    }

    protected void clearLastWord() {
        log.trace("Clear last word");
        lastWord.setLength(0);
    }

    protected void saveFieldValue() {
        String word = lastWord.toString();
        if (!word.isEmpty()) {
            log.trace("Set value {} for the current relational operation", word);
            currentRelational.setData(convert(currentRelational.getField(), word));
            currentLogical.addOperation(currentRelational);
        }
        mode = Mode.FIELD;
    }

    protected void saveFieldType() {
        String word = lastWord.toString();
        log.trace("Save the field {} in the current relational operation", word);
        SearchExpressionField field = plan.getField(word);
        currentRelational.setField(field);
        mode = Mode.VALUE;
        clearLastWord();
    }

    protected void checkOperator() {
        SearchLogicalOperator operator = SearchLogicalOperator.search(lastWord.toString());
        if (operator != null) {
            clearLastWord();
            log.trace("Found the operator {}", operator);
            if (currentLogical.hasOperator() && currentLogical.getOperator() != operator) {
                log.trace("Change of operator detected (current operation is {} and new one is {})", currentLogical.getOperator(), operator);
                changeOperator(operator);
            } else {
                log.trace("Set the operator {} for the current logical operation", operator);
                currentLogical.setOperator(operator);
                mode = Mode.FIELD;
            }
        }
    }

    protected void changeOperator(SearchLogicalOperator operator) {
        SearchLogicalOperation newOperation = new SearchLogicalOperation();
        newOperation.setOperator(operator);

        if (operator.hasHigherPriority(currentLogical.getOperator())) {
            log.trace("Balance the operation tree because of a higher priority operator (operator {} > current {})", operator, currentLogical.getOperator());
            newOperation.addOperation(popLastSearchOperation());
            currentLogical.addOperation(newOperation);
            headOperation = currentLogical;
            currentLogical = newOperation;
        } else {
            log.trace("Adding new operation as child of the current logical operation");
            newOperation.addOperation(currentLogical);
            currentLogical = newOperation;
            headOperation = currentLogical;
        }
    }

    protected SearchOperation popLastSearchOperation() {
        int lastOperationIndex = currentLogical.getOperations().size() - 1;
        SearchOperation lastOperation = currentLogical.getOperations().get(lastOperationIndex);
        currentLogical.getOperations().remove(lastOperationIndex);
        return lastOperation;
    }

    protected void manageQuote(char character) {
        if (quote == null) {
            log.trace("Activating quote mode for character {}", character);
            quote = character;
        } else {
            log.trace("Disabling quote mode for character {}", character);
            quote = null;
        }
    }

    protected void restoreParentOperation() {
        log.trace("Following a closing bracket restore the parent operation");
        SearchLogicalOperation parentOperation = stack.pop();
        parentOperation.addOperation(currentLogical);
        currentLogical = parentOperation;
    }

    protected void saveCurrentOperation() {
        log.trace("Following an opening bracket save the current operation");
        stack.push(currentLogical);
        currentLogical = new SearchLogicalOperation();
    }

    protected void saveRelationalOperation() {
        if (!lastWord.toString().isEmpty()) {
            log.trace("Save the current relational operation inside the current logical operation");
            saveFieldValue();
            newRelationalOperation();
            clearLastWord();
        }
    }

    protected void newRelationalOperation() {
        log.trace("New relational operation");
        currentRelational = new SearchRelationalOperation();
    }

    protected boolean quoteMode() {
        return quote != null;
    }

    protected boolean isEqual(Character quote, Character character) {
        return allNotNull(quote, character) && quote.equals(character);
    }

    enum Mode {
        FIELD,
        VALUE
    }

}
