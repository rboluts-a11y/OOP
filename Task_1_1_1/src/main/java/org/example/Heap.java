package org.example;

/**
 * Структура данных Двоичная куча на минимуме (Binary Min-Heap).
 * Используется для эффективного поиска и извлечения минимального элемента.
 * Асимптотика по памяти O(N), по времени O(N*log(N)), где N - количество операций над структурой.
 */
public class Heap {
    private int currentSize;
    private int[] array;
    /**
     * Конструктор инициализирует кучу под максимальное количество элементов.
     *
     * @param capacity максимальная емкость кучи (размер массива)
     */
    public Heap(int capacity) {
        array = new int [capacity];
        currentSize = 0;
    }

    /**
     * Вспомогательный метод для обмена местами двух элементов массива.
     *
     * @param indexFirst индекс первого элемента
     *
     * @param indexSecond индекс второго элемента
     */
    private void swapIndices(int indexFirst, int indexSecond) {
        int val = array[indexFirst];
        array[indexFirst] = array[indexSecond];
        array[indexSecond] = val;
    }

    /**
     * Просеивание элемента вверх для восстановления свойств кучи после вставки.
     *
     * @param currentIndex индекс просеиваемого элемента
     */
    private void shiftUp(int currentIndex) {
        int indexParent = (currentIndex - 1) / 2;
        if (currentIndex > 0 && array[currentIndex] < array[indexParent]) {
            swapIndices(currentIndex, indexParent);
            shiftUp(indexParent);
        }
    }

    /**
     * Просеивание элемента вниз для восстановления свойств кучи после извлечения корня.
     *
     * @param currentIndex индекс просеиваемого элемента
     */
    private void shiftDown(int currentIndex) {
        int indexLeftChild = 2 * currentIndex + 1;
        int indexRightChild = 2 * currentIndex + 2;
        int indexMinElement = currentIndex;
        if (indexLeftChild < currentSize && array[indexLeftChild] < array[indexMinElement]) {
            indexMinElement = indexLeftChild;
        }
        if (indexRightChild < currentSize && array[indexRightChild] < array[indexMinElement]) {
            indexMinElement = indexRightChild;
        }
        if (indexMinElement != currentIndex) {
            swapIndices(indexMinElement, currentIndex);
            shiftDown(indexMinElement);
        }
    }

    /**
     * Добавляет новый элемент в кучу и просеивает вверх его на нужное место.
     *
     * @param newValue новое значение для вставки в кучу
     *
     * @throws IndexOutOfBoundsException если куча уже полностью заполнена
     */
    public void addNewElement(int newValue) {
        if (currentSize >= array.length) {
            throw new IndexOutOfBoundsException("Куча переполнена! Невозможно добавить элемент.");
        }
        array[currentSize] = newValue;
        currentSize++;
        shiftUp(currentSize - 1);
    }

    /**
     * Извлекает минимальный элемент из корня кучи и перестраивает её структуру.
     *
     * @return минимальный элемент, хранившийся в куче
     *
     * @throws IllegalStateException если куча пуста
     */
    public int extractMinElement() {
        if (currentSize == 0) {
            throw new IllegalStateException("Куча пуста! Невозможно извлечь элемент.");
        }
        final int minElement = array[0];
        swapIndices(0, currentSize - 1);
        currentSize--;
        shiftDown(0);
        return minElement;
    }

    /**
     * Метод принимает неотсортированный массив чисел типа int
     * и возвращает новый, отсортированный по возрастанию.
     * Реализует алгоритм Heapsort с временной сложностью O(N log N).
     *
     * @param givenArray исходный массив целых чисел
     *
     * @return новый массив, элементы которого отсортированы по возрастанию
     */
    public static int[] heapsort(int[] givenArray) {
        int sizeArray = givenArray.length;
        Heap heapToSort = new Heap(sizeArray);
        int[] sortedArray = new int [sizeArray];
        for (int element : givenArray) {
            heapToSort.addNewElement(element);
        }
        for (int i = 0; i < sizeArray; i++) {
            sortedArray[i] = heapToSort.extractMinElement();
        }
        return sortedArray;
    }
}
