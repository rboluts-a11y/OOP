package org.example;

import java.util.*;

public abstract class AbstractGraph<T> implements Graph<T> {
    private final Class<T> vertexType;
    protected AbstractGraph(Class<T> vertexType) {
        this.vertexType = vertexType;
    }
    private record Edge<T>(T from, T to) {}
    private static <T> Set<Edge<T>> getEdges(Graph<T> graph) {
        Set<Edge<T>> edges = new HashSet<>();
        for (T from : graph.getVertices()) {
            List<T> outgoing = graph.getOutgoingVertices(from);
            for (T to : outgoing) {
                edges.add(new Edge<>(from, to));
            }
        }
        return edges;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractGraph<?> other)) {
            return false;
        }
        if (!vertexType.equals(other.vertexType)) {
            return false;
        }
        Set<T> verticesThis = new HashSet<>(this.getVertices());
        Set<?> verticesOther = new HashSet<>(other.getVertices());
        if (!verticesThis.equals(verticesOther)) {
            return false;
        }
        return getEdges(this).equals(getEdges(other));
    }
    @Override
    public String toString() {
        List<T> allVertices = this.getVertices();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allVertices.size(); i++) {
            T vertex = allVertices.get(i);
            List<T> allOutgoingFromThis = this.getOutgoingVertices(vertex);
            sb.append(vertex).append(" -> (");
            for (int j = 0; j < allOutgoingFromThis.size(); j++) {
                sb.append(allOutgoingFromThis.get(j));
                if (j + 1 != allOutgoingFromThis.size()) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            if (i + 1 != allVertices.size()) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }
    @Override
    public int hashCode() {
        int result = vertexType.hashCode();
        result = 31 * result + new HashSet<>(getVertices()).hashCode();
        result = 31 * result + getEdges(this).hashCode();
        return result;
    }
}
