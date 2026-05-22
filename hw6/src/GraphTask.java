import java.util.*;

/**
 * Models fog navigation in a forest using a weighted graph.
 * Vertices represent forest locations and arcs represent paths between them.
 * Each arc has a traversal time, a visibility value and a blocked flag.
 * The main goal of the program is to find the best path from a start vertex
 * to one of the exit vertices.
 *
 * @author Your Name
 * @version 1.0
 */
public class GraphTask {

    /**
     * Starts the program.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        GraphTask a = new GraphTask();
        a.run();
    }

    /**
     * Runs all demonstration examples.
     * This method includes at least five small examples and one large graph
     * with more than 2000 vertices.
     */
    public void run() {
        example1SimplePath();
        example2BlockedPath();
        example3NoPath();
        example4MultipleExits();
        example5LongPath();
        example6LargeGraph();
    }

    /**
     * Demonstrates the simplest case where there is exactly one clear path
     * from the start vertex to the exit.
     */
    private void example1SimplePath() {
        Graph g = new Graph("Example 1");

        Vertex a = g.createVertex("A");
        Vertex b = g.createVertex("B");
        Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 3, 8, false);
        g.createArcPair("BC", b, c, 2, 9, false);

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(c);

        PathResult result = g.findBestFogPath(a, exits);

        System.out.println("=== Example 1: Simple path ===");
        System.out.println(g);
        System.out.println("Start: " + a);
        System.out.println("Exits: " + exits);
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Demonstrates a case where one possible route is blocked and the algorithm
     * must choose another valid path.
     */
    private void example2BlockedPath() {
        Graph g = new Graph("Example 2");

        Vertex a = g.createVertex("A");
        Vertex b = g.createVertex("B");
        Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 1, 10, true);
        g.createArcPair("AC", a, c, 5, 10, false);
        g.createArcPair("BC", b, c, 1, 10, false);

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(c);

        PathResult result = g.findBestFogPath(a, exits);

        System.out.println("=== Example 2: Blocked path ===");
        System.out.println(g);
        System.out.println("Start: " + a);
        System.out.println("Exits: " + exits);
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Demonstrates a case where no path to the exit exists.
     */
    private void example3NoPath() {
        Graph g = new Graph("Example 3");

        Vertex a = g.createVertex("A");
        Vertex b = g.createVertex("B");
        Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 2, 8, true);

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(c);

        PathResult result = g.findBestFogPath(a, exits);

