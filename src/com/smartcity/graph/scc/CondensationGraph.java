package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import java.util.*;

public class CondensationGraph {
    private final Graph originalGraph;
    private final List<List<Integer>> sacs;
    private Graph condensation;
    private int[] componentMap;

    public CondensationGraph(Graph originalGraph, List<List<Integer>> sacs) {
        this.originalGraph = originalGraph;
        this.sacs = sacs;
        buildCondensation();
    }

    private void buildCondensation() {
        int numSCCs = sacs.size();
        componentMap = new int[originalGraph.getN()];

        for (int i = 0; i < sacs.size(); i++) {
            for (int v : sacs.get(i)) {
                componentMap[v] = i;
            }
        }

        condensation = new Graph(numSCCs, true, originalGraph.getWeightModel());
        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < originalGraph.getN(); u++) {
            int compU = componentMap[u];
            for (Graph.Edge edge : originalGraph.getNeighbors(u)) {
                int v = edge.to();
                int compV = componentMap[v];

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(compU, compV, edge.weight());
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
    }

    public Graph getCondensation() {
        return condensation;
    }

    public int[] getComponentMap() {
        return componentMap;
    }

    public int getComponent(int vertex) {
        return componentMap[vertex];
    }

    public void printInfo() {
        System.out.println("\n=== Condensation Graph ===");
        System.out.println("Original vertices: " + originalGraph.getN());
        System.out.println("Components: " + sacs.size());
        System.out.println("Original edges: " + originalGraph.getAllEdges().size());
        System.out.println("Condensation edges: " + condensation.getAllEdges().size());

        System.out.println("\nComponent details:");
        for (int i = 0; i < sacs.size(); i++) {
            System.out.println("Component " + i + ": " + sacs.get(i));
        }

        System.out.println("\nCondensation edges:");
        for (int u = 0; u < condensation.getN(); u++) {
            for (Graph.Edge e : condensation.getNeighbors(u)) {
                System.out.println("  " + u + " -> " + e.to() + " (weight: " + e.weight() + ")");
            }
        }
    }
}