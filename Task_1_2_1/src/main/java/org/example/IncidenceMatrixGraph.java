package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Класс, который реализует операции над графом, представленным в виде матрицы инцидентности.
 *
 * @param <T> тип вершин
 */
public class IncidenceMatrixGraph<T> extends AbstractGraph<T>  {
    private enum Incidence {
        OUTGOING,
        NONE,
        INCOMING,
        LOOP;
    }

    private final List<T> vertices;

    private final List<List<Incidence>> incidenceMatrix;

    /**
     * Конструктор, который создает пустой граф.
     *
     * @param vertexType тип вершин
     */
    public IncidenceMatrixGraph(Class<T> vertexType) {
        super(vertexType);
        vertices = new ArrayList<>();
        incidenceMatrix = new ArrayList<>();
    }

    private int indexOf(T v) {
        return vertices.indexOf(v);
    }

    /**
     * Проверяет, есть ли такая вершина в графе.
     *
     * @param v проверяемая вершина
     * @return соответствующее булевое значение
     */
    @Override
    public boolean hasVertex(T v) {
        return vertices.contains(v);
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
        int edgeCount = this.edgeCount();
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        for (int j = 0; j < edgeCount; j++) {
            if (from == to) {
                if (incidenceMatrix.get(indexFrom).get(j) == Incidence.LOOP) {
                    return true;
                }
            } else {
                if (incidenceMatrix.get(indexFrom).get(j) == Incidence.OUTGOING
                        && incidenceMatrix.get(indexTo).get(j) == Incidence.INCOMING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Возвращает количество вершин в графе.
     *
     * @return количество вершин
     */
    @Override
    public int vertexCount() {
        return vertices.size();
    }

    /**
     * Возвращает количество ребер в графе.
     *
     * @return количество ребер
     */
    @Override
    public int edgeCount() {
        if (incidenceMatrix.isEmpty()) {
            return 0;
        }
        return incidenceMatrix.get(0).size();
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
        vertices.add(v);
        incidenceMatrix.add(new ArrayList<>());
        int edgesCount = this.edgeCount();
        for (int j = 0; j < edgesCount; j++) {
            incidenceMatrix.get(incidenceMatrix.size() - 1).add(Incidence.NONE);
        }
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
        int indexVertex = this.indexOf(v);
        List<Integer> indicesEdgesSaved = new ArrayList<>();
        int allEdgesCount = this.edgeCount();
        for (int j = 0; j < allEdgesCount; j++) {
            if (incidenceMatrix.get(indexVertex).get(j) == Incidence.NONE) {
                indicesEdgesSaved.add(j);
            }
        }
        vertices.remove(indexVertex);
        incidenceMatrix.remove(indexVertex);
        for (int i = 0; i < incidenceMatrix.size(); i++) {
            List<Incidence> valuesSaved = new ArrayList<>();
            for (Integer indexEdge : indicesEdgesSaved) {
                valuesSaved.add(incidenceMatrix.get(i).get(indexEdge));
            }
            incidenceMatrix.set(i, valuesSaved);
        }
    }

    private int findEdge(int from, int to) {
        int indexEdge = -1;
        int edgeCount = this.edgeCount();
        for (int j = 0; j < edgeCount; j++) {
            if (from == to) {
                if (incidenceMatrix.get(from).get(j) == Incidence.LOOP) {
                    indexEdge = j;
                }
            } else {
                if (incidenceMatrix.get(from).get(j) == Incidence.OUTGOING
                        && incidenceMatrix.get(to).get(j) == Incidence.INCOMING) {
                    indexEdge = j;
                }
            }
        }
        return indexEdge;
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
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        int edgeCount = this.edgeCount();
        for (int i = 0; i < vertices.size(); i++) {
            incidenceMatrix.get(i).add(Incidence.NONE);
        }
        if (indexFrom == indexTo) {
            incidenceMatrix.get(indexFrom).set(edgeCount, Incidence.LOOP);
        } else {
            incidenceMatrix.get(indexFrom).set(edgeCount, Incidence.OUTGOING);
            incidenceMatrix.get(indexTo).set(edgeCount, Incidence.INCOMING);
        }
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
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        int indexEdge = this.findEdge(indexFrom, indexTo);
        for (int i = 0; i < vertices.size(); i++) {
            incidenceMatrix.get(i).remove(indexEdge);
        }
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
        int indexVertex = this.indexOf(v);
        List<T> adjacentVertices = new ArrayList<>();
        int vertexCount = incidenceMatrix.size();
        int edgeCount = this.edgeCount();
        for (int j = 0; j < edgeCount; j++) {
            if (incidenceMatrix.get(indexVertex).get(j) == Incidence.LOOP) {
                adjacentVertices.add(v);
            } else if (incidenceMatrix.get(indexVertex).get(j) == Incidence.OUTGOING) {
                for (int i = 0; i < vertexCount; i++) {
                    if (incidenceMatrix.get(i).get(j) == Incidence.INCOMING) {
                        adjacentVertices.add(vertices.get(i));
                    }
                }
            }
        }
        return adjacentVertices;
    }

    /**
     * Метод, который возвращает список всех вершин графа.
     *
     * @return список (List) всех вершин графа.
     */
    @Override
    public List<T> getVertices() {
        return new ArrayList<>(vertices);
    }
}
