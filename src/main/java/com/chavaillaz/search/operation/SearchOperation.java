package com.chavaillaz.search.operation;

import java.util.List;

import com.chavaillaz.search.data.SearchDataProvider;
import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Represents a search operation (element of the tree representing the search expression).
 */
public interface SearchOperation {

    /**
     * Indicates if the current operation and its children are matching with the given data from the provider.
     *
     * @param dataProvider The provider for the data to match with the operation
     * @return {@link Boolean#TRUE} if the data matches the operation, {@link Boolean#FALSE} otherwise
     */
    boolean match(SearchDataProvider dataProvider);

    /**
     * Gets all the fields that match with the current operation and its children.
     *
     * @param dataProvider The provider for the data to match with the operation
     * @return The list of fields that match the operation
     */
    List<SearchExpressionField> getMatchingFields(SearchDataProvider dataProvider);

    /**
     * Gets all fields in the current operation and its children.
     *
     * @return The list of all fields
     */
    List<SearchExpressionField> getFields();

}
