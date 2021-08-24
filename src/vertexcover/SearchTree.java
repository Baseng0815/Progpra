package vertexcover;

import java.io.FileNotFoundException;
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

    private void removeSingletons(Instance instance) {
        Object[] vertices = instance.graph.getVertices().toArray();
        for (Object a : vertices) {
            Integer i = (Integer) a;
            if (instance.graph.degree(i) == 0) {
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
                    instance.graph.deleteVertex(instance.graph.getNeighbors(v).iterator().next());
                    instance.k--;
                    abort = false;
                    break;
                }
            }
        } while (!abort);
    }

    private void removeHighDeg(Instance instance) {
        Object[] vertices = instance.graph.getVertices().toArray();
        for (Object a : vertices) {
            Integer i = (Integer) a;
            if (instance.graph.degree(i) > instance.k) {
                instance.graph.deleteVertex(i);
                instance.k--;
            }
        }
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
    private boolean solve(Instance i) {
        if (i.k < 0) {
            return false;
        }

        if (i.graph.getEdgeCount() == 0) {
            return true;
        }

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

        Instance newInstance = new Instance(i.graph.getCopy(), i.k);

        newInstance.graph.deleteVertex(u);
        removeHighDeg(newInstance);
        removeDegOne(newInstance);
        removeSingletons(newInstance);

        newInstance.k--;
        if (solve(newInstance)) {
            return true;
        }

        newInstance.k = i.k;
        newInstance.graph = i.graph.getCopy();

        newInstance.graph.deleteVertex(v);
        removeHighDeg(newInstance);
        removeDegOne(newInstance);
        removeSingletons(newInstance);

        newInstance.k--;
        return solve(newInstance);
    }

    public int solve(Graph g) {
        for (int k = 0; k < g.size(); k++) {
            System.out.println(k);
            if (solve(new Instance(g, k))) {
                return k;
            }
        }
        return -1;
    }
}