        System.out.println("=== Example 3: No path available ===");
        System.out.println(g);
        System.out.println("Start: " + a);
        System.out.println("Exits: " + exits);
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Demonstrates a case with multiple exits where the algorithm must choose
     * the exit with the smallest total cost.
     */
    private void example4MultipleExits() {
        Graph g = new Graph("Example 4");

        Vertex s = g.createVertex("S");
        Vertex x = g.createVertex("X");
        Vertex y = g.createVertex("Y");

        g.createArcPair("SX", s, x, 10, 10, false);
        g.createArcPair("SY", s, y, 1, 10, false);

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(x);
        exits.add(y);

        PathResult result = g.findBestFogPath(s, exits);

        System.out.println("=== Example 4: Multiple exits ===");
        System.out.println(g);
        System.out.println("Start: " + s);
        System.out.println("Exits: " + exits);
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Demonstrates a longer path that consists of several consecutive arcs.
     */
    private void example5LongPath() {
        Graph g = new Graph("Example 5");

        Vertex a = g.createVertex("A");
        Vertex b = g.createVertex("B");
        Vertex c = g.createVertex("C");
        Vertex d = g.createVertex("D");
        Vertex e = g.createVertex("E");

        g.createArcPair("AB", a, b, 2, 7, false);
        g.createArcPair("BC", b, c, 2, 7, false);
        g.createArcPair("CD", c, d, 2, 8, false);
        g.createArcPair("DE", d, e, 3, 9, false);

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(e);

        PathResult result = g.findBestFogPath(a, exits);

        System.out.println("=== Example 5: Long path ===");
        System.out.println(g);
        System.out.println("Start: " + a);
        System.out.println("Exits: " + exits);
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Demonstrates performance on a graph with more than 2000 vertices.
     * Only the running time and basic result information are printed.
     */
    private void example6LargeGraph() {
        Graph g = new Graph("LargeGraph");

        int n = 2200;
        Vertex[] vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = g.createVertex("V" + i);
        }

        for (int i = 0; i < n - 1; i++) {
            g.createArcPair("E" + i, vertices[i], vertices[i + 1], 1, 10, false);
        }

        List<Vertex> exits = new ArrayList<Vertex>();
        exits.add(vertices[n - 1]);

        long startTime = System.currentTimeMillis();
        PathResult result = g.findBestFogPath(vertices[0], exits);
        long endTime = System.currentTimeMillis();

        System.out.println("=== Example 6: Large graph (2200 vertices) ===");
        System.out.println("Start: " + vertices[0]);
        System.out.println("Exit: " + vertices[n - 1]);
        System.out.println("Path length: " + result.getPath().size());
        System.out.println("Total cost: " + result.getTotalCost());
        System.out.println("Time (ms): " + (endTime - startTime));
        System.out.println();
    }

    /**
     * Represents a vertex in the graph.
     * A vertex may correspond to a forest junction, a landmark or an exit.
     */
    class Vertex {

        /** Vertex identifier. */
        private String id;

        /** Reference to the next vertex in the internal linked list. */
        private Vertex next;

        /** Reference to the first outgoing arc of this vertex. */
        private Arc first;

        /** Auxiliary field used by graph algorithms. */
        private int info = 0;

        /**
         * Creates a new vertex with full initialization data.
         *
         * @param s vertex identifier
         * @param v next vertex in the list
         * @param e first outgoing arc
         */
        Vertex(String s, Vertex v, Arc e) {
            id = s;
            next = v;
            first = e;
        }

        /**
         * Creates a new vertex with no next vertex and no outgoing arcs.
         *
         * @param s vertex identifier
         */
        Vertex(String s) {
            this(s, null, null);
        }

        /**
         * Returns the identifier of this vertex.
         *
         * @return vertex identifier
         */
        @Override
        public String toString() {
            return id;
        }

        /**
         * Returns the identifier of this vertex.
         *
         * @return vertex identifier
         */
        public String getId() {
            return id;
        }

        /**
         * Returns the next vertex in the linked list.
         *
         * @return next vertex
         */
        public Vertex getNext() {
            return next;
        }

        /**
         * Returns the first outgoing arc of this vertex.
         *
         * @return first outgoing arc
         */
        public Arc getFirst() {
            return first;
        }

        /**
         * Returns the value of the auxiliary info field.
         *
         * @return auxiliary field value
         */
        public int getInfo() {
            return info;
        }

        /**
         * Updates the value of the auxiliary info field.
         *
         * @param info new auxiliary value
         */
        public void setInfo(int info) {
            this.info = info;
        }
    }

    /**
     * Represents one directed arc in the graph.
     * An undirected edge is modeled as two opposite arcs.
     */
    class Arc {

        /** Arc identifier. */
        private String id;

        /** Target vertex of the arc. */
        private Vertex target;

        /** Reference to the next arc in the adjacency list. */
        private Arc next;

        /** Auxiliary field used by graph algorithms. */
        private int info = 0;

        /** Traversal time of the arc. */
        private int time;

        /** Visibility value of the arc. */
        private int visibility;

        /** Indicates whether this arc is blocked. */
        private boolean blocked;

        /**
         * Creates a new arc with full initialization data.
         *
         * @param s arc identifier
         * @param v target vertex
         * @param a next arc in the adjacency list
         */
        Arc(String s, Vertex v, Arc a) {
            id = s;
            target = v;
            next = a;
        }

        /**
         * Creates a new arc with no target and no next reference.
         *
         * @param s arc identifier
         */
        Arc(String s) {
            this(s, null, null);
        }

        /**
         * Returns a textual description of the arc.
         *
         * @return textual description
         */
        @Override
        public String toString() {
            return id + "[time=" + time + ", visibility=" + visibility
                    + ", blocked=" + blocked + "]";
        }

        /**
         * Returns the identifier of the arc.
         *
         * @return arc identifier
         */
        public String getId() {
            return id;
        }

        /**
         * Returns the target vertex of the arc.
         *
         * @return target vertex
         */
        public Vertex getTarget() {
            return target;
        }

        /**
         * Returns the next arc in the adjacency list.
         *
         * @return next arc
         */
        public Arc getNext() {
            return next;
        }

        /**
         * Returns the value of the auxiliary info field.
         *
         * @return auxiliary field value
         */
        public int getInfo() {
            return info;
        }

        /**
         * Sets the value of the auxiliary info field.
         *
         * @param info new auxiliary value
         */
        public void setInfo(int info) {
            this.info = info;
        }

        /**
         * Returns the traversal time of the arc.
         *
         * @return traversal time
         */
        public int getTime() {
            return time;
        }

        /**
         * Sets the traversal time of the arc.
         *
         * @param time traversal time
         */
        public void setTime(int time) {
            this.time = time;
        }

        /**
         * Returns the visibility value of the arc.
         *
         * @return visibility value
         */
        public int getVisibility() {
            return visibility;
        }

        /**
         * Sets the visibility value of the arc.
         *
         * @param visibility visibility value
         */
        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        /**
         * Returns whether the arc is blocked.
         *
         * @return {@code true} if blocked, otherwise {@code false}
         */
        public boolean isBlocked() {
            return blocked;
        }

        /**
         * Sets whether the arc is blocked.
         *
         * @param blocked blocked state
         */
        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        /**
         * Computes the combined cost of this arc.
         * Smaller time and better visibility produce a smaller cost.
         *
         * @return combined arc cost
         */
        public int getCost() {
            return time + (10 - visibility);
        }
    }

    /**
     * Represents the whole graph and contains graph construction
     * and path-finding operations.
     */
    class Graph {

        /** Graph identifier. */
        private String id;

        /** First vertex in the internal vertex list. */
        private Vertex first;

        /** Auxiliary field used by graph algorithms. */
        private int info = 0;

        /**
         * Creates a graph with a given identifier and first vertex.
         *
         * @param s graph identifier
         * @param v first vertex
         */
        Graph(String s, Vertex v) {
            id = s;
            first = v;
        }

        /**
         * Creates an empty graph with a given identifier.
         *
         * @param s graph identifier
         */
        Graph(String s) {
            this(s, null);
        }

        /**
         * Returns a textual representation of the graph.
         *
         * @return textual representation of the graph
         */
        @Override
        public String toString() {
            String nl = System.getProperty("line.separator");
            StringBuffer sb = new StringBuffer(nl);
            sb.append(id);
            sb.append(nl);
            Vertex v = first;
            while (v != null) {
                sb.append(v.toString());
                sb.append(" -->");
                Arc a = v.first;
                while (a != null) {
                    sb.append(" ");
                    sb.append(a.toString());
                    sb.append(" (");
                    sb.append(v.toString());
                    sb.append("->");
                    sb.append(a.target.toString());
                    sb.append(")");
                    a = a.next;
                }
                sb.append(nl);
                v = v.next;
            }
            return sb.toString();
        }

        /**
         * Creates a new vertex and adds it to the graph.
         *
         * @param vid vertex identifier
         * @return created vertex
         */
        public Vertex createVertex(String vid) {
            Vertex res = new Vertex(vid);
            res.next = first;
            first = res;
            return res;
        }

        /**
         * Creates a new directed arc from one vertex to another.
         *
         * @param aid arc identifier
         * @param from source vertex
         * @param to target vertex
         * @return created arc
         */
        public Arc createArc(String aid, Vertex from, Vertex to) {
            Arc res = new Arc(aid);
            res.next = from.first;
            from.first = res;
            res.target = to;
            return res;
        }

        /**
         * Creates a connected undirected random tree with {@code n} vertices.
         * Each new vertex is connected to one random existing vertex.
         *
         * @param n number of vertices
         */
        public void createRandomTree(int n) {
            if (n <= 0)
                return;
            Vertex[] varray = new Vertex[n];
            for (int i = 0; i < n; i++) {
                varray[i] = createVertex("v" + String.valueOf(n - i));
                if (i > 0) {
                    int vnr = (int)(Math.random() * i);
                    createArc("a" + varray[vnr].toString() + "_"
                            + varray[i].toString(), varray[vnr], varray[i]);
                    createArc("a" + varray[i].toString() + "_"
                            + varray[vnr].toString(), varray[i], varray[vnr]);
                }
            }
        }

        /**
         * Creates the adjacency matrix of this graph.
         * This method overwrites the {@code info} fields of the vertices.
         *
         * @return adjacency matrix
         */
        public int[][] createAdjMatrix() {
            info = 0;
            Vertex v = first;
            while (v != null) {
                v.info = info++;
                v = v.next;
            }
            int[][] res = new int[info][info];
            v = first;
            while (v != null) {
                int i = v.info;
                Arc a = v.first;
                while (a != null) {
                    int j = a.target.info;
                    res[i][j]++;
                    a = a.next;
                }
                v = v.next;
            }
            return res;
        }

        /**
         * Creates a connected simple undirected random graph with {@code n} vertices
         * and {@code m} edges.
         *
         * @param n number of vertices
         * @param m number of edges
         * @throws IllegalArgumentException if the given values are impossible
         */
        public void createRandomSimpleGraph(int n, int m) {
            if (n <= 0)
                return;
            if (n > 2500)
                throw new IllegalArgumentException("Too many vertices: " + n);
            if (m < n - 1 || m > n * (n - 1) / 2)
                throw new IllegalArgumentException("Impossible number of edges: " + m);
            first = null;
            createRandomTree(n);
            Vertex[] vert = new Vertex[n];
            Vertex v = first;
            int c = 0;
            while (v != null) {
                vert[c++] = v;
                v = v.next;
            }
            int[][] connected = createAdjMatrix();
            int edgeCount = m - n + 1;
            while (edgeCount > 0) {
                int i = (int)(Math.random() * n);
                int j = (int)(Math.random() * n);
                if (i == j)
                    continue;
                if (connected[i][j] != 0 || connected[j][i] != 0)
                    continue;
                Vertex vi = vert[i];
                Vertex vj = vert[j];
                createArc("a" + vi.toString() + "_" + vj.toString(), vi, vj);
                connected[i][j] = 1;
                createArc("a" + vj.toString() + "_" + vi.toString(), vj, vi);
                connected[j][i] = 1;
                edgeCount--;
            }
        }

        /**
         * Creates two opposite arcs for one undirected edge and assigns
         * the same path data to both arcs.
         *
         * @param baseId base identifier of the edge
         * @param v1 first endpoint
         * @param v2 second endpoint
         * @param time traversal time
         * @param visibility visibility value
         * @param blocked blocked state
         */
        public void createArcPair(String baseId, Vertex v1, Vertex v2,
                                  int time, int visibility, boolean blocked) {
            Arc a1 = createArc(baseId + "_1", v1, v2);
            a1.setTime(time);
            a1.setVisibility(visibility);
            a1.setBlocked(blocked);

            Arc a2 = createArc(baseId + "_2", v2, v1);
            a2.setTime(time);
            a2.setVisibility(visibility);
            a2.setBlocked(blocked);
        }

        /**
         * Finds the best path from the start vertex to one of the exits.
         * Blocked arcs are ignored.
         *
         * @param start start vertex
         * @param exits list of possible exits
         * @return result object containing the path, total cost and reached exit
         */
        public PathResult findBestFogPath(Vertex start, List<Vertex> exits) {
            if (start == null || exits == null || exits.isEmpty()) {
                return new PathResult(new ArrayList<Arc>(), Integer.MAX_VALUE, null);
            }

            Map<Vertex, Integer> dist = new HashMap<Vertex, Integer>();
            Map<Vertex, Arc> prevArc = new HashMap<Vertex, Arc>();
            Map<Vertex, Vertex> prevVertex = new HashMap<Vertex, Vertex>();
            PriorityQueue<Node> pq = new PriorityQueue<Node>();

            Vertex v = first;
            while (v != null) {
                dist.put(v, Integer.MAX_VALUE);
                v = v.next;
            }

            dist.put(start, 0);
            pq.add(new Node(start, 0));

            while (!pq.isEmpty()) {
                Node current = pq.poll();

                if (current.dist != dist.get(current.vertex)) {
                    continue;
                }

                if (exits.contains(current.vertex)) {
                    List<Arc> path = reconstructPath(start, current.vertex, prevArc, prevVertex);
                    return new PathResult(path, dist.get(current.vertex), current.vertex);
                }

                Arc a = current.vertex.first;
                while (a != null) {
                    if (!a.isBlocked()) {
                        Vertex nextVertex = a.target;
                        int newDist = dist.get(current.vertex) + a.getCost();

                        if (newDist < dist.get(nextVertex)) {
                            dist.put(nextVertex, newDist);
                            prevArc.put(nextVertex, a);
                            prevVertex.put(nextVertex, current.vertex);
                            pq.add(new Node(nextVertex, newDist));
                        }
                    }
                    a = a.next;
                }
            }

            return new PathResult(new ArrayList<Arc>(), Integer.MAX_VALUE, null);
        }

        /**
         * Reconstructs the found path as a list of arcs.
         *
         * @param start start vertex
         * @param end end vertex
         * @param prevArc map of predecessor arcs
         * @param prevVertex map of predecessor vertices
         * @return path as a list of arcs
         */
        private List<Arc> reconstructPath(Vertex start, Vertex end,
                                          Map<Vertex, Arc> prevArc,
                                          Map<Vertex, Vertex> prevVertex) {
            LinkedList<Arc> path = new LinkedList<Arc>();
            Vertex current = end;

            while (current != start) {
                Arc a = prevArc.get(current);
                Vertex previous = prevVertex.get(current);
                if (a == null || previous == null) {
                    return new ArrayList<Arc>();
                }
                path.addFirst(a);
                current = previous;
            }
            return path;
        }
    }

    /**
     * Helper class for the priority queue used in Dijkstra's algorithm.
     */
    class Node implements Comparable<Node> {

        /** Vertex stored in this queue entry. */
        Vertex vertex;

        /** Current distance to the vertex. */
        int dist;

        /**
         * Creates a queue entry.
         *
         * @param vertex stored vertex
         * @param dist current distance
         */
        Node(Vertex vertex, int dist) {
            this.vertex = vertex;
            this.dist = dist;
        }

        /**
         * Compares queue entries by distance.
         *
         * @param other other queue entry
         * @return comparison result
         */
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    /**
     * Stores the result of the path search.
     */
    class PathResult {

        /** Path as a list of arcs. */
        private List<Arc> path;

        /** Total path cost. */
        private int totalCost;

        /** Exit vertex reached by the path. */
        private Vertex endVertex;

        /**
         * Creates a result object.
         *
         * @param path found path
         * @param totalCost total cost of the path
         * @param endVertex reached exit vertex
         */
        PathResult(List<Arc> path, int totalCost, Vertex endVertex) {
            this.path = path;
            this.totalCost = totalCost;
            this.endVertex = endVertex;
        }

        /**
         * Returns the found path.
         *
         * @return path as a list of arcs
         */
        public List<Arc> getPath() {
            return path;
        }

        /**
         * Returns the total path cost.
         *
         * @return total path cost
         */
        public int getTotalCost() {
            return totalCost;
        }

        /**
         * Returns the reached exit vertex.
         *
         * @return reached exit vertex
         */
        public Vertex getEndVertex() {
            return endVertex;
        }

        /**
         * Returns a textual description of the result.
         *
         * @return textual description of the result
         */
        @Override
        public String toString() {
            return "path=" + path + ", totalCost=" + totalCost + ", endVertex=" + endVertex;
        }
    }
}