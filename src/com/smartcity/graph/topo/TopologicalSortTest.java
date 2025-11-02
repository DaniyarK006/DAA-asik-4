package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    public void testKahnSimpleDAG() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    public void testDFSSimpleDAG() {
        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        DFSTopologicalSort dfs = new DFSTopologicalSort(graph);
        List<Integer> order = dfs.sort();

        assertNotNull(order);
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    public void testCycleDetection() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        assertNull(kahn.sort());

        DFSTopologicalSort dfs = new DFSTopologicalSort(graph);
        assertNull(dfs.sort());
    }

    @Test
    public void testComplexDAG() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true, "edge");

        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(1, order.size());
        assertEquals(0, order.getFirst());
    }

    @Test
    public void testDisconnectedDAG() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        List<Integer> order = kahn.sort();

        assertNotNull(order);
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }
}
