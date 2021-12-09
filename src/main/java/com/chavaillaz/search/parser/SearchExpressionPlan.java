package com.chavaillaz.search.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.chavaillaz.search.MultiLineToStringStyle;
import com.chavaillaz.search.operator.SearchRelationalOperatorResolver;

/**
 * The search expression plan containing the fields allowed in the search and the relational operator resolver.
 */
public class SearchExpressionPlan {

    private final SearchRelationalOperatorResolver operatorResolver;
    private final Map<String, SearchExpressionField> fields;

    /**
     * Creates a plan with the given relational operator resolver.
     *
     * @param operatorResolver The relational operator resolver
     */
    public SearchExpressionPlan(SearchRelationalOperatorResolver operatorResolver) {
        this.operatorResolver = operatorResolver;
        this.fields = new HashMap<>();
    }

    /**
     * Gets the relational operator resolver.
     *
     * @return The relational operator resolver
     */
    public SearchRelationalOperatorResolver getOperatorResolver() {
        return operatorResolver;
    }

    /**
     * Gets all the fields defined in the current plan.
     *
     * @return The list of all fields
     */
    public Collection<SearchExpressionField> getFields() {
        return fields.values();
    }

    /**
     * Adds a new field to the plan.
     *
     * @param field The field to add
     */
    public void addField(SearchExpressionField field) {
        this.fields.put(field.getName(), field);
    }

    /**
     * Gets the field corresponding to the given name.
     *
     * @param name The name of the desired field
     * @return The field found with the given name
     */
    public SearchExpressionField getField(String name) {
        return this.fields.get(name);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new MultiLineToStringStyle());
    }

}
