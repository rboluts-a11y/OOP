package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


abstract class AbstractTopoSortTest {

    public abstract <T> List<T> topoSort(Graph<T> graph);

    private Graph<String> createGraph(String... vertices) {
        Graph<String> graph = new AdjacencyListGraph<>(String.class);

        for (String vertex : vertices) {
            graph.addVertex(vertex);
        }

        return graph;
    }

    private void assertValidTopologicalOrder(Graph<String> graph, List<String> order) {
        List<String> vertices = graph.getVertices();
        assertEquals(vertices.size(), order.size());
        Set<String> expectedVertices = new HashSet<>(vertices);
        Set<String> actualVertices = new HashSet<>(order);
        assertEquals(expectedVertices, actualVertices);
        Map<String, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        for (String from : vertices) {
            for (String to : graph.getOutgoingVertices(from)) {
                assertTrue(position.get(from) < position.get(to));
            }
        }
    }

    @Test
    void emptyGraph() {
        Graph<String> graph = createGraph();
        List<String> order = topoSort(graph);

        assertNotNull(order);
        assertTrue(order.isEmpty());
        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void singleVertex() {
        Graph<String> graph = createGraph("A");
        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void isolatedVertices() {
        Graph<String> graph = createGraph("A", "B", "C");

        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void chain() {
        Graph<String> graph = createGraph("A", "B", "C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void independentChains() {
        Graph<String> graph = createGraph("A", "B", "C", "D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");

        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void branching() {
        Graph<String> graph = createGraph("A", "B", "C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");

        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void edgeIntoVertexWithoutOutgoingEdges() {
        Graph<String> graph = createGraph("A", "B");
        graph.addEdge("A", "B");

        assertTrue(graph.getOutgoingVertices("B").isEmpty());

        List<String> order = topoSort(graph);

        assertValidTopologicalOrder(graph, order);
    }

    @Test
    void twoVertexCycle() {
        Graph<String> graph = createGraph("A", "B");
        graph.addEdge("A", "B");
        graph.addEdge("B", "A");

        assertThrows(IllegalStateException.class,
                () -> topoSort(graph));
    }

    @Test
    void threeVertexCycle() {
        Graph<String> graph = createGraph("A", "B", "C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        assertThrows(IllegalStateException.class,
                () -> topoSort(graph));
    }

    @Test
    void cycleWithVertexOutsideCycle() {
        Graph<String> graph = createGraph("A", "B", "C", "D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "B");

        assertThrows(IllegalStateException.class,
                () -> topoSort(graph));
    }

    @Test
    void selfLoop() {
        Graph<String> graph = createGraph("A");
        graph.addEdge("A", "A");

        assertThrows(IllegalStateException.class,
                () -> topoSort(graph));
    }

    @Test
    void nullGraph() {
        assertThrows(NullPointerException.class,
                () -> topoSort(null));
    }
}
