package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;
import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int id;
    private List<List<Integer>> sacs;
    public TarjanSCC(Graph graph) {
        this(graph, new MetricsImpl());
    }
    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<List<Integer>> findSCCs() {
        int n = graph.getN();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sacs = new ArrayList<>();
        id = 0;

        Arrays.fill(ids, -1);

        metrics.startTimer();

        for (int u = 0; u < n; u++) {
            if (ids[u] == -1) {
                dfs(u);
            }
        }
        metrics.stopTimer();

        return sacs;
    }
    private void dfs(int u) {
        metrics.incrementCounter("dfs_visits");
        ids[u] = low[u] = id++;
        stack.push(u);
        onStack[u] = true;
        for (Graph.Edge edge : graph.getNeighbors(u)) {
            int v = edge.to();
            metrics.incrementCounter("edges_explored");

            if (ids[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], ids[v]);
            }
        }
        if (ids[u] == low[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                scc.add(v);
                metrics.incrementCounter("stack_pops");
            } while (v != u);

            sacs.add(scc);
        }
    }
    public Metrics getMetrics() {
        return metrics;
    }
    public static void printSCCs(List<List<Integer>> sccs) {
        System.out.println("\n=== Strongly Connected Components ===");
        System.out.println("Total SCCs: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> scc = sccs.get(i);
            System.out.println("SCC " + i + " (size " + scc.size() + "): " + scc);
        }
    }
}