package com.chavaillaz.search;

/**
 * General exception when something goes wrong during a search.
 */
public class SearchException extends RuntimeException {

    /**
     * Creates a new exception.
     *
     * @param description The description of the exception
     */
    public SearchException(String description) {
        super(description);
    }

    /**
     * Creates a new exception.
     *
     * @param description The description of the exception
     * @param exception   The root cause of this exception
     */
    public SearchException(String description, Throwable exception) {
        super(description, exception);
    }

    /**
     * Creates a new exception.
     *
     * @param exception The root cause of this exception
     */
    public SearchException(Throwable exception) {
        super(exception);
    }

}
