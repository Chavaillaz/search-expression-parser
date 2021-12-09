package com.chavaillaz.search.converter;

import com.chavaillaz.search.operator.SearchRelationalOperator;
import com.chavaillaz.search.operator.SearchRelationalOperatorResolver;
import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Converter used to transform values to their correct object instance.
 */
public interface SearchDataConverter {

    /**
     * Converts the given value to the final object.
     *
     * @param operatorResolver The operator resolver
     * @param field            The field for which we want to convert the value
     * @param value            The value to be converted
     * @return The converted value.
     */
    SearchDataConverterResult asObject(SearchRelationalOperatorResolver operatorResolver, SearchExpressionField field, String value);

    /**
     * Indicates if the given class is supported by this converter.
     *
     * @param clazz The class to check
     * @return {@link Boolean#TRUE} if the converter supports the given class, {@link Boolean#FALSE} otherwise
     */
    boolean supports(Class<?> clazz);

    /**
     * Indicates if the current value is matching the expected one with the given operator.
     *
     * @param field    The field of the current matching
     * @param operator The operator to use for the matching
     * @param expected The expected value given by the user
     * @param current  The current value given by the data provider
     * @return {@link Boolean#TRUE} if the current value match the expected one, {@link Boolean#FALSE} otherwise
     */
    boolean match(SearchExpressionField field, SearchRelationalOperator operator, Object expected, Object current);

}
