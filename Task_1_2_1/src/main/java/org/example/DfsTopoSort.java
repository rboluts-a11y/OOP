package org.example;

import java.util.*;

public class DfsTopoSort {
    private DfsTopoSort() {
    }

    private enum Color {
        WHITE(0),
        GRAY(1),
        BLACK(2);

        private final int value;

        Color(int value) {
            this.value = value;
        }
    }

    private static <T> void dfs(Graph<T> graph, T curVertex,
                                Map<T, Color> vertexColors, List<T> orderVertices) {
        vertexColors.put(curVertex, Color.GRAY);
        List<T> adjacentVertices = graph.getOutgoingVertices(curVertex);
        for (T adjacentVertex : adjacentVertices) {
            if (vertexColors.get(adjacentVertex) == Color.GRAY) {
                throw new IllegalStateException();
            } else if (vertexColors.get(adjacentVertex) == Color.WHITE) {
                dfs(graph, adjacentVertex, vertexColors, orderVertices);
            }
        }
        vertexColors.put(curVertex, Color.BLACK);
        orderVertices.add(curVertex);
    }

    public static <T> List<T> topoSort(Graph<T> graph) {
        Objects.requireNonNull(graph);
        Map<T, Color> vertexColors = new HashMap<>();
        List<T> allVertices = graph.getVertices();
        for (T vertex : allVertices) {
            vertexColors.put(vertex, Color.WHITE);
        }
        List<T> orderVertices = new ArrayList<>();
        for (T vertex : allVertices) {
            if (vertexColors.get(vertex) == Color.WHITE) {
                dfs(graph, vertex, vertexColors, orderVertices);
            }
        }
        Collections.reverse(orderVertices);
        return orderVertices;
    }
}
