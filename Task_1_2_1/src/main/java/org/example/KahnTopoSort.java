package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KahnTopoSort {
    private KahnTopoSort() {
    }

    public static <T> List<T> topoSort(Graph<T> graph) {
        Objects.requireNonNull(graph);
        List<T> vertices = graph.getVertices();
        Map<T, Integer> degreeIn = new HashMap<>();
        for (T vertex : vertices) {
            degreeIn.put(vertex, 0);
        }
        for (T vertex : vertices) {
            for (T adjacentVertex: graph.getOutgoingVertices(vertex)) {
                int oldValue = degreeIn.get(adjacentVertex);
                int newValue = oldValue + 1;
                degreeIn.put(adjacentVertex, newValue);
            }
        }

        List<T> zeroInDegree = new ArrayList<>();
        List<T> orderVertices = new ArrayList<>();
        for (T vertex: vertices) {
            if (degreeIn.get(vertex) == 0) {
                zeroInDegree.add(vertex);
            }
        }
        while (!zeroInDegree.isEmpty()) {
            int zeroDegreeCount = zeroInDegree.size();
            T curVertex = zeroInDegree.get(zeroDegreeCount - 1);
            orderVertices.add(curVertex);
            zeroInDegree.remove(zeroDegreeCount - 1);
            for (T adjacentVertex: graph.getOutgoingVertices(curVertex)) {
                int oldValue = degreeIn.get(adjacentVertex);
                int newValue = oldValue - 1;
                degreeIn.put(adjacentVertex, newValue);
                if (newValue == 0) {
                    zeroInDegree.add(adjacentVertex);
                }
            }

        }
        if (orderVertices.size() != vertices.size()) {
            throw new IllegalStateException();
        } else {
            return orderVertices;
        }
    }
}
