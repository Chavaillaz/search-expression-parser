package com.chavaillaz.search.operator;

import static com.chavaillaz.search.operator.SearchRelationalOperator.INTERVAL;
import static com.chavaillaz.search.operator.SearchRelationalOperator.LIKE;
import static com.chavaillaz.search.operator.SearchRelationalOperator.LIST;

import java.util.Date;

import org.apache.commons.lang3.ClassUtils;

import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Resolver for relational operators.
 * Used to define which relational operator use when parsing a search expression for a field.
 */
public class SearchRelationalOperatorResolver {

    public static final String INTERVAL_SEPARATOR = "-";
    public static final String LIST_SEPARATOR = ",";

    /**
     * Resolves the operator for the given searched field and value.
     *
     * @param field The field used in the search expression
     * @param value The value to search defined in the search expression
     * @return The found relational operator
     */
    public SearchRelationalOperator resolveOperator(SearchExpressionField field, String value) {
        // The field can be null if the user is searching over all of them
        if (field == null) {
            return value.contains(LIST_SEPARATOR) ? LIST : LIKE;
        }

        Class<?> fieldType = field.getType();

        if (fieldType.isPrimitive()) {
            fieldType = ClassUtils.primitiveToWrapper(fieldType);
        }

        SearchRelationalOperator operator = (field.getDefaultOperator() == null) ? LIKE : field.getDefaultOperator();
        if (value != null) {
            if (Number.class.isAssignableFrom(fieldType) || Date.class.isAssignableFrom(fieldType)) {
                if (value.contains(INTERVAL_SEPARATOR)) {
                    operator = INTERVAL;
                }
            }
            if (value.contains(LIST_SEPARATOR)) {
                operator = LIST;
            }
        }

        return operator;
    }

}
