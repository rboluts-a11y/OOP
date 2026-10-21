package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class GraphReaderTest {

    private static final String TRIANGLE_GRAPH = """
            3
            A B C
            3
            A B
            A C
            B C
            """;

    private static final String EMPTY_GRAPH = """
            0
            0
            """;

    private static final String SINGLE_VERTEX = """
            1
            A
            0
            """;

    private static final String SELF_LOOP = """
            1
            A
            1
            A A
            """;

    private static final String TWO_WAY = """
            2
            A B
            2
            A B
            B A
            """;

    private static <T> Graph<T> read(
            String content,
            Class<T> vertexType,
            Function<String, T> parser,
            Function<Class<T>, Graph<T>> graphFactory
    ) {
        return GraphReader.read(
                new Scanner(content),
                vertexType,
                parser,
                graphFactory
        );
    }

    @Test
    void readsEmptyGraph() {
        Graph<String> graph = read(
                EMPTY_GRAPH,
                String.class,
                s -> s,
                AdjacencyListGraph::new
        );

        assertEquals(
                new AdjacencyListGraph<>(String.class),
                graph
        );
    }

    @Test
    void readsSingleVertex() {
        Graph<String> graph = read(
                SINGLE_VERTEX,
                String.class,
                s -> s,
                AdjacencyListGraph::new
        );

        Graph<String> expected = new AdjacencyListGraph<>(String.class);
        expected.addVertex("A");

        assertEquals(expected, graph);
    }

    @Test
    void readsGraphWithTriangleUsingAllImplementations() {
        Graph<String> adjacencyList = read(
                TRIANGLE_GRAPH,
                String.class,
                s -> s,
                AdjacencyListGraph::new
        );

        Graph<String> adjacencyMatrix = read(
                TRIANGLE_GRAPH,
                String.class,
                s -> s,
                AdjacencyMatrixGraph::new
        );

        Graph<String> incidenceMatrix = read(
                TRIANGLE_GRAPH,
                String.class,
                s -> s,
                IncidenceMatrixGraph::new
        );

        Graph<String> expected = new AdjacencyListGraph<>(String.class);
        expected.addVertex("A");
        expected.addVertex("B");
        expected.addVertex("C");
        expected.addEdge("A", "B");
        expected.addEdge("A", "C");
        expected.addEdge("B", "C");

        assertEquals(expected, adjacencyList);
        assertEquals(expected, adjacencyMatrix);
        assertEquals(expected, incidenceMatrix);
    }

    @Test
    void readsTwoWayGraph() {
        Graph<String> graph = read(
                TWO_WAY,
                String.class,
                s -> s,
                AdjacencyMatrixGraph::new
        );

        Graph<String> expected = new AdjacencyListGraph<>(String.class);
        expected.addVertex("A");
        expected.addVertex("B");
        expected.addEdge("A", "B");
        expected.addEdge("B", "A");

        assertEquals(expected, graph);
    }

    @Test
    void readsGraphWithSelfLoop() {
        Graph<String> graph = read(
                SELF_LOOP,
                String.class,
                s -> s,
                IncidenceMatrixGraph::new
        );

        Graph<String> expected = new AdjacencyListGraph<>(String.class);
        expected.addVertex("A");
        expected.addEdge("A", "A");

        assertEquals(expected, graph);
    }

    @Test
    void readsIntegerVertices() {
        String content = """
                3
                10 20 30
                2
                10 20
                20 30
                """;

        Graph<Integer> graph = read(
                content,
                Integer.class,
                Integer::parseInt,
                AdjacencyListGraph::new
        );

        Graph<Integer> expected = new AdjacencyListGraph<>(Integer.class);
        expected.addVertex(10);
        expected.addVertex(20);
        expected.addVertex(30);
        expected.addEdge(10, 20);
        expected.addEdge(20, 30);

        assertEquals(expected, graph);
    }

    @Test
    void rejectsMissingVertexCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        "",
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsNonNumericVertexCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        abc
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsNegativeVertexCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        -1
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsMissingVertices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        3
                        A B
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsNonNumericEdgeCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        1
                        A
                        abc
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsNegativeEdgeCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        1
                        A
                        -1
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsMissingEdgeVertices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        2
                        A B
                        1
                        A
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsUnparseableVertex() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        2
                        10 abc
                        0
                        """,
                        Integer.class,
                        Integer::parseInt,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void rejectsExtraData() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        1
                        A
                        0
                        EXTRA
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void graphRejectsDuplicateVertices() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        2
                        A A
                        0
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void graphRejectsDuplicateEdges() {
        assertThrows(
                IllegalArgumentException.class,
                () -> read(
                        """
                        2
                        A B
                        2
                        A B
                        A B
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }

    @Test
    void graphRejectsEdgeWithUnknownVertex() {
        assertThrows(
                NoSuchElementException.class,
                () -> read(
                        """
                        2
                        A B
                        1
                        A C
                        """,
                        String.class,
                        s -> s,
                        AdjacencyListGraph::new
                )
        );
    }
}