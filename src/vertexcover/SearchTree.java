import java.io.FileNotFoundException;
import java.util.HashSet;
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
                instance.graph.deleteVertex(i);
                instance.k--;
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
        for(Integer a : i.graph.getVertices()) {
            if(isInMatching.size() / 2 > i.k) return isInMatching.size()/2;
            if(!isInMatching.contains(a)) {
                for(Integer n : i.graph.getNeighbors(a)) {
                    if(isInMatching.add(n)) {
                        isInMatching.add(a);
                        break;
                    }
                }
            }
        }
        return isInMatching.size()/2;
    }


    private boolean solve(Instance i) {
        if (i.k < 0) {
            return false;
        }

        if (i.graph.getEdgeCount() == 0) {
            return true;
        }

        if(computeMatching(i) > i.k) return false;

        int u = -1;
        int maxDel = -1;
        for (Integer a : i.graph.getVertices()) {
            Set<Integer> neighbors = i.graph.getNeighbors(a);
            if (neighbors.size() > 0) {
                int nv = getBestNeighbor(neighbors, i.graph);
                int newMaxDel = Math.min(neighbors.size(),
                        i.graph.getNeighbors(nv).size());

                if (newMaxDel > maxDel) {
                    u = a;
                    maxDel = newMaxDel;
                }
            }
        }



        Instance newInstance = new Instance(i.graph.getCopy(), i.k);

        // Optimierung
        Object[] neighb = newInstance.graph.getNeighbors(u).toArray();
        for(Object n : neighb) {
            newInstance.graph.deleteVertex((Integer) n);
        }
        newInstance.graph.deleteVertex(u); // singleton

        // newInstance.graph.deleteVertex(v); taken over by optimization
        removeHighDeg(newInstance);
        removeDegOne(newInstance);
        removeSingletons(newInstance);
        newInstance.k-= neighb.length;
        if (solve(newInstance)) {
            return true;
        }

        newInstance.k = i.k;
        newInstance.graph = i.graph.getCopy();

        newInstance.graph.deleteVertex(u);
        removeHighDeg(newInstance);
        removeDegOne(newInstance);
        removeSingletons(newInstance);
        newInstance.k--;
        return solve(newInstance);
    }

    public int solve(Graph g) {
        for (int k = 0; k < g.size(); k++) {
            if (solve(new Instance(g, k))) {
                return k;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        SearchTree i = new SearchTree();
        Graph g = new MyGraph("data/" + files[2]);
        long start = System.currentTimeMillis();
        int k = i.solve(g);
        long end = System.currentTimeMillis();
        System.out.printf("|V|=%d, |E|=%d, k=%d (took %dms)\n",
                g.getVertices().size(), g.getEdgeCount(), k, end - start);
    }

    final static String[] files = {
            "bio-dmelamtx.sec", "inf-openflightsedges.sec", "inf-USAir97mtx.sec",
            "outarenas-email.sec", "outcontiguous-usa.sec", "outmoreno_zebra_zebra.sec",
            "sample", "soc-brightkitemtx.sec", "ca-sandi_authsmtx.sec",
            "inf-powermtx.sec", "outadjnoun_adjacency_adjacency.sec", "outarenas-jazz.sec",
            "outdolphins.sec", "outucidata-zachary.sec", "sample2"
    };


}