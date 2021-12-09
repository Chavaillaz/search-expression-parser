package com.chavaillaz.search.parser;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.chavaillaz.search.MultiLineToStringStyle;
import com.chavaillaz.search.converter.SearchDataConverter;
import com.chavaillaz.search.operator.SearchRelationalOperator;

/**
 * Represents a field that can be used in the search expression.
 */
public class SearchExpressionField {

    private String name;
    private Class<?> type;
    private SearchDataConverter converter;
    private SearchRelationalOperator defaultOperator;
    private boolean caseSensitive;

    /**
     * Creates a new field.
     */
    public SearchExpressionField() {
        caseSensitive = false;
    }

    /**
     * Gets the name of the field.
     *
     * @return The field name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the field.
     *
     * @param name The field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the field.
     *
     * @return The field type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets the type of the field.
     *
     * @param type The field type
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Gets the converter of the field if existing.
     *
     * @return The converter if it exists, {@code null} otherwise
     */
    public SearchDataConverter getConverter() {
        return converter;
    }

    /**
     * Sets the converter of the field.
     *
     * @param converter The field converter
     */
    public void setConverter(SearchDataConverter converter) {
        this.converter = converter;
    }

    /**
     * Gets the default relational operator of the field.
     *
     * @return The default relational operator
     */
    public SearchRelationalOperator getDefaultOperator() {
        return defaultOperator;
    }

    /**
     * Sets the default relational operator of the field.
     *
     * @param operator The default relational operator
     */
    public void setDefaultOperator(SearchRelationalOperator operator) {
        this.defaultOperator = operator;
    }

    /**
     * Indicates if the field is case sensitive or not.
     *
     * @return {@link Boolean#TRUE} if the field is case sensitive, {@link Boolean#FALSE} otherwise
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Sets the case sentitive flag of the field.
     *
     * @param caseSensitive {@link Boolean#TRUE} if the field is case sensitive, {@link Boolean#FALSE} otherwise
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new MultiLineToStringStyle());
    }

}
