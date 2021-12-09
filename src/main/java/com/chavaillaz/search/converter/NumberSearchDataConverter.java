package com.chavaillaz.search.converter;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Range;

import com.chavaillaz.search.SearchException;
import com.chavaillaz.search.operator.SearchRelationalOperator;
import com.chavaillaz.search.parser.SearchExpressionField;

/**
 * Converter for numbers.
 * <p>
 * Manages the conversion for:
 * <ul>
 * <li>Byte</li>
 * <li>Short</li>
 * <li>Integer</li>
 * <li>Long</li>
 * <li>Float</li>
 * <li>Double</li>
 * <li>BigDecimal</li>
 * <li>BigInteger</li>
 * </ul>
 * </p>
 */
public class NumberSearchDataConverter extends AbstractSearchDataConverter {

    @Override
    protected Object asSingleObject(SearchExpressionField field, String value) {
        if (value == null) {
            return null;
        }

        Class<?> targetType = field.getType();
        if (targetType.isPrimitive()) {
            targetType = ClassUtils.primitiveToWrapper(targetType);
        }

        try {
            Constructor constructor = targetType.getConstructor(String.class);
            return constructor.newInstance(value.trim());
        } catch (Exception e) {
            throw new SearchException("Error during conversion of value " + value + " to " + targetType);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Byte.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz) || short.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)
                || BigDecimal.class.isAssignableFrom(clazz)
                || BigInteger.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean match(SearchExpressionField field, SearchRelationalOperator operator, Object expected, Object current) {
        return switch (operator) {
            case INTERVAL -> matchInterval(expected, current);
            default -> super.match(field, operator, expected, current);
        };
    }

    private boolean matchInterval(Object expected, Object current) {
        if (expected instanceof List expectedValue) {
            Object startInterval = expectedValue.get(0);
            Object endInterval = expectedValue.get(1);

            if (startInterval instanceof Comparable comparableStart
                    && endInterval instanceof Comparable comparableEnd) {
                return Range.between(comparableStart, comparableEnd).contains(current);
            }
        }

        return false;
    }

}
