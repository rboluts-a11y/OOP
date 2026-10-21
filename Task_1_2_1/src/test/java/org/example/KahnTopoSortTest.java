package org.example;

import java.util.List;

class KahnTopoSortTest extends AbstractTopoSortTest {
    @Override
    public <T> List<T> topoSort(Graph<T> graph) {
        return KahnTopoSort.topoSort(graph);
    }
}
