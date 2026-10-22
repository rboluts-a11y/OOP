package org.example;

class AdjacencyMatrixGraphTest extends AbstractGraphTest {
    @Override
    public <T> Graph<T> createGraph(Class<T> type) {
        return new AdjacencyMatrixGraph<>(type);
    }
}
