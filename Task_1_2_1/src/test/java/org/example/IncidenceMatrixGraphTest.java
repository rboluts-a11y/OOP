package org.example;

class IncidenceMatrixGraphTest extends AbstractGraphTest {
    @Override
    public <T> Graph<T> createGraph(Class<T> type) {
        return new IncidenceMatrixGraph<>(type);
    }
}
