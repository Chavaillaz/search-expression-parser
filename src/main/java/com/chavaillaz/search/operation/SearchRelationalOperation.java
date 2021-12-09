package com.chavaillaz.search.operation;

import com.chavaillaz.search.MultiLineToStringStyle;
import com.chavaillaz.search.SearchException;
import com.chavaillaz.search.converter.SearchDataConverterResult;
import com.chavaillaz.search.data.SearchDataProvider;
import com.chavaillaz.search.operator.SearchRelationalOperator;
import com.chavaillaz.search.parser.SearchExpressionField;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.chavaillaz.search.converter.SearchDataConverterFactory.findConverter;
import static java.util.Collections.singletonList;

/**
 * Represents a relational operation between a field and a value defined by a relational operator.
 */
public class SearchRelationalOperation implements SearchOperation {

    private SearchExpressionField field;
    private SearchRelationalOperator operator;
    private Object value;
    private boolean inverse;

    /**
     * Creates a new relational operation.
     */
    public SearchRelationalOperation() {
        this.inverse = false;
    }

    /**
     * Gets the field of the operation.
     *
     * @return The field
     */
    public SearchExpressionField getField() {
        return field;
    }

    /**
     * Sets the field of the operation.
     *
     * @param field The field
     */
    public void setField(SearchExpressionField field) {
        this.field = field;
    }

    /**
     * Gets the operator of the operation.
     *
     * @return The operator
     */
    public SearchRelationalOperator getOperator() {
        return operator;
    }

    /**
     * Gets the value of the operation.
     *
     * @return The value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the data corresponding to the operation.
     *
     * @param data The data
     */
    public void setData(SearchDataConverterResult data) {
        this.value = data.getValue();
        this.operator = data.getOperator();
        this.inverse = data.isInverse();
    }

    @Override
    public boolean match(SearchDataProvider dataProvider) {
        if (field != null) {
            return Stream.of(dataProvider.getFieldValue(field)).anyMatch(this::match);
        } else {
            if (inverse) {
                return dataProvider.getAllFieldValue().stream().allMatch(this::match);
            } else {
                return dataProvider.getAllFieldValue().stream().anyMatch(this::match);
            }
        }
    }

    private boolean match(Object dataProviderValue) {
        return inverse ^ Optional.ofNullable(field)
                .map(SearchExpressionField::getConverter)
                .or(() -> Optional.ofNullable(findConverter(field, String.class)))
                .orElseThrow(() -> new SearchException("Neither a converter for field nor for type of " + field + " was found."))
                .match(field, operator, value, dataProviderValue);
    }

    @Override
    public List<SearchExpressionField> getMatchingFields(SearchDataProvider dataProvider) {
        return match(dataProvider) ? getFields() : new ArrayList<>();
    }

    @Override
    public List<SearchExpressionField> getFields() {
        return singletonList(field);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new MultiLineToStringStyle());
    }

}
