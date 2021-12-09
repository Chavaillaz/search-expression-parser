package com.chavaillaz.search.converter;

import com.chavaillaz.search.operator.SearchRelationalOperator;

/**
 * Represents the result of a conversion of a value by a {@link SearchDataConverter}.
 */
public class SearchDataConverterResult {

    private final SearchRelationalOperator operator;
    private final Object value;
    private final boolean inverse;

    /**
     * Creates a new result with the given operator.
     *
     * @param operator The relational operator
     */
    public SearchDataConverterResult(SearchRelationalOperator operator) {
        this(operator, null);
    }

    /**
     * Creates a new result with the given operator and value.
     *
     * @param operator The relational operator
     * @param value    The value
     */
    public SearchDataConverterResult(SearchRelationalOperator operator, Object value) {
        this(operator, value, false);
    }

    /**
     * Creates a new result with the given operator, value and inversion indicator.
     *
     * @param operator The relational operator
     * @param value    The value
     * @param inverse  {@link Boolean#TRUE} if the result must be inverted, {@link Boolean#FALSE} otherwise
     */
    public SearchDataConverterResult(SearchRelationalOperator operator, Object value, boolean inverse) {
        this.operator = operator;
        this.value = value;
        this.inverse = inverse;
    }

    /**
     * Gets the relational operator of the conversion result.
     *
     * @return The relational operator
     */
    public SearchRelationalOperator getOperator() {
        return operator;
    }

    /**
     * Gets the value of the conversion result.
     *
     * @return The value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the inversion indicator of the conversion result.
     *
     * @return {@link Boolean#TRUE} if the result must be inverted, {@link Boolean#FALSE} otherwise
     */
    public boolean isInverse() {
        return inverse;
    }

}
