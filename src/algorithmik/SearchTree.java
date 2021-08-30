package algorithmik;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class SearchTree {

    public SearchTree(Graph g) {
        edgeStack = new Edge[g.getEdgeCount()]; // might increase later
        vertexStack = new Integer[g.size()];
    }

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
                vertexStack[vSIndex++] = i;
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
                    int var = instance.graph.getNeighbors(v).iterator().next();
                    for (Integer n : instance.graph.getNeighbors(var)) {
                        edgeStack[eSIndex++] = new Edge(n, var);
                    }
                    vertexStack[vSIndex++] = var;
                    instance.graph.deleteVertex(var);
                    instance.k--;
                    if (instance.k < 0) {
                        return;
                    }
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
                for (Integer n : instance.graph.getNeighbors(i)) {
                    edgeStack[eSIndex++] = new Edge(n, i);
                }
                vertexStack[vSIndex++] = i;
                instance.graph.deleteVertex(i);
                instance.k--;
                if (instance.k < 0) {
                    return;
                }
            }
        }
    }


    private Integer getBestNeighbor(Set<Integer> neighbors, Graph g) {
        int maxDel = -1, u = -1;
        for (Integer n : neighbors) {
            int newMaxDel = g.getNeighbors(n).size();
            if (newMaxDel > maxDel) {
                maxDel = newMaxDel;
                u = n;
            }
        }
        return u;
    }

    private final Set<Integer> isInMatching = new HashSet<>();

    private int computeMatching(Instance i) {
        isInMatching.clear();
        for (Integer a : i.graph.getVertices()) {
            if (isInMatching.size() / 2 > i.k) {
                return isInMatching.size() / 2;
            }
            if (!isInMatching.contains(a)) {
                for (Integer n : i.graph.getNeighbors(a)) {
                    if (isInMatching.add(n)) {
                        isInMatching.add(a);
                        break;
                    }
                }
            }
        }
        return isInMatching.size() / 2;
    }

    private static class Edge {
        int first, second;

        public Edge(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    Integer[] vertexStack;
    int vSIndex = 0;

    Edge[] edgeStack;
    int eSIndex = 0;


    /* LowerBound:
    1. k größten knoten wählen
    2. alle nachbarschaftswertae aufsummieren
    3. wenn ergebnis kleiner |E| => nicht lösbar
     */


    private boolean solve(Instance i) {
        if (i.k < 0) {
            return false;
        }

        if (i.graph.getEdgeCount() == 0) {
            return true;
        }

        if (computeMatching(i) > i.k) {
            return false;
        }

        int u = -1;
        int maxDel = -1;
        for (Integer a : i.graph.getVertices()) {
            Set<Integer> neighbors = i.graph.getNeighbors(a);
            if (neighbors.size() > 0) {
                int nv = getBestNeighbor(neighbors, i.graph), nvSize =
                        i.graph.getNeighbors(nv).size();
                int newMaxDel = Math.min(neighbors.size(), nvSize);

                if (newMaxDel > maxDel) {
                    u = a;
                    // assure that u is the one with the most neighbors
                    if (neighbors.size() < nvSize) {
                        u = nv;
                    }
                    maxDel = newMaxDel;
                }
            }
        }

        final int vSIPrev = vSIndex, eSIPrev = eSIndex, oldK = i.k;

        // branch 1
        Object[] neighb = i.graph.getNeighbors(u).toArray();
        for (Object n : neighb) {
            Integer a = (Integer) n;
            for (Integer l : i.graph.getNeighbors(a)) {
                edgeStack[eSIndex++] = new Edge(a, l);
            }
            vertexStack[vSIndex++] = a;
            i.graph.deleteVertex(a);
        }
        vertexStack[vSIndex++] = u;
        i.graph.deleteVertex(u); // singleton

        // newInstance.graph.deleteVertex(v); taken over by optimization
        removeHighDeg(i);
        removeDegOne(i);
        removeSingletons(i);
        i.k -= neighb.length;
        boolean solve = solve(i);

        // restore by difference
        while (vSIndex > vSIPrev) {
            i.graph.addVertex(vertexStack[--vSIndex]);
        }
        while (eSIndex > eSIPrev) {
            Edge e = edgeStack[--eSIndex];
            i.graph.addEdge(e.first, e.second);
        }
        i.k = oldK;

        if (solve) {
            return true;
        }

        // branch 2
        for (Integer l : i.graph.getNeighbors(u)) {
            edgeStack[eSIndex++] = new Edge(u, l);
        }
        vertexStack[vSIndex++] = u;
        i.graph.deleteVertex(u);
        removeHighDeg(i);
        removeDegOne(i);
        removeSingletons(i);
        i.k--;
        solve = solve(i);

        // restore by difference
        while (vSIndex > vSIPrev) {
            i.graph.addVertex(vertexStack[--vSIndex]);
        }
        while (eSIndex > eSIPrev) {
            Edge e = edgeStack[--eSIndex];
            i.graph.addEdge(e.first, e.second);
        }
        i.k = oldK;
        return solve;
    }

    public int solve(Graph g) {
        for (int k = 0; k < g.size(); k++) {
            System.out.println(k);
            if (solve(new Instance(g.getCopy(), k))) {
                return k;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Graph g = new MyGraph("data/" + files[2]);
        SearchTree i = new SearchTree(g);
        long start = System.currentTimeMillis();
        int k = i.solve(g);
        long end = System.currentTimeMillis();
        System.out.printf("|V|=%d, |E|=%d, k=%d (took %dms)\n",
                g.getVertices().size(), g.getEdgeCount(), k, end - start);
    }

    final static String[] files = {
            "bio-dmelamtx.sec", "inf-openflightsedges.sec", "inf-USAir97mtx.sec",
            "outarenas-email.sec", "outcontiguous-usa.sec", "outmoreno_zebra_zebra" +
            ".sec",
            "sample", "soc-brightkitemtx.sec", "ca-sandi_authsmtx.sec",
            "inf-powermtx.sec", "outadjnoun_adjacency_adjacency.sec", "outarenas" +
            "-jazz.sec",
            "outdolphins.sec", "outucidata-zachary.sec", "sample2"
    };


}