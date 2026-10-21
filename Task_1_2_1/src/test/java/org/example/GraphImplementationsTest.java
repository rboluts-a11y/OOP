package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphImplementationsTest {

    @Test
    void emptyGraphsAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyMatrix, adjacencyList);

        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(incidenceMatrix, adjacencyList);

        assertEquals(adjacencyMatrix, incidenceMatrix);
        assertEquals(incidenceMatrix, adjacencyMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void SingleVertexGraphsAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyMatrix.addVertex("A");
        incidenceMatrix.addVertex("A");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void SingleVertexGraphsWithLoopAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyMatrix.addVertex("A");
        incidenceMatrix.addVertex("A");

        adjacencyList.addEdge("A", "A");
        adjacencyMatrix.addEdge("A", "A");
        incidenceMatrix.addEdge("A", "A");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void TwoVertexGraphsWithOneEdgeAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyList.addVertex("B");
        adjacencyList.addEdge("A", "B");

        adjacencyMatrix.addVertex("A");
        adjacencyMatrix.addVertex("B");
        adjacencyMatrix.addEdge("A", "B");

        incidenceMatrix.addVertex("A");
        incidenceMatrix.addVertex("B");
        incidenceMatrix.addEdge("A", "B");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void TwoVertexGraphsWithEdgesInBothDirectionsAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyList.addVertex("B");
        adjacencyList.addEdge("A", "B");
        adjacencyList.addEdge("B", "A");

        adjacencyMatrix.addVertex("A");
        adjacencyMatrix.addVertex("B");
        adjacencyMatrix.addEdge("A", "B");
        adjacencyMatrix.addEdge("B", "A");

        incidenceMatrix.addVertex("A");
        incidenceMatrix.addVertex("B");
        incidenceMatrix.addEdge("A", "B");
        incidenceMatrix.addEdge("B", "A");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void TriangleGraphsAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        for (Graph<String> graph : new Graph[]{adjacencyList, adjacencyMatrix, incidenceMatrix}) {
            graph.addVertex("A");
            graph.addVertex("B");
            graph.addVertex("C");

            graph.addEdge("A", "B");
            graph.addEdge("A", "C");
            graph.addEdge("B", "C");
        }

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }
}