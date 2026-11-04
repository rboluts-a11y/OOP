package org.example;

import java.util.AbstractMap;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Параметризованная хэш-таблица с разрешением коллизий методом цепочек.
 * При превышении коэффициента загрузки массив корзин увеличивается вдвое.
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Node<K, V>[] table;
    private int size;
    private int modCount;

    /**
     * Узел связного списка, хранящий пару «ключ — значение».
     * Содержит неизменяемые ключ и его хэш-код, изменяемое значение и ссылку на следующий узел.
     *
     * @param <K> тип ключей
     * @param <V> тип значений
     */
    private static class Node<K, V> {
        final K key;
        final int hash;
        V value;
        Node<K, V> next;
        Node(K key, int hash, V value, Node <K, V> next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] newTable(int capacity) {
        return (Node <K, V>[]) new Node[capacity];
    }

    /**
     * Конструктор, создает пустую хэш-таблицу.
     */
    public HashTable() {
        table = newTable(DEFAULT_INITIAL_CAPACITY);
    }

    private int getHash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private Node<K, V> findNode(K key) {
        int hash = getHash(key);
        int index = hash & (table.length - 1);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    /**
     * Увеличивает ёмкость таблицы вдвое и перераспределяет узлы по новым корзинам.
     * При этом связные списки заново перестраиваются.
     */
    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        table = newTable(oldCapacity * 2);
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> current = oldTable[i];
            Node<K, V> lowHead = null;
            Node<K, V> lowTail = null;
            Node<K, V> highHead = null;
            Node<K, V> highTail = null;
            while (current != null) {
                Node<K, V> next = current.next;
                current.next = null;
                if ((current.hash & oldCapacity) == 0) {
                    if (lowHead == null) {
                        lowHead = current;
                    } else {
                        lowTail.next = current;
                    }
                    lowTail = current;
                } else {
                    if (highHead == null) {
                        highHead = current;
                    } else {
                        highTail.next = current;
                    }
                    highTail = current;
                }
                current = next;
            }
            table[i] = lowHead;
            table[i + oldCapacity] = highHead;
        }
    }

    /**
     * Внутренний итератор для обхода пар "ключ — значение".
     * Отслеживает структурные изменения хэш-таблицы и выбрасывает
     * ConcurrentModificationException при обнаружении изменений во время обхода.
     */
    private class HashTableIterator implements Iterator<Map.Entry<K, V>> {
        private int bucketIndex;
        private Node<K, V> nextNode;
        private final int expectedModCount;

        /**
         * Создаёт итератор в начальном состоянии и запоминает
         * текущее количество структурных изменений хэш-таблицы.
         */
        private HashTableIterator() {
            bucketIndex = 0;
            nextNode = null;
            expectedModCount = modCount;
        }

        /**
         * Проверяет, не изменялась ли структурно хэш-таблица после создания итератора.
         *
         * @throws ConcurrentModificationException если обнаружены структурные изменения
         */
        private void checkForCoModification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        private void prepareNextNode() {
            while (nextNode == null && bucketIndex < table.length) {
                nextNode = table[bucketIndex];
                bucketIndex++;
            }
        }

        /**
         * Проверяет, остались ли ещё пары для обхода.
         *
         * @return true, если существует следующий элемент, иначе false
         * @throws ConcurrentModificationException если хэш-таблица была структурно изменена
         */
        @Override
        public boolean hasNext() {
            checkForCoModification();
            prepareNextNode();
            return nextNode != null;
        }

        /**
         * Возвращает следующую неизменяемую пару «ключ — значение»
         * и перемещает итератор к следующему элементу.
         *
         * @return следующая пара «ключ — значение»
         * @throws NoSuchElementException если элементы для обхода закончились
         * @throws ConcurrentModificationException если хэш-таблица была структурно изменена
         */
        @Override
        public Map.Entry<K, V> next() {
            checkForCoModification();
            prepareNextNode();
            if (nextNode == null) {
                throw new NoSuchElementException();
            }
            Node<K, V> currentNode = nextNode;
            nextNode = currentNode.next;
            return new AbstractMap.SimpleImmutableEntry<>(currentNode.key, currentNode.value);
        }
    }

    /**
     * Возвращает итератор для обхода пар «ключ — значение».
     *
     * @return итератор по элементам хэш-таблицы
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Формирует строковое представление пар «ключ — значение».
     *
     * @return соответствующая строка из пар, формата [(1 : 2); (3 : 4)]
     * @throws ConcurrentModificationException если таблица структурно изменена во время обхода
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        for (Map.Entry<K, V> entry : this) {
            if (!isFirst) {
                sb.append("; ");
            }
            sb.append("(").append(entry.getKey()).append(" : ")
                    .append(entry.getValue()).append(")");
            isFirst = false;
        }
        sb.append("]");
        return sb.toString();
    }

    private boolean containsMapping(Object key, Object value) {
        int hash = getHash(key);
        int index = hash & (table.length - 1);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                return Objects.equals(currentNode.value, value);
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    /**
     * Метод, определяющий, равен ли объект данной хэш-таблице.
     * Две хэш-таблицы считаются равными, если множества пар "ключ - значение" у них совпадают.
     *
     * @param obj объект, который собираемся проверить на равенство
     * @return булевое значение, означающее, равен ли объект данной хэш-таблице
     * @throws ConcurrentModificationException если таблица структурно изменена во время обхода
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashTable<?, ?>)) {
            return false;
        }
        HashTable<?, ?> other = (HashTable<?, ?>) obj;
        if (size != other.size) {
            return false;
        }
        for (Map.Entry<K, V> entry : this) {
            if (!other.containsMapping(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод, вычисляющий хэш-код данной хэш-таблицы.
     * Так как порядок пар не влияет на равенство хэш-таблиц,
     * хэш-код всей таблицы делается равным сумме хэш-кодов по всем парам.
     *
     * @return значение хэш-кода
     * @throws ConcurrentModificationException если таблица структурно изменена во время обхода
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (Map.Entry<K, V> entry : this) {
            int entryHash = 1;
            entryHash = 31 * entryHash + Objects.hashCode(entry.getKey());
            entryHash = 31 * entryHash + Objects.hashCode(entry.getValue());
            hash += entryHash;
        }
        return hash;
    }

    /**
     * Возвращает количество пар "ключ - значение" в хэш-таблице.
     *
     * @return количество элементов
     */
    public int size() {
        return this.size;
    }

    /**
     * Проверяет, является ли хэш-таблица пустой.
     *
     * @return true, если хэш-таблица не содержит ни одной пары, и false иначе.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет, есть ли в хэш-таблице данный ключ.
     *
     * @param key ключ, который проверяем на существование
     * @return соответствующее булевое значение
     */
    public boolean hasKey(K key) {
        return findNode(key) != null;
    }

    /**
     * Добавляет пару "ключ - значение" в хэш-таблицу.
     * При достаточном количестве элементов расширяет таблицу.
     *
     * @param key ключ
     * @param value устанавливаемое значение
     * @throws IllegalArgumentException если данный ключ уже есть в хэш-таблице
     */
    public void put(K key, V value) {
        if (hasKey(key)) {
            throw new IllegalArgumentException("Элемент с таким ключом уже есть в хэш-таблице!");
        }
        if (size + 1 > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int hash = getHash(key);
        int index = hash & (table.length - 1);
        table[index] = new Node<>(key, hash, value, table[index]);
        size++;
        modCount++;
    }

    /**
     * Возвращает значение по ключу, если данный ключ есть в хэш-таблице.
     *
     * @param key ключ
     * @return значение по данному ключу
     * @throws NoSuchElementException если элемента с данным ключом нет в хэш-таблице
     */
    public V get(K key) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException("Элемента с таким ключом ещё нет в хэш-таблице!");
        }
        return node.value;
    }

    /**
     * Метод, который изменяет значение по ключу уже существующего элемента.
     *
     * @param key ключ
     * @param value новое значение по ключу
     * @throws NoSuchElementException если элемента с данным ключом нет в хэш-таблице
     */
    public void update(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException("Элемента с таким ключом ещё нет в хэш-таблице!");
        }
        node.value = value;
    }

    /**
     * Удаляет пару "ключ - значение" из хэш-таблицы по заданному ключу.
     *
     * @param key ключ, который собираемся удалить из хэш-таблицы
     * @return значение по ключу
     * @throws NoSuchElementException если элемента с данным ключом нет в хэш-таблице
     */
    public V remove(K key) {
        int hash = getHash(key);
        int index = hash & (table.length - 1);
        Node<K, V> previousNode = null;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                if (previousNode == null) {
                    table[index] = currentNode.next;
                } else {
                    previousNode.next = currentNode.next;
                }
                size--;
                modCount++;
                return currentNode.value;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        throw new NoSuchElementException("Элемента с таким ключом ещё нет в хэш-таблице!");
    }

    /**
     * Метод, который удаляет пару "ключ - значение", при заданном ключе и значении.
     * Если соответствующего элемента нет в хэш-таблице, ничего с ней не происходит.
     *
     * @param key ключ
     * @param value значение
     * @return true, если пара была удалена, иначе false
     */
    public boolean remove(K key, V value) {
        int hash = getHash(key);
        int index = hash & (table.length - 1);
        Node<K, V> previousNode = null;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash && Objects.equals(currentNode.key, key)) {
                if (!Objects.equals(currentNode.value, value)) {
                    return false;
                }
                if (previousNode == null) {
                    table[index] = currentNode.next;
                } else {
                    previousNode.next = currentNode.next;
                }
                size--;
                modCount++;
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return false;
    }
}