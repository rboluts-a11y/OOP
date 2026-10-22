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
     * Добавляет вершину в граф.
     *
     * @param v вершина, которую собираемся добавить
     * @throws IllegalArgumentException если вершина уже есть в графе
     */
    @Override
    public void addVertex(T v) throws IllegalArgumentException {
        if (adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException();
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
        if (!adjacencyList.containsKey(v)) {
            throw new NoSuchElementException();
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
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (adjacencyList.get(from).contains(to)) {
            throw new IllegalArgumentException();
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
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (!adjacencyList.get(from).contains(to)) {
            throw new NoSuchElementException();
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
        if (!adjacencyList.containsKey(v)) {
            throw new NoSuchElementException();
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
