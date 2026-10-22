package org.example;

import java.util.List;
import java.util.Scanner;

/**
 * Главный класс Main, который нужен для взаимодействия с пользователем.
 * Спрашивает у пользователя вершины и ребра, желаемую реализацию.
 * Программа выводит toString() графа.
 * Также если граф содержит цикл, выводится что есть цикл, иначе две топологические сортировки.
 */
public class Main {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true,
                java.nio.charset.StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Добро пожаловать в строитель графов!");
        System.out.println("Выберите, какую реализацию графа Вы хотите использовать:");
        System.out.println("Введите цифру от 1 до 3:");
        System.out.println("1) Список смежности");
        System.out.println("2) Матрица смежности");
        System.out.println("3) Матрица инцидентности");
        Graph<String> graph;
        int variant = scanner.nextInt();
        if (variant == 1) {
            graph = new AdjacencyListGraph<>(String.class);
        } else if (variant == 2) {
            graph = new AdjacencyMatrixGraph<>(String.class);
        } else {
            graph = new IncidenceMatrixGraph<>(String.class);
        }
        System.out.println("\nВведите количество вершин в графе:");
        int vertexCount = scanner.nextInt();
        System.out.println("Введите строковые значения всех вершин:");
        for (int i = 0; i < vertexCount; i++) {
            String vertex = scanner.next();
            graph.addVertex(vertex);
        }
        System.out.println("\nВведите количество ребер в графе:");
        int edgeCount = scanner.nextInt();
        System.out.println("Для каждого ребра введите две вершины, "
                + "между которыми провести ребро (вначале откуда, затем куда):");
        for (int i = 0; i < edgeCount; i++) {
            String vertexFrom = scanner.next();
            String vertexTo = scanner.next();
            graph.addEdge(vertexFrom, vertexTo);
        }
        System.out.println("Строковое представление графа: " + graph);
        try {
            List<String> dfsResult = DfsTopoSort.topoSort(graph);
            List<String> kahnResult = KahnTopoSort.topoSort(graph);
            System.out.print("Результат топологической сортировки алгоритмом DFS: ");
            System.out.println(dfsResult);
            System.out.print("Результат топологической сортировки алгоритмом Кана: ");
            System.out.println(kahnResult);

        } catch (IllegalStateException e) {
            System.out.println("К сожалению, вершины графа невозможно топологически отсортировать"
                   + ", из-за того, что в графе есть цикл.");
        }
    }
}