package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public abstract class AbstractGraphTest {
    public abstract <T> Graph<T> createGraph(Class<T> type);

    @Test
    void addVertexAddsNewVertex() {
        Graph<Integer> graph = createGraph(Integer.class);

        graph.addVertex(1);

        assertEquals(List.of(1), graph.getVertices());
        assertEquals(List.of(), graph.getOutgoingVertices(1));
    }

    @Test
    void addVertexThrowsIfVertexAlreadyExists() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);

        assertThrows(IllegalArgumentException.class,
                () -> graph.addVertex(1));

        assertEquals(List.of(1), graph.getVertices());
    }

    @Test
    void removeVertexRemovesExistingVertex() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);

        graph.removeVertex(1);

        assertEquals(List.of(2), graph.getVertices());
    }

    @Test
    void removeVertexRemovesAllIncidentEdges() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);
        graph.addEdge(3, 1);
        graph.addEdge(2, 3);

        graph.removeVertex(1);

        assertEquals(List.of(3), graph.getOutgoingVertices(2));
        assertEquals(List.of(), graph.getOutgoingVertices(3));
    }

    @Test
    void removeVertexThrowsIfVertexDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);

        assertThrows(NoSuchElementException.class,
                () -> graph.removeVertex(2));

        assertEquals(List.of(1), graph.getVertices());
    }

    @Test
    void addEdgeAddsDirectedEdge() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);

        graph.addEdge(1, 2);

        assertEquals(List.of(2), graph.getOutgoingVertices(1));
        assertEquals(List.of(), graph.getOutgoingVertices(2));
    }

    @Test
    void addEdgeAllowsReverseEdge() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        graph.addEdge(2, 1);

        assertEquals(List.of(2), graph.getOutgoingVertices(1));
        assertEquals(List.of(1), graph.getOutgoingVertices(2));
    }

    @Test
    void addEdgeAllowsSelfLoop() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);

        graph.addEdge(1, 1);

        assertEquals(List.of(1), graph.getOutgoingVertices(1));
    }

    @Test
    void addEdgeThrowsIfSourceDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(2);

        assertThrows(NoSuchElementException.class,
                () -> graph.addEdge(1, 2));
    }

    @Test
    void addEdgeThrowsIfDestinationDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);

        assertThrows(NoSuchElementException.class,
                () -> graph.addEdge(1, 2));
    }

    @Test
    void addEdgeThrowsIfBothVerticesDoNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertThrows(NoSuchElementException.class,
                () -> graph.addEdge(1, 2));
    }

    @Test
    void addEdgeThrowsIfEdgeAlreadyExists() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertThrows(IllegalArgumentException.class,
                () -> graph.addEdge(1, 2));

        assertEquals(List.of(2), graph.getOutgoingVertices(1));
    }

    @Test
    void removeEdgeRemovesExistingEdge() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        graph.removeEdge(1, 2);

        assertEquals(List.of(), graph.getOutgoingVertices(1));
    }

    @Test
    void removeEdgeDoesNotRemoveReverseEdge() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);

        graph.removeEdge(1, 2);

        assertEquals(List.of(), graph.getOutgoingVertices(1));
        assertEquals(List.of(1), graph.getOutgoingVertices(2));
    }

    @Test
    void removeEdgeThrowsIfSourceDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(2);

        assertThrows(NoSuchElementException.class,
                () -> graph.removeEdge(1, 2));
    }

    @Test
    void removeEdgeThrowsIfDestinationDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);

        assertThrows(NoSuchElementException.class,
                () -> graph.removeEdge(1, 2));
    }

    @Test
    void removeEdgeThrowsIfBothVerticesDoNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertThrows(NoSuchElementException.class,
                () -> graph.removeEdge(1, 2));
    }

    @Test
    void removeEdgeThrowsIfEdgeDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);

        assertThrows(NoSuchElementException.class,
                () -> graph.removeEdge(1, 2));
    }

    @Test
    void getOutgoingVerticesReturnsAllOutgoingNeighbors() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        assertEquals(List.of(2, 3), graph.getOutgoingVertices(1));
    }

    @Test
    void getOutgoingVerticesReturnsEmptyListIfNoOutgoingEdges() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertEquals(List.of(), graph.getOutgoingVertices(2));
    }

    @Test
    void getOutgoingVerticesThrowsIfVertexDoesNotExist() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertThrows(NoSuchElementException.class,
                () -> graph.getOutgoingVertices(1));
    }

    @Test
    void getOutgoingVerticesReturnsIndependentList() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        List<Integer> outgoing = graph.getOutgoingVertices(1);
        outgoing.clear();

        assertEquals(List.of(2), graph.getOutgoingVertices(1));
    }

    @Test
    void getVerticesReturnsAllVertices() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        assertEquals(3, graph.getVertices().size());
        assertTrue(graph.getVertices().containsAll(List.of(1, 2, 3)));
    }

    @Test
    void getVerticesReturnsEmptyListForEmptyGraph() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertEquals(List.of(), graph.getVertices());
    }

    @Test
    void getVerticesReturnsIndependentList() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);

        List<Integer> vertices = graph.getVertices();
        vertices.clear();

        assertEquals(2, graph.getVertices().size());
    }

    @Test
    void equalsReturnsTrueForSameGraph() {
        Graph<Integer> graph = createGraph(Integer.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertEquals(graph, graph);
    }

    @Test
    void equalsReturnsTrueForEqualGraphs() {
        Graph<Integer> first = createGraph(Integer.class);
        first.addVertex(1);
        first.addVertex(2);
        first.addVertex(3);
        first.addEdge(1, 2);
        first.addEdge(1, 3);

        Graph<Integer> second = createGraph(Integer.class);
        second.addVertex(3);
        second.addVertex(2);
        second.addVertex(1);
        second.addEdge(1, 3);
        second.addEdge(1, 2);

        assertEquals(first, second);
    }

    @Test
    void equalsReturnsFalseIfVertexSetsDiffer() {
        Graph<Integer> first = createGraph(Integer.class);
        first.addVertex(1);
        first.addVertex(2);

        Graph<Integer> second = createGraph(Integer.class);
        second.addVertex(1);
        second.addVertex(3);

        assertNotEquals(first, second);
    }

    @Test
    void equalsReturnsFalseIfEdgeSetsDiffer() {
        Graph<Integer> first = createGraph(Integer.class);
        first.addVertex(1);
        first.addVertex(2);
        first.addEdge(1, 2);

        Graph<Integer> second = createGraph(Integer.class);
        second.addVertex(1);
        second.addVertex(2);
        second.addEdge(2, 1);

        assertNotEquals(first, second);
    }

    @Test
    void equalsReturnsFalseIfOnlyOneGraphHasAnEdge() {
        Graph<Integer> first = createGraph(Integer.class);
        first.addVertex(1);
        first.addVertex(2);

        Graph<Integer> second = createGraph(Integer.class);
        second.addVertex(1);
        second.addVertex(2);
        second.addEdge(1, 2);

        assertNotEquals(first, second);
    }

    @Test
    void equalsReturnsFalseIfVertexTypesDiffer() {
        Graph<Integer> integers = createGraph(Integer.class);
        integers.addVertex(1);

        Graph<String> strings = createGraph(String.class);
        strings.addVertex("1");

        assertNotEquals(integers, strings);
    }

    @Test
    void equalsReturnsFalseForNull() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertNotEquals(null, graph);
    }

    @Test
    void equalsReturnsFalseForObjectOfAnotherClass() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertNotEquals("not a graph", graph);
    }

    // ---------- hashCode ----------

    @Test
    void equalGraphsHaveEqualHashCodes() {
        Graph<Integer> first = createGraph(Integer.class);
        first.addVertex(1);
        first.addVertex(2);
        first.addEdge(1, 2);

        Graph<Integer> second = createGraph(Integer.class);
        second.addVertex(2);
        second.addVertex(1);
        second.addEdge(1, 2);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void toStringReturnsEmptyStringForEmptyGraph() {
        Graph<Integer> graph = createGraph(Integer.class);

        assertEquals("", graph.toString());
    }

    @Test
    void toStringReturnsRepresentationOfSingleVertexWithNoEdges() {
        Graph<String> graph = createGraph(String.class);
        graph.addVertex("A");

        assertEquals("A -> ()", graph.toString());
    }

    @Test
    void toStringReturnsRepresentationOfSingleVertexWithSelfLoop() {
        Graph<String> graph = createGraph(String.class);
        graph.addVertex("A");
        graph.addEdge("A", "A");

        assertEquals("A -> (A)", graph.toString());
    }

    @Test
    void toStringReturnsRepresentationOfGraphWithMultipleVerticesAndEdges() {
        Graph<String> graph = createGraph(String.class);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");

        assertEquals(
                "A -> (B, C); B -> (C); C -> ()",
                graph.toString()
        );
    }
}
