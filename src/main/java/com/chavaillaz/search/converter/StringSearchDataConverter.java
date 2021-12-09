package com.chavaillaz.search.converter;

import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Converter for strings.
 * <p>
 * Manages the conversion for:
 * <ul>
 * <li>String</li>
 * </ul>
 * </p>
 */
public class StringSearchDataConverter extends AbstractSearchDataConverter {

    @Override
    protected Object asSingleObject(SearchExpressionField field, String value) {
        return value;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    protected boolean matchEquals(SearchExpressionField field, Object expected, Object current) {
        String expectedRepresentation = fieldRepresentation(field, expected);
        String currentRepresentation = fieldRepresentation(field, current);
        return super.matchEquals(field, expectedRepresentation, currentRepresentation);
    }
}