package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class AdjacencyMatrixGraph<T> extends AbstractGraph<T> {
    private final List<T> vertices;
    private final List<List<Boolean>> adjacencyMatrix;
    public AdjacencyMatrixGraph(Class<T> vertexType) {
        super(vertexType);
        vertices = new ArrayList<>();
        adjacencyMatrix = new ArrayList<>();
    }

    private int indexOf(T v) {
        return vertices.indexOf(v);
    }

    @Override
    public void addVertex(T v) throws IllegalArgumentException {
        if (this.indexOf(v) != -1) {
            throw new IllegalArgumentException();
        }
        vertices.add(v);
        adjacencyMatrix.add(new ArrayList<>());
        int vertexCount = vertices.size();
        for (int i = 0; i < vertexCount - 1; i++) {
            adjacencyMatrix.get(i).add(false);
        }
        for (int j = 0; j < vertexCount; j++) {
            adjacencyMatrix.get(vertexCount - 1).add(false);
        }
    }

    @Override
    public void removeVertex(T v) throws NoSuchElementException {
        int indexVertex = this.indexOf(v);
        if (indexVertex == -1) {
            throw new NoSuchElementException();
        }
        vertices.remove(indexVertex);
        adjacencyMatrix.remove(indexVertex);
        int vertexCount = vertices.size();
        for (int i = 0; i < vertexCount; i++) {
            adjacencyMatrix.get(i).remove(indexVertex);
        }
    }

    @Override
    public void addEdge(T from, T to) throws NoSuchElementException, IllegalArgumentException {
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        if (indexFrom == -1 || indexTo == -1) {
            throw new NoSuchElementException();
        }
        if (adjacencyMatrix.get(indexFrom).get(indexTo)) {
            throw new IllegalArgumentException();
        }
        adjacencyMatrix.get(indexFrom).set(indexTo, true);
    }

    @Override
    public void removeEdge(T from, T to) throws NoSuchElementException {
        int indexFrom = this.indexOf(from);
        int indexTo = this.indexOf(to);
        if (indexFrom == -1 || indexTo == -1) {
            throw new NoSuchElementException();
        }
        if (!adjacencyMatrix.get(indexFrom).get(indexTo)) {
            throw new NoSuchElementException();
        }
        adjacencyMatrix.get(indexFrom).set(indexTo, false);
    }

    @Override
    public List<T> getOutgoingVertices(T v) throws NoSuchElementException {
        int indexVertex = this.indexOf(v);
        if (indexVertex == -1) {
            throw new NoSuchElementException();
        }
        int vertexCount = vertices.size();
        List <T> listAdjacent = new ArrayList<>();
        for (int j = 0; j < vertexCount; j++) {
            if (adjacencyMatrix.get(indexVertex).get(j)) {
                listAdjacent.add(vertices.get(j));
            }
        }
        return listAdjacent;
    }

    @Override
    public List<T> getVertices() {
        return new ArrayList<>(vertices);
    }
}
