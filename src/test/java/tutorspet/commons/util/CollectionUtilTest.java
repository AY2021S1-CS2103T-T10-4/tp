package tutorspet.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutorspet.commons.util.CollectionUtil.deepCopyList;
import static tutorspet.commons.util.CollectionUtil.deepCopyMap;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

public class CollectionUtilTest {

    @Test
    public void requireAllNonNullVarargs() {
        // no arguments
        assertNullPointerExceptionNotThrown();

        // any non-empty argument list
        assertNullPointerExceptionNotThrown(new Object(), new Object());
        assertNullPointerExceptionNotThrown("test");
        assertNullPointerExceptionNotThrown("");

        // argument lists with just one null at the beginning
        assertNullPointerExceptionThrown((Object) null);
        assertNullPointerExceptionThrown(null, "", new Object());
        assertNullPointerExceptionThrown(null, new Object(), new Object());

        // argument lists with nulls in the middle
        assertNullPointerExceptionThrown(new Object(), null, null, "test");
        assertNullPointerExceptionThrown("", null, new Object());

        // argument lists with one null as the last argument
        assertNullPointerExceptionThrown("", new Object(), null);
        assertNullPointerExceptionThrown(new Object(), new Object(), null);

        // null reference
        assertNullPointerExceptionThrown((Object[]) null);

        // confirms nulls inside lists in the argument list are not considered
        List<Object> containingNull = Arrays.asList((Object) null);
        assertNullPointerExceptionNotThrown(containingNull, new Object());
    }

    @Test
    public void requireAllNonNullCollection() {
        // lists containing nulls in the front
        assertNullPointerExceptionThrown(Arrays.asList((Object) null));
        assertNullPointerExceptionThrown(Arrays.asList(null, new Object(), ""));

        // lists containing nulls in the middle
        assertNullPointerExceptionThrown(Arrays.asList("spam", null, new Object()));
        assertNullPointerExceptionThrown(Arrays.asList("spam", null, "eggs", null, new Object()));

        // lists containing nulls at the end
        assertNullPointerExceptionThrown(Arrays.asList("spam", new Object(), null));
        assertNullPointerExceptionThrown(Arrays.asList(new Object(), null));

        // null reference
        assertNullPointerExceptionThrown((Collection<Object>) null);

        // empty list
        assertNullPointerExceptionNotThrown(Collections.emptyList());

        // list with all non-null elements
        assertNullPointerExceptionNotThrown(Arrays.asList(new Object(), "ham", Integer.valueOf(1)));
        assertNullPointerExceptionNotThrown(Arrays.asList(new Object()));

        // confirms nulls inside nested lists are not considered
        List<Object> containingNull = Arrays.asList((Object) null);
        assertNullPointerExceptionNotThrown(Arrays.asList(containingNull, new Object()));
    }

    @Test
    public void isAnyNonNull() {
        assertFalse(CollectionUtil.isAnyNonNull());
        assertFalse(CollectionUtil.isAnyNonNull((Object) null));
        assertFalse(CollectionUtil.isAnyNonNull((Object[]) null));
        assertTrue(CollectionUtil.isAnyNonNull(new Object()));
        assertTrue(CollectionUtil.isAnyNonNull(new Object(), null));
    }

    @Test
    public void testDeepCopyList_primitive() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> copiedList = deepCopyList(list, n -> n);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), copiedList.get(i));
        }
    }

    @Test
    public void testDeepCopyList_class() {
        List<MockTestClass> list = new ArrayList<>();
        list.add(new MockTestClass(1, "1"));
        list.add(new MockTestClass(2, "2"));
        list.add(new MockTestClass(3, "3"));

        List<MockTestClass> copiedList = deepCopyList(list, elem -> elem.deepCopy());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), copiedList.get(i));
            assertFalse(list.get(i) == copiedList.get(i));
        }
    }

    @Test
    public void testDeepCopyMap_primitive() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");

        Map<Integer, String> copiedMap = deepCopyMap(map, key -> key, value -> value);
        for (Integer key: map.keySet()) {
            assertTrue(copiedMap.containsKey(key));
            assertEquals(map.get(key), copiedMap.get(key));
        }
        for (Integer key: copiedMap.keySet()) {
            assertTrue(map.containsKey(key));
        }
    }

    @Test
    public void testDeepCopyMap_class() {
        Map<MockTestClass, MockTestClass> map = new HashMap<>();
        map.put(new MockTestClass(1, "key1"), new MockTestClass(1, "value1"));
        map.put(new MockTestClass(2, "key2"), new MockTestClass(2, "value2"));
        map.put(new MockTestClass(3, "key3"), new MockTestClass(3, "value3"));

        Map<MockTestClass, MockTestClass> copiedMap = deepCopyMap(
                map, key -> key.deepCopy(), value -> value.deepCopy());
        for (MockTestClass key: map.keySet()) {
            assertEquals(map.get(key), copiedMap.get(key));
            assertFalse(map.get(key) == copiedMap.get(key));
        }

        MockTestClass[] mapKeyArray = map.keySet().toArray(new MockTestClass[3]);
        MockTestClass[] copiedMapKeyArray = copiedMap.keySet().toArray(new MockTestClass[3]);

        for (int i = 0; i < mapKeyArray.length; i++) {
            assertEquals(mapKeyArray[i], copiedMapKeyArray[i]);
            assertFalse(mapKeyArray[i] == copiedMapKeyArray[i]);
        }
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Object...)} throw {@code NullPointerException}
     * if {@code objects} or any element of {@code objects} is null.
     */
    private void assertNullPointerExceptionThrown(Object... objects) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(objects));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Collection<?>)} throw {@code NullPointerException}
     * if {@code collection} or any element of {@code collection} is null.
     */
    private void assertNullPointerExceptionThrown(Collection<?> collection) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(collection));
    }

    private void assertNullPointerExceptionNotThrown(Object... objects) {
        requireAllNonNull(objects);
    }

    private void assertNullPointerExceptionNotThrown(Collection<?> collection) {
        requireAllNonNull(collection);
    }

    private class MockTestClass {

        private final int x;
        private final String y;

        MockTestClass(int x, String y) {
            this.x = x;
            this.y = y;
        }

        MockTestClass deepCopy() {
            return new MockTestClass(x, y);
        }

        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof MockTestClass // instanceof handles nulls
                    && ((MockTestClass) other).x == x
                    && ((MockTestClass) other).y == y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
