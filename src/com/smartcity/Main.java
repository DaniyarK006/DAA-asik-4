package com.smartcity;

import com.smartcity.common.*;
import com.smartcity.graph.scc.*;
import com.smartcity.graph.topo.*;
import org.json.JSONArray;
import org.json.JSONObject;
import path.testing.DAGShortestPath;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║     Smart City/Campus Task Scheduling System       ║");
        System.out.println("╚════════════════════════════════════════════════════╝");

        try {
            runCompleteAnalysis();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void runCompleteAnalysis() throws Exception {
        System.out.println("\n Loading graph from: resources/tasks.json");

        InputStream is = Main.class.getResourceAsStream("/tasks.json");
        if (is == null) {
            throw new FileNotFoundException("tasks.json not found in resources folder!");
        }

        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        boolean directed = json.optBoolean("directed", false);
        int n = json.getInt("n");
        String weightModel = json.optString("weight_model", "edge");
        int source = json.optInt("source", 0);

        Graph graph = new Graph(n, directed, weightModel);
        JSONArray edges = json.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject edge = edges.getJSONObject(i);
            int u = edge.getInt("u");
            int v = edge.getInt("v");
            int w = edge.optInt("w", 1);
            graph.addEdge(u, v, w);
        }

        System.out.println(" Graph loaded successfully!");
        System.out.println("  Vertices: " + graph.getN());
        System.out.println("  Edges: " + graph.getAllEdges().size());
        System.out.println("  Directed: " + graph.isDirected());
        System.out.println("  Weight Model: " + graph.getWeightModel());
        System.out.println("  Source Vertex: " + source);

        //  Strongly Connected Components
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Finding Strongly Connected Components (Tarjan)");
        System.out.println("=".repeat(60));

        Metrics sccMetrics = new MetricsImpl();
        TarjanSCC tarjan = new TarjanSCC(graph, sccMetrics);
        List<List<Integer>> sccs = tarjan.findSCCs();

        TarjanSCC.printSCCs(sccs);
        System.out.println("\n" + sccMetrics.getSummary());

        //  Condensation Graph
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Building Condensation Graph (DAG)");
        System.out.println("=".repeat(60));

        CondensationGraph condensation = new CondensationGraph(graph, sccs);
        condensation.printInfo();
        Graph dag = condensation.getCondensation();

        //  Topological Sort
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Topological Ordering");
        System.out.println("=".repeat(60));

        ComponentTopologicalSort.TopoResult topoResult =
                ComponentTopologicalSort.sortWithTasks(dag, sccs);

        if (topoResult != null) {
            ComponentTopologicalSort.printTopoOrders(topoResult, sccs);
        } else {
            System.out.println("Warning: Condensation graph has a cycle (unexpected!)");
        }

        //  Shortest Paths
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Shortest Paths in DAG");
        System.out.println("=".repeat(60));

        int dagSource = condensation.getComponent(source);
        System.out.println("Original source vertex " + source + " maps to component " + dagSource);

        Metrics spMetrics = new MetricsImpl();
        DAGShortestPath dagSP = new DAGShortestPath(dag, spMetrics);
        DAGShortestPath.PathResult shortestPaths = dagSP.shortestPaths(dagSource);
        shortestPaths.printShortestPaths();
        System.out.println("\n" + spMetrics.getSummary());

        //  Longest (Critical) Path
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Longest Path (Critical Path) in DAG");
        System.out.println("=".repeat(60));

        Metrics lpMetrics = new MetricsImpl();
        DAGShortestPath dagLP = new DAGShortestPath(dag, lpMetrics);
        DAGShortestPath.PathResult longestPaths = dagLP.longestPaths(dagSource);

        DAGShortestPath.CriticalPath criticalPath = longestPaths.findCriticalPath();
        if (criticalPath != null) {
            criticalPath.print();
        } else {
            System.out.println("No critical path found");
        }

        System.out.println("\n" + lpMetrics.getSummary());
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Analysis Complete");
        System.out.println("=".repeat(60));
        System.out.println(" SCCs detected and compressed");
        System.out.println(" Condensation DAG created");
        System.out.println(" Topological order computed");
        System.out.println(" Shortest paths calculated");
        System.out.println(" Critical path identified");
    }
}
