package com.chavaillaz.search.operation;

import com.chavaillaz.search.MultiLineToStringStyle;
import com.chavaillaz.search.data.SearchDataProvider;
import com.chavaillaz.search.operator.SearchLogicalOperator;
import com.chavaillaz.search.parser.SearchExpressionField;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chavaillaz.search.operator.SearchLogicalOperator.AND;

/**
 * Represents a logical operation between a list of operations defined by a logical operator.
 */
public class SearchLogicalOperation implements SearchOperation {

    private final List<SearchOperation> operations;
    private SearchLogicalOperator operator;

    /**
     * Creates a new logical operation.
     */
    public SearchLogicalOperation() {
        this.operations = new ArrayList<>();
    }

    /**
     * Indicates if the current operation has a logical operator.
     *
     * @return {@link Boolean#TRUE} if the operation has an operator, {@link Boolean#FALSE} otherwise
     */
    public boolean hasOperator() {
        return operator != null;
    }

    /**
     * Gets the logical operator of the current operation.
     *
     * @return The logical operation
     */
    public SearchLogicalOperator getOperator() {
        return hasOperator() ? operator : AND;
    }

    /**
     * Sets the logical operator of the current operation.
     *
     * @param operator The logical operator
     */
    public void setOperator(SearchLogicalOperator operator) {
        this.operator = operator;
    }

    /**
     * Gets the list of children operations.
     *
     * @return The list of children operations
     */
    public List<SearchOperation> getOperations() {
        return operations;
    }

    /**
     * Adds a child operation.
     *
     * @param operation The operation to add as child
     */
    public void addOperation(SearchOperation operation) {
        this.operations.add(operation);
    }

    @Override
    public boolean match(SearchDataProvider dataProvider) {
        return getOperator().compute(operations.stream()
                .map(operation -> operation.match(dataProvider))
                .toList());
    }

    @Override
    public List<SearchExpressionField> getMatchingFields(SearchDataProvider dataProvider) {
        return match(dataProvider) ? operations.stream()
                .filter(operation -> operation.match(dataProvider))
                .map(operation -> operation.getMatchingFields(dataProvider))
                .flatMap(Collection::stream)
                .toList() : new ArrayList<>();
    }

    @Override
    public List<SearchExpressionField> getFields() {
        return operations.stream()
                .map(SearchOperation::getFields)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new MultiLineToStringStyle());
    }

}
