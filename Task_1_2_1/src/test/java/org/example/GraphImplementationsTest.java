package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
    void singleVertexGraphsAreEqual() {
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
    void singleVertexGraphsWithLoopAreEqual() {
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
    void twoVertexGraphsWithOneEdgeAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyMatrix.addVertex("A");
        incidenceMatrix.addVertex("A");

        adjacencyList.addVertex("B");
        adjacencyMatrix.addVertex("B");
        incidenceMatrix.addVertex("B");

        adjacencyList.addEdge("A", "B");
        adjacencyMatrix.addEdge("A", "B");
        incidenceMatrix.addEdge("A", "B");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void twoVertexGraphsWithEdgesInBothDirectionsAreEqual() {
        Graph<String> adjacencyList = new AdjacencyListGraph<>(String.class);
        Graph<String> adjacencyMatrix = new AdjacencyMatrixGraph<>(String.class);
        Graph<String> incidenceMatrix = new IncidenceMatrixGraph<>(String.class);

        adjacencyList.addVertex("A");
        adjacencyMatrix.addVertex("A");
        incidenceMatrix.addVertex("A");

        adjacencyList.addVertex("B");
        adjacencyMatrix.addVertex("B");
        incidenceMatrix.addVertex("B");

        adjacencyList.addEdge("A", "B");
        adjacencyMatrix.addEdge("A", "B");
        incidenceMatrix.addEdge("A", "B");

        adjacencyList.addEdge("B", "A");
        adjacencyMatrix.addEdge("B", "A");
        incidenceMatrix.addEdge("B", "A");

        assertEquals(adjacencyList, adjacencyMatrix);
        assertEquals(adjacencyList, incidenceMatrix);
        assertEquals(adjacencyMatrix, incidenceMatrix);

        assertEquals(adjacencyList.hashCode(), adjacencyMatrix.hashCode());
        assertEquals(adjacencyList.hashCode(), incidenceMatrix.hashCode());
    }

    @Test
    void triangleGraphsAreEqual() {
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