# Assignment 4 - Smart City Scheduling System
-------------------------------------------------------------------------------------------------
Implementation of graph algorithms: SCC detection, topological sorting, and DAG shortest paths.

## Files

**Documentation:**
- README.md - Setup and usage guide
- ANALYSIS.md - Performance results and analysis

**Code:**
- `src/main/java/com/smartcity/` - Implementation
- `src/test/java/` - JUnit tests
- `data/` - 9 test datasets

## Quick Start

```bash
mvn clean compile    # Build
mvn test            # Run tests
mvn exec:java -Dexec.mainClass="com.smartcity.Main"  # Run
```

## Implementation

**1. Strongly Connected Components**
- File: `graph/scc/TarjanSCC.java`
- Algorithm: Tarjan's O(V+E)
- Features: SCC detection, condensation graph

**2. Topological Sort**
- Files: `graph/topo/KahnTopologicalSort.java`, `DFSTopologicalSort.java`
- Algorithms: Kahn (BFS) and DFS variants
- Both O(V+E) with cycle detection

**3. DAG Shortest/Longest Paths**
- File: `graph/dagsp/DAGShortestPath.java`
- Features: Shortest paths, longest paths, critical path
- O(V+E) complexity

## Test Datasets

9 datasets covering different graph structures:
- Small (6-8 vertices): cyclic, DAG, multi-SCC
- Medium (10-18 vertices): dense, sparse, mixed
- Large (25-40 vertices): various structures

## Results

All algorithms achieve O(V+E) complexity:
- Tarjan SCC: ~3 operations per vertex
- Topological Sort: ~3 operations per vertex
- Execution times: <0.11ms for graphs up to 40 vertices

## Testing

12 JUnit tests with 100% pass rate covering:
- Cycle detection
- Multiple SCCs
- Pure DAGs
- Path reconstruction
- Edge cases
