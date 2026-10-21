package org.example;

import java.util.List;
import java.util.NoSuchElementException;

public interface Graph<T> {
    void addVertex(T v) throws IllegalArgumentException;
    void removeVertex(T v) throws NoSuchElementException;
    void addEdge(T from, T to) throws NoSuchElementException, IllegalArgumentException;
    void removeEdge(T from, T to) throws NoSuchElementException;
    List<T> getOutgoingVertices(T v) throws NoSuchElementException;
    List<T> getVertices();
}
