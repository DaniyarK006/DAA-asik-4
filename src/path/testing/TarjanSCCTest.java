package path.testing;

import com.smartcity.common.Graph;
import com.smartcity.graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class TarjanSCCTest {

    @org.junit.Test
    @Test
    public void testSingleVertex() {
        var graph = new Graph(1, true, "edge");
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(1, sacs.size());
        assertEquals(1, sacs.getFirst().size());
        assertTrue(sacs.getFirst().contains(0));
    }

    @Test
    public void testSimpleCycle() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(1, sacs.size());
        assertEquals(3, sacs.getFirst().size());
    }

    @Test
    public void testMultipleSCCs() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(2, sacs.size());
        for (List<Integer> scc : sacs) {
            assertEquals(2, scc.size());
        }
    }

    @Test
    public void testDAG() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(0, 2, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(3, sacs.size());
        for (List<Integer> scc : sacs) {
            assertEquals(1, scc.size());
        }
    }

    @Test
    public void testMixedStructure() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 1, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(2, sacs.size());


        List<Integer> cycleSCC = sacs.stream()
                .filter(scc -> scc.size() == 3)
                .findFirst()
                .orElse(null);

        assertNotNull(cycleSCC);
        assertTrue(cycleSCC.contains(1));
        assertTrue(cycleSCC.contains(2));
        assertTrue(cycleSCC.contains(3));
    }

    @Test
    public void testDisconnectedGraph() {
        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sacs = tarjan.findSCCs();

        assertEquals(4, sacs.size());
    }
}