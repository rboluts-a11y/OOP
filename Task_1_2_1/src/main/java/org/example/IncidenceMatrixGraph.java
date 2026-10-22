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
        OUTGOING(-1),
        NONE(0),
        INCOMING(1),
        LOOP(2);

        private final int value;

        Incidence(int value) {
            this.value = value;
        }
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

    private int getEdgesCount() {
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
        if (this.indexOf(v) != -1) {
            throw new IllegalArgumentException();
        }
        vertices.add(v);
        incidenceMatrix.add(new ArrayList<>());
        int edgesCount = this.getEdgesCount();
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
        int indexVertex = this.indexOf(v);
        if (indexVertex == -1) {
            throw new NoSuchElementException();
        }
        List <Integer> indicesEdgesSaved = new ArrayList<>();
        int allEdgesCount = this.getEdgesCount();
        for (int j = 0; j < allEdgesCount; j++) {
            if (incidenceMatrix.get(indexVertex).get(j) == Incidence.NONE) {
                indicesEdgesSaved.add(j);
            }
        }
        vertices.remove(indexVertex);
        incidenceMatrix.remove(indexVertex);
        for (int i = 0; i < incidenceMatrix.size(); i++) {
            List <Incidence> valuesSaved = new ArrayList<>();
            for (Integer indexEdge : indicesEdgesSaved) {
                valuesSaved.add(incidenceMatrix.get(i).get(indexEdge));
            }
            incidenceMatrix.set(i, valuesSaved);
        }
    }

    private int findEdge(int from, int to) {
        int indexEdge = -1;
        int edgeCount = this.getEdgesCount();
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
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        if (indexFrom == -1 || indexTo == -1) {
            throw new NoSuchElementException();
        }
        int indexEdge = this.findEdge(indexFrom, indexTo);
        if (indexEdge != -1) {
            throw new IllegalArgumentException();
        }
        int edgeCount = this.getEdgesCount();
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
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        if (indexFrom == -1 || indexTo == -1) {
            throw new NoSuchElementException();
        }
        int indexEdge = this.findEdge(indexFrom, indexTo);
        if (indexEdge == -1) {
            throw new NoSuchElementException();
        }
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
        int indexVertex = this.indexOf(v);
        if (indexVertex == -1) {
            throw new NoSuchElementException();
        }
        List <T> adjacentVertices = new ArrayList<>();
        int vertexCount = incidenceMatrix.size();
        int edgeCount = this.getEdgesCount();
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
