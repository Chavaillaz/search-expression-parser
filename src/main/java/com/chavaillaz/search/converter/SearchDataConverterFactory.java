package com.chavaillaz.search.converter;

import com.chavaillaz.search.parser.SearchExpressionField;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory used to find the right converter for a specific type.
 * <p>
 * <p>By default, two converters are registered:</p>
 * <ul>
 * <li>{@link StringSearchDataConverter}</li>
 * <li>{@link NumberSearchDataConverter}</li>
 * </ul>
 */
@UtilityClass
public class SearchDataConverterFactory {

    private static final List<SearchDataConverter> converters;

    static {
        // Default converters
        converters = new ArrayList<>();
        registerConverter(new StringSearchDataConverter());
        registerConverter(new NumberSearchDataConverter());
    }

    /**
     * Registers a new converter.
     *
     * @param converter The converter
     */
    public static void registerConverter(SearchDataConverter converter) {
        converters.add(0, converter);
    }

    /**
     * Deregister a converter.
     *
     * @param converter The converter
     */
    public static void unregisterConverter(SearchDataConverter converter) {
        converters.remove(converter);
    }

    /**
     * Finds the correct converter for the given class.
     *
     * @param clazz The class for which search a converter
     * @return The converter found or {@code null} otherwise
     */
    public static SearchDataConverter findConverter(Class<?> clazz) {
        return converters.stream()
                .filter(converter -> converter.supports(clazz))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds the correct converter for the given field.
     * Uses the given default type if the type cannot be defined with the field.
     *
     * @param field       The field for which search the converter.
     * @param defaultType The default type to use when necessary.
     * @return The found converter or the converter the given default type when necessary.
     */
    public static SearchDataConverter findConverter(SearchExpressionField field, Class<?> defaultType) {
        Class<?> fieldType = field != null ? field.getType() : defaultType;
        return defaultType != null ? findConverter(fieldType) : null;
    }

}
