package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;
import java.util.*;

public record KahnTopologicalSort(Graph graph, Metrics metrics) {
    public KahnTopologicalSort(Graph graph) {
        this(graph, new MetricsImpl());
    }

    public List<Integer> sort() {
        int n = graph.getN();
        int[] inDegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to()]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int u = 0; u < n; u++) {
            if (inDegree[u] == 0) {
                queue.offer(u);
                metrics.incrementCounter("queue_pushes");
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        metrics.startTimer();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementCounter("queue_pops");
            topoOrder.add(u);

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.to();
                inDegree[v]--;
                metrics.incrementCounter("edge_relaxations");

                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementCounter("queue_pushes");
                }
            }
        }

        metrics.stopTimer();

        if (topoOrder.size() != n) {
            return null;
        }

        return topoOrder;
    }
}