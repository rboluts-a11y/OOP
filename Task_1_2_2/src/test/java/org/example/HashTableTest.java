package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HashTableTest {
    private static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    private static <K, V> Set<Map.Entry<K, V>> actualEntries(HashTable<K, V> table) {
        Set<Map.Entry<K, V>> result = new HashSet<>();
        for (Map.Entry<K, V> currentEntry : table) {
            result.add(currentEntry);
        }
        return result;
    }

    @Test
    void newTableIsEmpty() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertFalse(table.hasKey("missing"));
        assertEquals("[]", table.toString());
        assertFalse(table.iterator().hasNext());
    }

    @Test
    void putAndGetWork() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        assertEquals(2, table.size());
        assertFalse(table.isEmpty());
        assertTrue(table.hasKey("one"));
        assertTrue(table.hasKey("two"));
        assertEquals(Integer.valueOf(1), table.get("one"));
        assertEquals(Integer.valueOf(2), table.get("two"));
    }

    @Test
    void differentGenericTypesAreSupported() {
        HashTable<Integer, String> first = new HashTable<>();
        first.put(42, "answer");
        assertEquals("answer", first.get(42));
        HashTable<String, Number> second = new HashTable<>();
        second.put("one", 1);
        second.update("one", 1.0);
        assertEquals(1.0, second.get("one"));
    }

    @Test
    void nullKeyAndNullValueAreSupported() {
        HashTable<String, String> table = new HashTable<>();
        table.put(null, null);
        table.put("null-value", null);
        assertEquals(2, table.size());
        assertTrue(table.hasKey(null));
        assertTrue(table.hasKey("null-value"));
        assertNull(table.get(null));
        assertNull(table.get("null-value"));
        table.update(null, "not-null-anymore");
        assertEquals("not-null-anymore", table.get(null));
        assertNull(table.remove("null-value"));
        assertFalse(table.hasKey("null-value"));
        assertEquals("not-null-anymore", table.remove(null));
        assertFalse(table.hasKey(null));
        assertTrue(table.isEmpty());
    }

    @Test
    void putRejectsAnAlreadyPresentKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 10);
        assertThrows(IllegalArgumentException.class, () -> table.put("key", 20));
        assertEquals(1, table.size());
        assertEquals(Integer.valueOf(10), table.get("key"));
    }

    @Test
    void putRejectsDuplicateNullKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, 10);
        assertThrows(IllegalArgumentException.class, () -> table.put(null, 20));
        assertEquals(1, table.size());
        assertEquals(Integer.valueOf(10), table.get(null));
    }

    @Test
    void getThrowsWhenKeyIsAbsent() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.get("missing"));
    }

    @Test
    void updateChangesExistingValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("answer", 41);
        table.update("answer", 42);
        assertEquals(Integer.valueOf(42), table.get("answer"));
        assertEquals(1, table.size());
    }

    @Test
    void updateCanSetValueToNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 10);
        table.update("key", null);
        assertTrue(table.hasKey("key"));
        assertNull(table.get("key"));
    }

    @Test
    void updateThrowsWhenKeyIsAbsent() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.update("missing", 10));
        assertEquals(0, table.size());
    }

    @Test
    void removeByKeyReturnsValueAndDeletesMapping() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 10);
        Integer removedValue = table.remove("key");
        assertEquals(Integer.valueOf(10), removedValue);
        assertFalse(table.hasKey("key"));
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void removeByKeyCanDeleteMappingWhoseValueIsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", null);
        assertNull(table.remove("key"));
        assertFalse(table.hasKey("key"));
        assertEquals(0, table.size());
    }

    @Test
    void removeByKeyThrowsWhenKeyIsAbsent() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.remove("missing"));
        assertEquals(0, table.size());
    }

    @Test
    void removeByKeyAndValueRemovesExactMapping() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 10);
        assertTrue(table.remove("key", 10));
        assertFalse(table.hasKey("key"));
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void removeByKeyAndValueDoesNotRemoveWhenValueDiffers() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 10);
        assertFalse(table.remove("key", 20));
        assertTrue(table.hasKey("key"));
        assertEquals(Integer.valueOf(10), table.get("key"));
        assertEquals(1, table.size());
    }

    @Test
    void removeByKeyAndValueReturnsFalseWhenKeyIsAbsent() {
        HashTable<String, Integer> table = new HashTable<>();
        assertFalse(table.remove("missing", 10));
        assertTrue(table.isEmpty());
    }

    @Test
    void removeByKeyAndValueSupportsNullKeyAndValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, null);
        assertFalse(table.remove(null, 10));
        assertTrue(table.hasKey(null));
        assertTrue(table.remove(null, null));
        assertFalse(table.hasKey(null));
    }

    @Test
    void sizeAndIsEmptyChangeOnlyAfterSuccessfulStructuralOperations() {
        HashTable<Integer, Integer> table = new HashTable<>();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        table.put(1, 10);
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
        table.put(2, 20);
        assertEquals(2, table.size());
        assertThrows(IllegalArgumentException.class, () -> table.put(2, 200));
        assertEquals(2, table.size());
        assertFalse(table.remove(2, 200));
        assertEquals(2, table.size());
        table.remove(1);
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
        assertThrows(NoSuchElementException.class, () -> table.remove(100));
        assertEquals(1, table.size());
        table.remove(2);
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void differentKeysWithSameHashCodeCanCoexist() {
        HashTable<TestKey, String> table = new HashTable<>();
        final TestKey first = new TestKey("first", 7);
        final TestKey second = new TestKey("second", 7);
        final TestKey third = new TestKey("third", 7);
        table.put(first, "A");
        table.put(second, "B");
        table.put(third, "C");
        assertEquals(3, table.size());
        assertEquals("A", table.get(new TestKey("first", 7)));
        assertEquals("B", table.get(new TestKey("second", 7)));
        assertEquals("C", table.get(new TestKey("third", 7)));
    }

    @Test
    void removingHeadMiddleAndTailOfCollisionChainWorks() {
        HashTable<TestKey, String> table = new HashTable<>();
        final TestKey first = new TestKey("first", 5);
        final TestKey middle = new TestKey("middle", 5);
        final TestKey last = new TestKey("last", 5);
        table.put(first, "A");
        table.put(middle, "B");
        table.put(last, "C");
        assertEquals("B", table.remove(new TestKey("middle", 5)));
        assertTrue(table.hasKey(first));
        assertTrue(table.hasKey(last));
        assertFalse(table.hasKey(middle));
        assertEquals("C", table.remove(last));
        assertEquals("A", table.remove(first));
        assertTrue(table.isEmpty());
    }

    @Test
    void resizeAppendsNodesToBothLowAndHighSplitChains() {
        HashTable<TestKey, String> table = new HashTable<>();
        TestKey lowFirst = new TestKey("low-first", 1);
        TestKey lowSecond = new TestKey("low-second", 33);
        TestKey highFirst = new TestKey("high-first", 17);
        TestKey highSecond = new TestKey("high-second", 49);
        table.put(lowFirst, "low-1");
        table.put(lowSecond, "low-2");
        table.put(highFirst, "high-1");
        table.put(highSecond, "high-2");
        for (int i = 2; i <= 10; i++) {
            table.put(new TestKey("filler-" + i, i), "value-" + i);
        }
        assertEquals(13, table.size());
        assertEquals("low-1", table.get(new TestKey("low-first", 1)));
        assertEquals("low-2", table.get(new TestKey("low-second", 33)));
        assertEquals("high-1", table.get(new TestKey("high-first", 17)));
        assertEquals("high-2", table.get(new TestKey("high-second", 49)));
        assertEquals(13, actualEntries(table).size());
        assertEquals("low-1", table.remove(lowFirst));
        assertEquals("low-2", table.remove(lowSecond));
        assertEquals("high-1", table.remove(highFirst));
        assertEquals("high-2", table.remove(highSecond));
    }

    @Test
    void multipleResizesPreserveAllMappings() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 200; i++) {
            table.put(i, i * 10);
        }
        assertEquals(200, table.size());
        for (int i = 0; i < 200; i++) {
            assertEquals(Integer.valueOf(i * 10), table.get(i));
        }
        assertEquals(200, actualEntries(table).size());
    }

    @Test
    void emptyTableHasNoNextElement() {
        HashTable<String, Integer> table = new HashTable<>();
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteratorVisitsEveryMappingAcrossBucketsAndChains() {
        HashTable<TestKey, Integer> table = new HashTable<>();
        final TestKey first = new TestKey("first", 1);
        final TestKey second = new TestKey("second", 17);
        final TestKey third = new TestKey("third", 33);
        final TestKey anotherBucket = new TestKey("another", 2);
        table.put(first, 10);
        table.put(second, 20);
        table.put(third, 30);
        table.put(anotherBucket, 40);
        Set<Map.Entry<TestKey, Integer>> expected = new HashSet<>(Arrays.asList(
                entry(first, 10),
                entry(second, 20),
                entry(third, 30),
                entry(anotherBucket, 40)
        ));
        assertEquals(expected, actualEntries(table));
        assertEquals(4, actualEntries(table).size());
    }

    @Test
    void hasNextDoesNotConsumeElement() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        for (int i = 1; i <= 3; i++) {
            assertTrue(iterator.hasNext());
        }
        assertEquals(entry("one", 1), iterator.next());
    }

    @Test
    void nextWorksWithoutCallingHasNextFirst() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertEquals(entry("one", 1), iterator.next());
    }

    @Test
    void nextThrowsAfterAllElementsWereReturned() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertEquals(entry("one", 1), iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void returnedEntriesAreImmutable() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Map.Entry<String, Integer> returnedEntry = table.iterator().next();
        assertThrows(UnsupportedOperationException.class, () -> returnedEntry.setValue(100));
        assertEquals(Integer.valueOf(1), table.get("one"));
    }

    @Test
    void iteratorDetectsInsertion() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.put("two", 2);
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
    }

    @Test
    void iteratorDetectsRemoval() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.remove("one");
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void iteratorDetectsInsertionThatTriggersResize() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 12; i++) {
            table.put(i, i);
        }
        Iterator<Map.Entry<Integer, Integer>> iterator = table.iterator();
        table.put(12, 12);
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
    }

    @Test
    void updatingValueDoesNotInvalidateIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        table.update("one", 2);
        assertTrue(iterator.hasNext());
        assertEquals(entry("one", 2), iterator.next());
    }

    @Test
    void unsuccessfulOperationsDoNotInvalidateIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertThrows(IllegalArgumentException.class, () -> table.put("one", 10));
        assertThrows(NoSuchElementException.class, () -> table.remove("missing"));
        assertThrows(NoSuchElementException.class, () -> table.update("missing", 10));
        assertFalse(table.remove("one", 10));
        assertEquals(entry("one", 1), iterator.next());
    }

    @Test
    void successfulRemoveByKeyAndValueInvalidatesIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> iterator = table.iterator();
        assertTrue(table.remove("one", 1));
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
    }

    @Test
    void eachCallToIteratorCreatesIndependentTraversal() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        Iterator<Map.Entry<String, Integer>> first = table.iterator();
        Iterator<Map.Entry<String, Integer>> second = table.iterator();
        assertEquals(first.next(), second.next());
        assertTrue(first.hasNext());
        assertTrue(second.hasNext());
        assertEquals(first.next(), second.next());
    }

    @Test
    void tableEqualsItself() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertEquals(table, table);
    }

    @Test
    void tableDoesNotEqualNullOrObjectOfAnotherType() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertNotEquals(null, table);
        assertNotEquals("not a hash table", table);
    }

    @Test
    void tablesWithSameMappingsAndDifferentInsertionOrderAreEqual() {
        final HashTable<String, Integer> first = new HashTable<>();
        final HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        first.put("two", 2);
        first.put("three", 3);
        second.put("three", 3);
        second.put("one", 1);
        second.put("two", 2);
        assertEquals(first, second);
        assertEquals(second, first);
    }

    @Test
    void tablesWithDifferentValuesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        second.put("one", 2);
        assertNotEquals(first, second);
    }

    @Test
    void tablesWithDifferentKeysAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        second.put("two", 1);
        assertNotEquals(first, second);
    }

    @Test
    void tablesWithDifferentSizesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        first.put("two", 2);
        second.put("one", 1);
        assertNotEquals(first, second);
    }

    @Test
    void equalitySupportsNullKeysAndValues() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put(null, null);
        first.put("one", null);
        second.put("one", null);
        second.put(null, null);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalityDoesNotDependOnCapacity() {
        final HashTable<Integer, Integer> largerCapacity = new HashTable<>();
        final HashTable<Integer, Integer> smallerCapacity = new HashTable<>();
        for (int i = 0; i < 13; i++) {
            largerCapacity.put(i, i * 10);
        }
        largerCapacity.remove(12);
        for (int i = 0; i < 12; i++) {
            smallerCapacity.put(i, i * 10);
        }
        assertEquals(largerCapacity, smallerCapacity);
        assertEquals(largerCapacity.hashCode(), smallerCapacity.hashCode());
    }

    @Test
    void differentGenericParametersCanStillRepresentEqualMappings() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<Object, Number> second = new HashTable<>();
        first.put("answer", 42);
        second.put("answer", 42);
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalTablesHaveEqualHashCodesRegardlessOfInsertionOrder() {
        final HashTable<Integer, String> first = new HashTable<>();
        final HashTable<Integer, String> second = new HashTable<>();
        first.put(1, "one");
        first.put(2, "two");
        second.put(2, "two");
        second.put(1, "one");
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void emptyTableIsFormattedAsEmptyBrackets() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals("[]", table.toString());
    }

    @Test
    void singleMappingIsFormattedCorrectly() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertEquals("[(one : 1)]", table.toString());
    }

    @Test
    void multipleMappingsAreSeparatedAndAllIncluded() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        String result = table.toString();
        assertTrue(result.startsWith("["));
        assertTrue(result.endsWith("]"));
        assertTrue(result.contains("(one : 1)"));
        assertTrue(result.contains("(two : 2)"));
        assertTrue(result.contains("(three : 3)"));
        assertEquals(2, result.split("; ", -1).length - 1);
    }

    @Test
    void nullKeyAndValueArePrintedAsNull() {
        HashTable<String, String> table = new HashTable<>();
        table.put(null, null);
        assertEquals("[(null : null)]", table.toString());
    }

    @Test
    void nullAndLiteralNullAreDifferentButHaveSameRepresentation() {
        HashTable<String, String> nullTable = new HashTable<>();
        nullTable.put(null, null);
        HashTable<String, String> literalNullTable = new HashTable<>();
        literalNullTable.put("null", "null");
        assertEquals("[(null : null)]", nullTable.toString());
        assertEquals(nullTable.toString(), literalNullTable.toString());
        assertNotEquals(nullTable, literalNullTable);
    }
}

final class TestKey {
    private final String id;
    private final int hash;

    TestKey(String id, int hash) {
        this.id = id;
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TestKey)) {
            return false;
        }
        TestKey other = (TestKey) obj;
        return id.equals(other.id) && hash == other.hash;
    }

    @Override
    public String toString() {
        return id;
    }
}
