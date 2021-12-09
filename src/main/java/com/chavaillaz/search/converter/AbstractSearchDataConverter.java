package com.chavaillaz.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chavaillaz.search.SearchException;
import com.chavaillaz.search.operator.SearchRelationalOperator;
import com.chavaillaz.search.operator.SearchRelationalOperatorResolver;
import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Abstract converter containing common method to manage conversion.
 * List and intervals are automatically managed. The method to override is {#asSingleObject}.
 */
public abstract class AbstractSearchDataConverter implements SearchDataConverter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSearchDataConverter.class);

    @Override
    public SearchDataConverterResult asObject(SearchRelationalOperatorResolver operatorResolver, SearchExpressionField field, String value) {
        boolean inversion = value != null && value.startsWith("!");
        if (inversion) {
            logger.debug("Operation inversion detected");
            value = value.substring(1);
        }

        SearchRelationalOperator operator = operatorResolver.resolveOperator(field, value);
        if (value == null) {
            return new SearchDataConverterResult(operator, null);
        }

        Object objectValue = switch (operator) {
            case LIKE, EQUALS -> asSingleObject(field, value);
            case LIST -> processListValue(field, value);
            case INTERVAL -> processIntervalValue(field, value);
            default -> throw new SearchException("Operator " + operator + " not supported");
        };

        return new SearchDataConverterResult(operator, objectValue, inversion);
    }

    private Object processIntervalValue(SearchExpressionField field, String value) {
        final String[] striped = value.split(SearchRelationalOperatorResolver.INTERVAL_SEPARATOR);
        final List<Object> interval = new ArrayList<>();
        if (striped.length == 0) {
            throw new SearchException("No interval expression found " + value);
        }
        if (striped.length == 1) {
            Object converted = asSingleObject(field, striped[0]);
            interval.add(converted);
            interval.add(converted);
        } else if (striped.length == 2) {
            interval.add(asSingleObject(field, striped[0]));
            interval.add(asSingleObject(field, striped[1]));
        }
        return interval;
    }

    private Object processListValue(SearchExpressionField field, String value) {
        final String[] striped = value.split(SearchRelationalOperatorResolver.LIST_SEPARATOR);
        final List<Object> list = new ArrayList<>();
        if (striped.length == 0) {
            throw new SearchException("No list expression found " + value);
        }
        for (String singleValue : striped) {
            list.add(asSingleObject(field, singleValue));
        }
        return list;
    }

    /**
     * Converts the given value to the final object.
     *
     * @param field The field containing the given value
     * @param value The value as {@link String}
     * @return The correct object representing the value
     */
    protected abstract Object asSingleObject(SearchExpressionField field, String value);

    /**
     * Indicates if the current value is matching the expected one with the given operator.
     * By default the matching processes for the following operator are:
     * <ul>
     * <li><b>EQUALS :</b> See {@link #matchEquals(SearchExpressionField, Object, Object)}</li>
     * <li><b>LIKE :</b> See {@link #matchLike(SearchExpressionField, Object, Object)}</li>
     * <li><b>LIST:</b> See {@link #matchList(SearchExpressionField, Object, Object)}</li>
     * </ul>
     *
     * @param field    The field of the current matching
     * @param operator The operator to use for the matching
     * @param expected The expected value given by the user
     * @param current  The current value given by the data provider
     * @return {@link Boolean#TRUE} if the current value match the expected one, {@link Boolean#FALSE} otherwise
     */
    @Override
    public boolean match(SearchExpressionField field, SearchRelationalOperator operator, Object expected, Object current) {
        if (expected == null || current == null) {
            return false;
        }

        return switch (operator) {
            case EQUALS -> matchEquals(field, expected, current);
            case LIKE -> matchLike(field, expected, current);
            case LIST -> matchList(field, expected, current);
            default -> false;
        };
    }

    /**
     * Compare objects with {@link Object#equals(Object)}.
     *
     * @param field    The field to match
     * @param expected The expected value
     * @param current  The current value
     * @return {@link Boolean#TRUE} if the current value match the expected one, {@link Boolean#FALSE} otherwise
     */
    protected boolean matchEquals(SearchExpressionField field, Object expected, Object current) {
        return expected.equals(current);
    }

    /**
     * Compare values with {@link Object#toString()}
     *
     * @param field    The field to match
     * @param expected The expected value
     * @param current  The current value
     * @return {@link Boolean#TRUE} if the current value match the expected one, {@link Boolean#FALSE} otherwise
     */
    protected boolean matchLike(SearchExpressionField field, Object expected, Object current) {
        return fieldRepresentation(field, current).contains(fieldRepresentation(field, expected));
    }

    /**
     * Search value inside the list by comparing values with {@link Object#toString()}
     *
     * @param field    The field to match
     * @param expected The expected value
     * @param current  The current value
     * @return {@link Boolean#TRUE} if the current value match the expected one, {@link Boolean#FALSE} otherwise
     */
    protected boolean matchList(SearchExpressionField field, Object expected, Object current) {
        if (expected instanceof List<?> expectedValue) {
            String currentValue = current.toString();
            return expectedValue.stream()
                    .map(Object::toString)
                    .anyMatch(currentValue::contains);
        }
        return false;
    }

    /**
     * Manages if the field has to be normalized because it's case-insensitive.
     *
     * @param field The field concerned
     * @param value The value to normalize
     * @return The normalized value
     */
    protected String fieldRepresentation(SearchExpressionField field, Object value) {
        if (value != null) {
            if (field != null && field.isCaseSensitive()) {
                return value.toString();
            } else {
                return value.toString().toLowerCase();
            }
        }
        return null;
    }

}
