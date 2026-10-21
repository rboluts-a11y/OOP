package org.example;

import java.util.List;

class DfsTopoSortTest extends AbstractTopoSortTest {
    @Override
    public <T> List<T> topoSort(Graph<T> graph) {
        return DfsTopoSort.topoSort(graph);
    }
}
