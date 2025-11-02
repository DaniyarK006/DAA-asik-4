package path.testing;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;
import com.smartcity.graph.topo.DFSTopologicalSort;
import java.util.*;

public record DAGShortestPath(Graph graph, Metrics metrics) {
    public DAGShortestPath(Graph graph) {
        this(graph, new MetricsImpl());
    }
    public PathResult shortestPaths(int source) {
        return computePaths(source, false);
    }
    public PathResult longestPaths(int source) {
        return computePaths(source, true);
    }
    private PathResult computePaths(int source, boolean longest) {
        int n = graph.getN();
        DFSTopologicalSort topoSort = new DFSTopologicalSort(graph);
        List<Integer> topoOrder = topoSort.sort();

        if (topoOrder == null) {
            throw new IllegalArgumentException("Graph contains a cycle not a DAG");
        }

        int[] dist = new int[n];
        int[] pred = new int[n];
        Arrays.fill(dist, longest ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        Arrays.fill(pred, -1);
        dist[source] = 0;

        metrics.startTimer();
        for (int u : topoOrder) {
            if (dist[u] == (longest ? Integer.MIN_VALUE : Integer.MAX_VALUE)) {
                continue; // Unreachable vertex
            }
            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.to();
                int newDist = dist[u] + edge.weight();
                metrics.incrementCounter("Relaxations");

                boolean improved;
                if (longest) {
                    improved = newDist > dist[v];
                } else {
                    improved = newDist < dist[v];
                }

                if (improved) {
                    dist[v] = newDist;
                    pred[v] = u;
                    metrics.incrementCounter("distance_updates");
                }
            }
        }

        metrics.stopTimer();

        return new PathResult(dist, pred, source);
    }

    public record PathResult(int[] distances, int[] predecessors, int source) {

        public List<Integer> getPath(int dest) {
                if (distances[dest] == Integer.MAX_VALUE || distances[dest] == Integer.MIN_VALUE) {
                    return null; // No path exists
                }

                List<Integer> path = new ArrayList<>();
                int current = dest;
                while (current != -1) {
                    path.add(current);
                    current = predecessors[current];
                }
                Collections.reverse(path);
                return path;
            }

            public int getDistance(int dest) {
                return distances[dest];
            }

            public CriticalPath findCriticalPath() {
                int maxDist = Integer.MIN_VALUE;
                int endVertex = -1;

                for (int v = 0; v < distances.length; v++) {
                    if (distances[v] != Integer.MIN_VALUE && distances[v] > maxDist) {
                        maxDist = distances[v];
                        endVertex = v;
                    }
                }

                if (endVertex == -1) {
                    return null;
                }

                return new CriticalPath(getPath(endVertex), maxDist);
            }

            public void printShortestPaths() {
                System.out.println("\n=== Shortest Paths from Source " + source + " ===");
                for (int v = 0; v < distances.length; v++) {
                    if (distances[v] == Integer.MAX_VALUE) {
                        System.out.println("Vertex " + v + ": UNREACHABLE");
                    } else {
                        List<Integer> path = getPath(v);
                        System.out.println("Vertex " + v + ": distance = " + distances[v] + ", path = " + path);
                    }
                }
            }

            public void printLongestPaths() {
                System.out.println("\n=== Longest Paths from Source " + source + " ===");
                for (int v = 0; v < distances.length; v++) {
                    if (distances[v] == Integer.MIN_VALUE) {
                        System.out.println("Vertex " + v + ": UNREACHABLE");
                    } else {
                        List<Integer> path = getPath(v);
                        System.out.println("Vertex " + v + ": distance = " + distances[v] + ", path = " + path);
                    }
                }
            }
        }


    public record CriticalPath(List<Integer> path, int length) {

        public void print() {
                System.out.println("\n=== Critical Path (Longest Path) ===");
                System.out.println("Length: " + length);
                System.out.println("Path: " + path);
            }
        }
}