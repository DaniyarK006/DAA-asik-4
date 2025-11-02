package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import java.util.*;

public class ComponentTopologicalSort {

    private static Graph condensation;
    private static List<List<Integer>> sacs;

    public record TopoResult(List<Integer> componentOrder, List<Integer> taskOrder) {
    }

    public static TopoResult sortWithTasks(Graph condensation, List<List<Integer>> sacs) {
        ComponentTopologicalSort.condensation = condensation;
        ComponentTopologicalSort.sacs = sacs;
        KahnTopologicalSort kahnSort = new KahnTopologicalSort(condensation);
        List<Integer> componentOrder = kahnSort.sort();

        if (componentOrder == null) {
            return null;
        }

        List<Integer> taskOrder = new ArrayList<>();
        for (int compId : componentOrder) {
            List<Integer> scc = sacs.get(compId);
            taskOrder.addAll(scc);
        }

        return new TopoResult(componentOrder, taskOrder);
    }

    public static void printTopoOrders(TopoResult result, List<List<Integer>> sacs) {
        System.out.println("\n=== Topological Orders ===");
        System.out.println("\nComponent Order:");
        for (int i = 0; i < result.componentOrder.size(); i++) {
            int compId = result.componentOrder.get(i);
            System.out.println("  " + i + ". Component " + compId + ": " + sacs.get(compId));
        }

        System.out.println("\nDerived Task Order:");
        System.out.println("  " + result.taskOrder);
    }
}