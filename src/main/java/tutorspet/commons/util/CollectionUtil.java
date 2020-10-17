package tutorspet.commons.util;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Utility methods related to Collections
 */
public class CollectionUtil {

    /** @see #requireAllNonNull(Collection) */
    public static void requireAllNonNull(Object... items) {
        requireNonNull(items);

        Stream.of(items).forEach(Objects::requireNonNull);
    }

    /**
     * Throws NullPointerException if {@code items} or any element of {@code items} is null.
     */
    public static void requireAllNonNull(Collection<?> items) {
        requireNonNull(items);

        items.forEach(Objects::requireNonNull);
    }

    /**
     * Returns true if {@code items} contain any elements that are non-null.
     */
    public static boolean isAnyNonNull(Object... items) {
        return items != null && Arrays.stream(items).anyMatch(Objects::nonNull);
    }

    /**
     * Deep copies a {@code List}, along with deep copies of its elements.
     *
     * @param list the list to be copied.
     * @param deepCopyElement the function that deep copies the elements of the list.
     * @param <T> type of the element of the list.
     * @return the deep copied list.
     */
    public static <T> List<T> deepCopyList(List<T> list, UnaryOperator<T> deepCopyElement) {
        return list.stream()
                .map(element -> deepCopyElement.apply(element))
                .collect(toCollection(ArrayList::new));
    }

    /**
     * Deep copies a {@code Map}, along with deep copies of its keys and values.
     *
     * @param map the map to be copied.
     * @param deepCopyKey the function that deep copies the key.
     * @param deepCopyValue the function that deep copies the value.
     * @param <T> type of the key.
     * @param <U> type of the value.
     * @return the deep copied map.
     */
    public static <T, U> Map<T, U> deepCopyMap(
            Map<T, U> map, UnaryOperator<T> deepCopyKey, UnaryOperator<U> deepCopyValue) {
        HashMap<T, U> copyMap = new HashMap<>();
        for (T key : map.keySet()) {
            T copiedKey = deepCopyKey.apply(key);
            U copiedValue = deepCopyValue.apply(map.get(key));
            copyMap.put(copiedKey, copiedValue);
        }
        return copyMap;
    }
}
