package com.smartcity.common;

import java.util.*;
public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final String weightModel; // "edge" or "node"

    public record Edge(int to, int weight) {

        @Override
            public String toString() {
                return "(" + to + ", w=" + weight + ")";
            }
        }

    public Graph(int n, boolean directed, String weightModel) {
        this.n = n;
        this.directed = directed;
        this.weightModel = weightModel;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new Edge(v, weight));
        if (!directed) {
            adj.get(v).add(new Edge(u, weight));
        }
    }

    public List<Edge> getNeighbors(int u) {
        return adj.get(u);
    }

    public int getN() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public Graph reverse() {
        Graph rev = new Graph(n, directed, weightModel);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                rev.addEdge(e.to, u, e.weight);
            }
        }
        return rev;
    }

    public List<int[]> getAllEdges() {
        List<int[]> edges = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                edges.add(new int[]{u, e.to, e.weight});
            }
        }
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{n=").append(n)
                .append(", directed=").append(directed)
                .append(", weightModel=").append(weightModel)
                .append("}\n");
        for (int u = 0; u < n; u++) {
            sb.append(u).append(": ").append(adj.get(u)).append("\n");
        }
        return sb.toString();
    }
}