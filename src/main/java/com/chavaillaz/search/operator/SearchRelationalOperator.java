package com.chavaillaz.search.operator;

/**
 * Enumeration of relational operators used to define which value search in fields.
 */
public enum SearchRelationalOperator {

    EQUALS(false),
    LIKE(false),
    INTERVAL(true),
    LIST(true);

    private final boolean collection;

    /**
     * Creates a new relational operator.
     *
     * @param isCollection Indicates if this operator uses a collection of value during a search
     */
    SearchRelationalOperator(boolean isCollection) {
        this.collection = isCollection;
    }

    /**
     * Indicates if the operator receives multiple value when searching with it.
     *
     * @return {@link Boolean#TRUE} if the operator uses collection, {@link Boolean#FALSE} otherwise
     */
    public boolean isCollection() {
        return collection;
    }

}
