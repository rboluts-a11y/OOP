package org.example;

class AdjacencyListGraphTest extends AbstractGraphTest {
    @Override
    public <T> Graph<T> createGraph(Class<T> type) {
        return new AdjacencyListGraph<>(type);
    }
}