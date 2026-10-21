package org.example;

import java.util.*;

public class AdjacencyListGraph<T> extends AbstractGraph<T>  {
    private final Map<T, List<T>> adjacencyList;
    public AdjacencyListGraph(Class<T> vertexType) {
        super(vertexType);
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(T v) throws IllegalArgumentException {
        if (adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException();
        }
        adjacencyList.put(v, new ArrayList<>());
    }

    @Override
    public void removeVertex(T v) throws NoSuchElementException {
        if (!adjacencyList.containsKey(v)) {
            throw new NoSuchElementException();
        }
        for (T from: adjacencyList.keySet()) {
            adjacencyList.get(from).remove(v);
        }
        adjacencyList.remove(v);
    }

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

    @Override
    public List<T> getOutgoingVertices(T v) throws NoSuchElementException {
        if (!adjacencyList.containsKey(v)) {
            throw new NoSuchElementException();
        }
        return new ArrayList<>(adjacencyList.get(v));
    }

    @Override
    public List<T> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }
}
