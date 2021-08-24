package vertexcover;

import java.util.LinkedList;
import java.util.Set;

public class SearchTree {
    private static class Instance {
        Graph graph;
        int k;

        public Instance(Graph graph, int k) {
            this.graph = graph;
            this.k = k;
        }
    }

    private static class Pair {
        Integer a;
        Integer b;

        public Pair(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }
    }

    private LinkedList<Pair>     removedEdges;
    private LinkedList<Integer>  removedVertices;

    public SearchTree() {
        this.removedEdges       = new LinkedList<Pair>();
        this.removedVertices    = new LinkedList<Integer>();
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
                break;
            }
        }

        Instance newInstance = new Instance(i.graph.getCopy(), i.k - 1);
        newInstance.graph.deleteVertex(u);
        if (solve(newInstance))
            return true;

        newInstance.graph = i.graph.getCopy();
        newInstance.graph.deleteVertex(v);

        return solve(newInstance);
    }

    private void removeSingletons(Instance instance) {
        Object[] vertices = instance.graph.getVertices().toArray();
        for (Object a : vertices) {
            Integer i = (Integer) a;
            if (instance.graph.degree(i) == 0) {
                /* degree was 0 => 0 removed edges */
                removedVertices.addFirst(i);
                instance.graph.deleteVertex(i);
            }
        }
    }

    private void removeDegOne(Instance instance) {
        boolean abort;
        do {
            abort = true;

            Object[] vertices = instance.graph.getVertices().toArray();
            for (Object a : vertices) {
                Integer v = (Integer) a;
                if (instance.graph.degree(v) == 1) {
                    Integer neighbor = instance.graph.getNeighbors(v).iterator().next();
                    removedVertices.addFirst(neighbor);
                    for (Integer nneighbor : instance.graph.getNeighbors(neighbor)) {
                        removedEdges.addFirst(new Pair(neighbor, nneighbor));
                    }
                    instance.graph.deleteVertex(neighbor);

                    instance.k--;
                    abort = false;
                    break;
                }
            }
        } while (!abort);
    }

    private void removeHighDeg(Instance instance) {
        boolean abort;
        do {
            abort = true;

            Object[] vertices = instance.graph.getVertices().toArray();
            for (Object a : vertices) {
                Integer v = (Integer) a;
                if (instance.graph.degree(v) > instance.k) {
                    removedVertices.addFirst(v);
                    for (Integer neighbor : instance.graph.getNeighbors(v)) {
                        removedEdges.addFirst(new Pair(v, neighbor));
                    }
                    instance.graph.deleteVertex(v);

                    instance.k--;
                    abort = false;
                    break;
                }
            }
        } while (!abort);
    }

    private Integer getBestNeighbor(Set<Integer> neighbors, Graph g) {
        int maxDel = -1, u = -1;
        for(Integer n : neighbors) {
            int newMaxDel = g.getNeighbors(n).size();
            if(newMaxDel > maxDel) {
                maxDel = newMaxDel;
                u = n;
            }
        }
        return u;
    }

    private boolean solveEfficient(Instance i) {
        if (i.k < 0)
            return false;

        if (i.graph.getEdgeCount() == 0)
            return true;

        int u = -1, v = -1;
        int maxDel = -1;
        for (Integer a : i.graph.getVertices()) {
            Set<Integer> neighbors = i.graph.getNeighbors(a);
            if (neighbors.size() > 0) {
                int nv = getBestNeighbor(neighbors, i.graph);
                int newMaxDel = Math.min(neighbors.size(),
                        i.graph.getNeighbors(nv).size());
                if (newMaxDel > maxDel) {
                    u = a;
                    v = nv;
                    maxDel = newMaxDel;
                }
            }
        }

        /* we need to avoid copies */

        int prevVertStackTop = removedVertices.size();
        int prevEdgeStackTop = removedEdges.size();

        Instance newInstance = new Instance(i.graph, 0);
        newInstance.graph = i.graph;
        /* ---------- first vertex (u) ---------- */
        newInstance.k = i.k;
        removedVertices.addFirst(u);
        for (Integer n : i.graph.getNeighbors(u))
            removedEdges.addFirst(new Pair(u, n));
        newInstance.graph.deleteVertex(u);

        removeSingletons(newInstance);
        removeDegOne(newInstance);
        removeHighDeg(newInstance);
        newInstance.k--;
        boolean result = solveEfficient(newInstance);

        /* restore old state */
        int curStackTop = removedVertices.size();
        for (int counter = 0; counter < curStackTop - prevVertStackTop; counter++) {
            newInstance.graph.addVertex(removedVertices.removeFirst());
        }
        curStackTop = removedEdges.size();
        for (int counter = 0; counter < curStackTop - prevEdgeStackTop; counter++) {
            Pair p = removedEdges.removeFirst();
            newInstance.graph.addEdge(p.a, p.b);
        }

        if (result)
            return true;

        /* ---------- second vertex (v) ---------- */
        newInstance.k = i.k;
        removedVertices.addFirst(v);
        for (Integer n : i.graph.getNeighbors(v))
            removedEdges.addFirst(new Pair(v, n));
        newInstance.graph.deleteVertex(v);

        removeSingletons(newInstance);
        removeDegOne(newInstance);
        removeHighDeg(newInstance);
        newInstance.k--;
        result = solveEfficient(newInstance);

        /* restore old state */
        curStackTop = removedVertices.size();
        for (int counter = 0; counter < curStackTop - prevVertStackTop; counter++) {
            newInstance.graph.addVertex(removedVertices.removeFirst());
        }
        curStackTop = removedEdges.size();
        for (int counter = 0; counter < curStackTop - prevEdgeStackTop; counter++) {
            Pair p = removedEdges.removeFirst();
            newInstance.graph.addEdge(p.a, p.b);
        }

        return result;
    }

    public int solve(Graph g) {
        for (int k = 0; k < g.size(); k++) {
            System.out.println(k);
            if (solveEfficient(new Instance(g, k)))
                return k;
        }

        return -1;
    }
}