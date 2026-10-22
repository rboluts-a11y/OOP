package org.example;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Интерфейс графа, предоставляющие необходимые методы для работы с графом.
 * Повторяющиеся вершины и кратные ребра запрещены, петли и обратные ребра разрешены.
 *
 * @param <T> тип вершины (например, String или Integer)
 */
public interface Graph<T> {
    /**
     * Проверяет, есть ли такая вершина в графе.
     *
     * @param v проверяемая вершина
     * @return соответствующее булевое значение
     */
    boolean hasVertex(T v);

    /**
     * Проверяет, есть ли такое ребро в графе.
     *
     * @param from вершина, откуда должно исходить ребро
     * @param to вершина, куда должно входить ребро
     * @return соответствующее булевое значение
     * @throws NoSuchElementException если какой-либо из двух вершин нет в графе
     */
    boolean hasEdge(T from, T to) throws NoSuchElementException;

    /**
     * Возвращает количество вершин в графе.
     *
     * @return количество вершин
     */
    int vertexCount();

    /**
     * Возвращает количество ребер в графе.
     *
     * @return количество ребер
     */
    int edgeCount();

    /**
     * Добавляет вершину в граф.
     *
     * @param v вершина, которую собираемся добавить
     * @throws IllegalArgumentException если вершина уже есть в графе
     */
    void addVertex(T v) throws IllegalArgumentException;

    /**
     * Удаляет вершину из графа вместе со всеми смежными с ней ребрами.
     *
     * @param v вершина, которую собираемся удалить
     * @throws NoSuchElementException если вершины нет в графе
     */
    void removeVertex(T v) throws NoSuchElementException;

    /**
     * Добавляет ориентированное ребро в граф.
     *
     * @param from вершина, из которой идет ребро
     * @param to вершина, в которую ведет ребро
     * @throws NoSuchElementException если какой-то из двух вершин нет в графе
     * @throws IllegalArgumentException если данное ребро уже существует в графе
     */
    void addEdge(T from, T to) throws NoSuchElementException, IllegalArgumentException;

    /**
     * Удаляет ориентированное ребро из графа.
     *
     * @param from вершина, из которой идет ребро
     * @param to вершина, в которую ведет ребро
     * @throws NoSuchElementException если какой-то из двух вершин или ребра между ними нет в графе
     */
    void removeEdge(T from, T to) throws NoSuchElementException;

    /**
     * Метод, который возвращает список всех вершин, таких что есть прямое ребро из данной вершины.
     *
     * @param v вершина, для которой собираемся найти всех ее "соседей"
     * @return список (List) всех "соседей"
     * @throws NoSuchElementException если вершины нет в графе
     */
    List<T> getOutgoingVertices(T v) throws NoSuchElementException;

    /**
     * Метод, который возвращает список всех вершин графа.
     *
     * @return список (List) всех вершин графа.
     */
    List<T> getVertices();
}
