package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Класс, который реализует операции над графом, представленным в виде списка смежности.
 *
 * @param <T> тип вершин
 */
public class AdjacencyListGraph<T> extends AbstractGraph<T>  {
    private final Map<T, List<T>> adjacencyList;

    /**
     * Конструктор, который создает пустой граф.
     *
     * @param vertexType тип вершин
     */
    public AdjacencyListGraph(Class<T> vertexType) {
        super(vertexType);
        adjacencyList = new HashMap<>();
    }

    /**
     * Проверяет, есть ли такая вершина в графе.
     *
     * @param v проверяемая вершина
     * @return соответствующее булевое значение
     */
    @Override
    public boolean hasVertex(T v) {
        return adjacencyList.containsKey(v);
    }

    /**
     * Проверяет, есть ли такое ребро в графе.
     *
     * @param from вершина, откуда должно исходить ребро
     * @param to   вершина, куда должно входить ребро
     * @return соответствующее булевое значение
     * @throws NoSuchElementException если какой-либо из двух вершин нет в графе
     */
    @Override
    public boolean hasEdge(T from, T to) throws NoSuchElementException {
        if (!this.hasVertex(from)) {
            throw new NoSuchElementException("Вершины " + from + " в графе нет!");
        }
        if (!this.hasVertex(to)) {
            throw new NoSuchElementException("Вершины " + to + " в графе нет!");
        }
        return adjacencyList.get(from).contains(to);
    }

    /**
     * Возвращает количество вершин в графе.
     *
     * @return количество вершин
     */
    @Override
    public int vertexCount() {
        return adjacencyList.size();
    }

    /**
     * Возвращает количество ребер в графе.
     *
     * @return количество ребер
     */
    @Override
    public int edgeCount() {
        int edgeCountCur = 0;
        for (Map.Entry<T, List<T>> entry : adjacencyList.entrySet()) {
            edgeCountCur += entry.getValue().size();
        }
        return edgeCountCur;
    }

    /**
     * Добавляет вершину в граф.
     *
     * @param v вершина, которую собираемся добавить
     * @throws IllegalArgumentException если вершина уже есть в графе
     */
    @Override
    public void addVertex(T v) throws IllegalArgumentException {
        if (this.hasVertex(v)) {
            throw new IllegalArgumentException("Вершина " + v + " уже существует в графе!");
        }
        adjacencyList.put(v, new ArrayList<>());
    }

    /**
     * Удаляет вершину из графа вместе со всеми смежными с ней ребрами.
     *
     * @param v вершина, которую собираемся удалить
     * @throws NoSuchElementException если вершины нет в графе
     */
    @Override
    public void removeVertex(T v) throws NoSuchElementException {
        if (!this.hasVertex(v)) {
            throw new NoSuchElementException("Вершины " + v + " в графе нет!");
        }
        for (T from : adjacencyList.keySet()) {
            adjacencyList.get(from).remove(v);
        }
        adjacencyList.remove(v);
    }

    /**
     * Добавляет ориентированное ребро в граф.
     *
     * @param from вершина, из которой идет ребро
     * @param to вершина, в которую ведет ребро
     * @throws NoSuchElementException если какой-то из двух вершин нет в графе
     * @throws IllegalArgumentException если данное ребро уже существует в графе
     */
    @Override
    public void addEdge(T from, T to) throws NoSuchElementException, IllegalArgumentException {
        if (this.hasEdge(from, to)) {
            throw new IllegalArgumentException(
                    "В графе уже присутствует ребро " + from + " -> " + to + " !"
            );
        }
        adjacencyList.get(from).add(to);
    }

    /**
     * Удаляет ориентированное ребро из графа.
     *
     * @param from вершина, из которой идет ребро
     * @param to вершина, в которую ведет ребро
     * @throws NoSuchElementException если какой-то из двух вершин или ребра между ними нет в графе
     */
    @Override
    public void removeEdge(T from, T to) throws NoSuchElementException {
        if (!this.hasEdge(from, to)) {
            throw new NoSuchElementException(
                    "Ребра " + from + " -> " + to + " в графе нет!"
            );
        }
        adjacencyList.get(from).remove(to);
    }

    /**
     * Метод, который возвращает список всех вершин, таких что есть прямое ребро из данной вершины.
     *
     * @param v вершина, для которой собираемся найти всех ее "соседей"
     * @return список (List) всех "соседей"
     * @throws NoSuchElementException если вершины нет в графе
     */
    @Override
    public List<T> getOutgoingVertices(T v) throws NoSuchElementException {
        if (!this.hasVertex(v)) {
            throw new NoSuchElementException("Вершины " + v + " в графе нет!");
        }
        return new ArrayList<>(adjacencyList.get(v));
    }

    /**
     * Метод, который возвращает список всех вершин графа.
     *
     * @return список (List) всех вершин графа.
     */
    @Override
    public List<T> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }
}
