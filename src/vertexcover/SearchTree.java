package vertexcover;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class SearchTree {
    class Instance {
        Graph graph;
        int k;

        public Instance(Graph graph, int k) {
            this.graph = graph;
            this.k = k;
        }
    }

    private boolean solve(Instance i) {
        if (i.k < 0)
            return false;

        if (i.graph.getEdgeCount() == 0)
            return true;

        Integer u = -1, v = -1;
        for (Integer a : i.graph.getVertices()) {
            Set<Integer> neighbors = i.graph.getNeighbors(a);
            if (neighbors.size() > 0) {
                u = a;
                v = neighbors.iterator().next();
            }
        }

        Instance newInstance = new Instance(i.graph, i.k - 1);

        Set<Integer> neighbors = new HashSet<>(i.graph.getNeighbors(u));
        newInstance.graph.deleteVertex(u);
        boolean solved = solve(newInstance);

        newInstance.graph.addVertex(u);
        for (Integer n : neighbors) {
            newInstance.graph.addEdge(u, n);
        }

        if (solved)
            return true;

        neighbors = new HashSet<>(i.graph.getNeighbors(v));
        newInstance.graph.deleteVertex(v);
        solved = solve(newInstance);

        newInstance.graph.addVertex(v);
        for (Integer n : neighbors) {
            newInstance.graph.addEdge(v, n);
        }

        return solved;
    }

    public int solve(Graph g) {
        for (int k = 0; k < 100; k++) {
            if (solve(new Instance(g, k)))
                return k;
        }

        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        SearchTree i = new SearchTree();
        Graph g = new MyGraph("Aufgaben/Data/sample2");
        long start = System.currentTimeMillis();
        int k = i.solve(g);
        long end = System.currentTimeMillis();
        System.out.printf("|V|=%d, |E|=%d, k=%d (took %dms)\n", g.getVertices().size(), g.getEdgeCount(), k, end - start);
    }
}