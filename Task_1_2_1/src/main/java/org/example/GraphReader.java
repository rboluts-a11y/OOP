
package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public class GraphReader {

    private GraphReader() {
    }

    public static <T> Graph<T> read(
            Path path,
            Class<T> vertexType,
            Function<String, T> parser,
            Function<Class<T>, Graph<T>> graphFactory
    ) throws IOException {

        Objects.requireNonNull(path);
        Objects.requireNonNull(vertexType);
        Objects.requireNonNull(parser);
        Objects.requireNonNull(graphFactory);

        try (Scanner scanner = new Scanner(path)) {
            return read(scanner, vertexType, parser, graphFactory);
        }
    }

    static <T> Graph<T> read(
            Scanner scanner,
            Class<T> vertexType,
            Function<String, T> parser,
            Function<Class<T>, Graph<T>> graphFactory
    ) {
        Objects.requireNonNull(scanner);
        Objects.requireNonNull(vertexType);
        Objects.requireNonNull(parser);
        Objects.requireNonNull(graphFactory);

        Graph<T> graph = Objects.requireNonNull(
                graphFactory.apply(vertexType)
        );

        int vertexCount = readCount(scanner);

        for (int i = 0; i < vertexCount; i++) {
            String token = readToken(scanner);
            T vertex = parseVertex(token, parser);
            graph.addVertex(vertex);
        }

        int edgeCount = readCount(scanner);

        for (int i = 0; i < edgeCount; i++) {
            String fromToken = readToken(scanner);
            String toToken = readToken(scanner);

            T from = parseVertex(fromToken, parser);
            T to = parseVertex(toToken, parser);

            graph.addEdge(from, to);
        }

        if (scanner.hasNext()) {
            throw new IllegalArgumentException();
        }

        return graph;
    }

    private static int readCount(Scanner scanner) {
        String token = readToken(scanner);
        final int count;

        try {
            count = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if (count < 0) {
            throw new IllegalArgumentException();
        }

        return count;
    }

    private static String readToken(Scanner scanner) {
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException();
        }

        return scanner.next();
    }

    private static <T> T parseVertex(
            String token,
            Function<String, T> parser
    ) {
        try {
            return Objects.requireNonNull(parser.apply(token));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException();
        }
    }
}
