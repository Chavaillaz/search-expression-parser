package com.chavaillaz.search.operator;

import java.util.List;

import static org.apache.commons.lang3.EnumUtils.getEnum;

/**
 * Enumeration of logical operators used in a search expression.
 */
public enum SearchLogicalOperator {

    AND(2) {
        @Override
        public boolean compute(List<Boolean> values) {
            return values.stream().allMatch(value -> value);
        }
    },
    OR(1) {
        @Override
        public boolean compute(List<Boolean> values) {
            return values.stream().anyMatch(value -> value);
        }
    };

    private final int priority;

    /**
     * Creates a new logical operator.
     * The priority indicates the operation priority.
     * The parser will create the corresponding tree for the given priority.
     *
     * @param priority The computing priority of this operator
     */
    SearchLogicalOperator(int priority) {
        this.priority = priority;
    }

    /**
     * Indicates if the current logical operator has a higher priority compared to the given one.
     *
     * @param operator The logical operator for which compare the priority
     * @return {@link Boolean#TRUE} if the current logical operator has a higher priority, {@link Boolean#FALSE} otherwise
     */
    public boolean hasHigherPriority(SearchLogicalOperator operator) {
        return this.priority > operator.priority;
    }

    /**
     * Searches the logical operator with the given name.
     * This method will not throw exception when the operator is not found.
     *
     * @param name The name of the logical operator to search
     * @return The optional logical operator if it exists, {@code null} otherwise
     */
    public static SearchLogicalOperator search(String name) {
        return getEnum(SearchLogicalOperator.class, name.trim().toUpperCase());
    }

    /**
     * Computes the operator for the given boolean list.
     *
     * @param values The list of boolean values for which apply the logical operator
     * @return The result of the operation
     */
    public abstract boolean compute(List<Boolean> values);

}
