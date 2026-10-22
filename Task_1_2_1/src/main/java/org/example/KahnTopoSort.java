package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Класс, который предоставляет реализацию топологической сортировки графа.
 * Использует алгоритм Кана.
 * Пока вершины еще есть в графе, находим любую вершину, в которое не ведет ни одно ребро.
 * Затем добавляем ее в список, удаляем из графа и все связанные с ней ребра.
 * Если в какой-то момент остались вершины, но нет такой, что в нее ни входит ни одно ребро,
 * то граф имеет цикл, и выбрасывается исключение IllegalStateException.
 */
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
