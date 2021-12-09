package com.chavaillaz.search.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Data provider for a {@link Map}.
 *
 * @param <T> The type of the value matched in the {@link Map}
 */
public class SearchMapDataProvider<T> implements SearchDataProvider {

    protected Map<String, T> data;

    /**
     * Creates a new data provider for the given {@link Map}.
     *
     * @param data The {@link Map} for which search specific expression
     */
    public SearchMapDataProvider(Map<String, T> data) {
        this.data = data;
    }

    @Override
    public T getFieldValue(SearchExpressionField field) {
        return data.get(field.getName());
    }

    @Override
    public List<T> getAllFieldValue() {
        return new ArrayList<>(data.values());
    }

}
