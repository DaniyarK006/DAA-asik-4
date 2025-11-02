package path.testing;


import com.smartcity.common.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {

    @Test
    public void testShortestPathLinear() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPaths(0);

        assertEquals(0, result.getDistance(0));
        assertEquals(3, result.getDistance(1));
        assertEquals(5, result.getDistance(2));

        assertEquals(Arrays.asList(0, 1, 2), result.getPath(2));
    }

    @Test
    public void testShortestPathDiamond() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 4);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPaths(0);

        assertEquals(6, result.getDistance(3));
        assertEquals(Arrays.asList(0, 2, 3), result.getPath(3));
    }

    @Test
    public void testLongestPathLinear() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);

        DAGShortestPath dagLP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagLP.longestPaths(0);

        assertEquals(0, result.getDistance(0));
        assertEquals(3, result.getDistance(1));
        assertEquals(5, result.getDistance(2));

        DAGShortestPath.CriticalPath cp = result.findCriticalPath();
        assertNotNull(cp);
        assertEquals(5, cp.length());
        assertEquals(Arrays.asList(0, 1, 2), cp.path());
    }

    @Test
    public void testLongestPathDiamond() {

        Graph graph = new Graph(4, true, "edge");
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 4);

        DAGShortestPath dagLP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagLP.longestPaths(0);

        assertEquals(6, result.getDistance(3));
        assertEquals(Arrays.asList(0, 1, 3), result.getPath(3));

        DAGShortestPath.CriticalPath cp = result.findCriticalPath();
        assertNotNull(cp);
        assertEquals(6, cp.length());
    }

    @Test
    public void testUnreachableVertex() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 5);

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPaths(0);

        assertEquals(Integer.MAX_VALUE, result.getDistance(2));
        assertNull(result.getPath(2));
    }

    @Test
    public void testCycleDetection() {

        Graph graph = new Graph(3, true, "edge");
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        DAGShortestPath dagSP = new DAGShortestPath(graph);

        assertThrows(IllegalArgumentException.class, () -> {
            dagSP.shortestPaths(0);
        });
    }

    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true, "edge");

        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPaths(0);

        assertEquals(0, result.getDistance(0));
        assertEquals(List.of(0), result.getPath(0));
    }
}
