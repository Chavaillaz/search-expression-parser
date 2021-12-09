package com.chavaillaz.search.data;

import java.util.List;

import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Data provider for the search engine.
 */
public interface SearchDataProvider {

    /**
     * Gets the value object corresponding to the given field.
     *
     * @param field The field for which retrieve the value
     * @return The value of the given field
     */
    Object getFieldValue(SearchExpressionField field);

    /**
     * Gets all values for all fields.
     *
     * @return The list of all values
     */
    List<?> getAllFieldValue();

}
