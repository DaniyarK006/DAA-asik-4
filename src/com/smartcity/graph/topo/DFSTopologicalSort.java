package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;
import java.util.*;

public class DFSTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    private boolean[] visited;
    private boolean[] recStack;
    private Stack<Integer> stack;
    private boolean hasCycle;

    public DFSTopologicalSort(Graph graph) {
        this(graph, new MetricsImpl());
    }

    public DFSTopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<Integer> sort() {
        int n = graph.getN();
        visited = new boolean[n];
        recStack = new boolean[n];
        stack = new Stack<>();
        hasCycle = false;

        metrics.startTimer();

        for (int u= 0; u < n; u++)
            if (!visited[u]) {
                dfs(u);
                if (hasCycle) {
                    metrics.stopTimer();
                    return null;
                }
            }

        metrics.stopTimer();

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private void dfs(int u) {
        visited[u] = true;
        recStack[u] = true;
        metrics.incrementCounter("dfs_visits");

        for (Graph.Edge edge : graph.getNeighbors(u)) {
            int v = edge.to();
            metrics.incrementCounter("edges_explored");

            if (recStack[v]) {
                hasCycle = true;
                return;
            }

            if (!visited[v]) {
                dfs(v);
                if (hasCycle) return;
            }
        }

        recStack[u] = false;
        stack.push(u);
        metrics.incrementCounter("stack_pushes");
    }

    public Metrics getMetrics() {
        return metrics;
    }
}