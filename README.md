# Smart City Scheduling System | Daniyar Kairatov
Consolidates Strongly Connected Components, Topological Ordering, and DAG Shortest Paths for task dependency management and scheduling optimization.

---

##  Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Installation & Build](#installation--build)
- [Usage](#usage)
- [Algorithm Implementations](#algorithm-implementations)
- [Dataset Information](#dataset-information)
- [Testing](#testing)
- [Performance Metrics](#performance-metrics)
- [Results Analysis](#results-analysis)

---

##  Overview

This project consolidates three fundamental graph algorithms to solve real-world scheduling problems:

1. **Strongly Connected Components (SCC)** - Detects cyclic dependencies using Tarjan's algorithm
2. **Topological Ordering** - Determines valid task execution order using Kahn's and DFS methods
3. **DAG Shortest/Longest Paths** - Finds optimal paths and critical paths in directed acyclic graphs

### Scenario

City service tasks often have complex dependencies. Some form cycles (mutual dependencies), while others can be executed sequentially. This system:
- Identifies and compresses cyclic dependencies into single units (SCCs)
- Creates a condensation graph (DAG) from SCCs
- Computes optimal execution order
- Finds shortest paths (minimum time) and critical paths (maximum time/bottlenecks)

---

##  Features

- **Tarjan's SCC Algorithm**: O(V + E) detection of strongly connected components
- **Condensation Graph**: Automatic DAG creation from arbitrary directed graphs
- **Dual Topological Sort**: Both Kahn's (BFS) and DFS-based implementations
- **DAG Path Algorithms**: Single-source shortest and longest path computation
- **Critical Path Analysis**: Identifies bottlenecks in task scheduling
- **Comprehensive Metrics**: Operation counters and timing for performance analysis
- **Extensive Testing**: JUnit tests covering edge cases and correctness
- **JSON Data Loading**: Easy dataset management and testing

---

##  Project Structure

```
smart-city-scheduling/
├── src/
│   ├── main/java/com/smartcity/
│   │   ├── Main.java                      # Main workflow runner
│   │   ├── common/
│   │   │   ├── Graph.java                 # Graph data structure
│   │   │   ├── GraphLoader.java           # JSON graph loader
│   │   │   ├── Metrics.java               # Metrics interface
│   │   │   └── MetricsImpl.java           # Metrics implementation
│   │   └── graph/
│   │       ├── scc/
│   │       │   ├── TarjanSCC.java         # Tarjan's algorithm
│   │       │   └── CondensationGraph.java # DAG builder
│   │       ├── topo/
│   │       │   ├── KahnTopologicalSort.java
│   │       │   ├── DFSTopologicalSort.java
│   │       │   └── ComponentTopologicalSort.java
│   │       └── dagsp/
│   │           └── DAGShortestPath.java   # Shortest/longest paths
│   └── test/java/com/smartcity/graph/
│       ├── scc/TarjanSCCTest.java
│       ├── topo/TopologicalSortTest.java
│       └── dagsp/DAGShortestPathTest.java
├── data/
│   ├── tasks.json                         # Example dataset                                     
├── pom.xml                                # Maven build configuration
└── README.md                              # This file
```

---

### Clone the Repository

```bash
git clone <repository>
cd smart-city-scheduling
```

### Build with Maven

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Package JAR

```bash
mvn package
```

---

##  Usage

### Run Main Analysis

```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.smartcity.Main"

# Using compiled JAR
java -cp target/smart-city-scheduling-1.0.jar com.smartcity.Main data/tasks.json
```

### Expected Output

```
╔════════════════════════════════════════════════════╗
║      Smart City/Campus Task Scheduling System      ║
╚════════════════════════════════════════════════════╝

 Loading graph from: data/tasks.json
  Graph loaded successfully
  Vertices: 8
  Edges: 7
  ...

=== Strongly Connected Components ===
Total SCCs: 3
SCC 0 (size 3): [1, 2, 3]
SCC 1 (size 1): [0]
SCC 2 (size 4): [4, 5, 6, 7]

=== Critical Path (Longest Path) ===
Length: 8
Path: [1, 2]
```

---

##  Algorithm Implementations

### 1. Tarjan's SCC Algorithm

**File**: `TarjanSCC.java`

**Complexity**: O(V + E)

**Key Features**:
- Single DFS traversal
- Low-link value computation
- Stack-based SCC extraction

**Metrics Tracked**:
- DFS visits
- Edges explored
- Stack operations

### 2. Condensation Graph

**File**: `CondensationGraph.java`

**Purpose**: Converts graph with cycles into DAG where each SCC is a single node

**Process**:
1. Map each vertex to its SCC
2. Create new graph with SCC nodes
3. Add edges between different SCCs only

### 3. Topological Sorting

#### Kahn's Algorithm (BFS-based)
**File**: `KahnTopologicalSort.java`

- Uses in-degree tracking
- Queue-based processing
- Detects cycles if vertices remain

#### DFS-based
**File**: `DFSTopologicalSort.java`

- Post-order DFS traversal
- Stack-based result construction
- Uses recursion stack for cycle detection

### 4. DAG Shortest/Longest Paths

**File**: `DAGShortestPath.java`

**Complexity**: O(V + E)

**Features**:
- Topological sort preprocessing
- Edge relaxation in topo order
- Critical path detection
- Path reconstruction

**Weight Model**: Uses edge weights as specified in JSON (`weight_model: "edge"`)

---

### Dataset Characteristics

- **Density**: Mix of sparse (E ≈ V) and dense (E ≈ V²) graphs
- **Structure**: Cyclic, acyclic, and mixed
- **SCCs**: Includes graphs with 1, 2-3, and 5+ components
- **Weights**: Edge weights range from 1-10

### JSON Format

```json
{
  "directed": true,
  "n": 8,
  "edges": [
    {"u": 0, "v": 1, "w": 3},
    {"u": 1, "v": 2, "w": 2}
  ],
  "source": 0,
  "weight_model": "edge"
}
```

---

##  Testing

### Test Coverage

- **TarjanSCCTest**: Single vertex, simple cycles, multiple SCCs, DAGs, mixed structures
- **TopologicalSortTest**: Both Kahn and DFS, cycle detection, disconnected components
- **DAGShortestPathTest**: Shortest/longest paths, unreachable vertices, cycle detection

### Run Specific Test Class

```bash
mvn test -Dtest=TarjanSCCTest
```

### Run All Tests with Coverage

```bash
mvn test jacoco:report
```

---

##  Performance Metrics

### Tracked Metrics

| Algorithm | Metrics |
|-----------|---------|
| Tarjan SCC | DFS visits, edges explored, stack pops |
| Topological Sort | Queue pushes/pops (Kahn), DFS visits (DFS), edge relaxations |
| DAG Shortest Path | Relaxations, distance updates |

### Example Output

```
=== Metrics Summary ===
Time: 0.234 ms
Counters:
  dfs_visits: 8
  edges_explored: 7
  stack_pops: 8
```

---

##  Results Analysis

### Example Analysis (from `tasks.json`)

**Graph Properties**:
- 8 vertices, 7 edges
- 3 strongly connected components
- Weight model: edge weights

**SCC Analysis**:
- SCC 0: Cycle of 3 vertices [1, 2, 3] - mutual dependencies in repairs
- SCC 1: Single vertex [0] - independent task
- SCC 2: Linear chain [4, 5, 6, 7] - sequential sensor checks

**Critical Path**: Component 1 → Component 2 (length: 8)
- Bottleneck in sensor maintenance sequence

**Recommendations**:
1. Tasks in SCC 0 must be scheduled together (atomic unit)
2. Critical path indicates sensor checks are the longest sequence
3. Task 0 can start immediately (no dependencies)

### Complexity Analysis

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Tarjan SCC | O(V+E) | O(V) | Single DFS pass |
| Condensation | O(V+E) | O(V) | Linear in graph size |
| Kahn Topo | O(V+E) | O(V) | Queue + in-degrees |
| DFS Topo | O(V+E) | O(V) | Recursion stack |
| DAG Paths | O(V+E) | O(V) | After topo sort |

**Bottleneck**: For dense graphs (E → V²), edge processing dominates

---

##  Academic Notes

### When to Use Each Algorithm

**Tarjan vs Kosaraju**:
- Tarjan: Single pass, more cache-friendly, preferred
- Kosaraju: Easier to understand, requires graph transpose

**Kahn vs DFS Topological Sort**:
- Kahn: Better for online algorithms, explicit queue
- DFS: More intuitive, easier recursion

**DAG Shortest Paths vs Dijkstra**:
- DAG: O(V+E), works with negative weights
- Dijkstra: O((V+E)log V), positive weights only

### Practical Recommendations

1. **Large Sparse Graphs**: Tarjan + DFS Topo + DAG Paths (all O(V+E))
2. **Dense Graphs**: Consider condensation early to reduce graph size
3. **Real-time Systems**: Kahn's algorithm for incremental updates
4. **Critical Path Analysis**: Always use longest path after SCC detection

---

##  Contributing

This is an academic project. For improvements:
1. Fork the repository
2. Create feature branch
3. Add tests for new features
4. Ensure `mvn test` passes
5. Submit pull request


##  Author

**Daniyar Kairatov**  
Course: Desing and Analysis of Algorithms
/ Assignment 4: SCC, Topological Sort & DAG Shortest Paths
