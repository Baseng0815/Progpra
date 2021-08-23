package vertexcover;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class MyGraph implements Graph {
    Map<Integer, Set<Integer>> nodes;

    public MyGraph() {
        this.nodes = new HashMap<>();
    }

    public MyGraph(String filename) throws FileNotFoundException {
        this.nodes = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        Object[] lines = br.lines().toArray();
        for (Object line : lines) {
            /* empty line */
            if (((String) line).trim().equals("")) continue;
            String[] split = ((String) line).split(" ");
            int first = Integer.parseInt(split[0]), second = Integer.parseInt(split[1]);

            addEdge(first, second);
        }
    }

    @java.lang.Override
    public void addVertex(Integer v) {
        nodes.putIfAbsent(v, new HashSet<Integer>());
    }

    @java.lang.Override
    public void addEdge(Integer v, Integer w) {
        /* make sure vertices and adjacency lists are present */
        if (!contains(v))
            addVertex(v);
        if (!contains(w))
            addVertex(w);

        /* no edges to self allowed */
        if (v.compareTo(w) == 0)
            return;

        nodes.get(v).add(w);
        nodes.get(w).add(v);
    }

    @java.lang.Override
    public void deleteVertex(Integer v) {
        /* because undirected graph */
        Set<Integer> pointsToVSet = nodes.get(v);
        for (Integer i : pointsToVSet) {
            nodes.get(i).remove(v);
        }

        nodes.remove(v);
    }

    @java.lang.Override
    public void deleteEdge(Integer u, Integer v) {
        if (!contains(u) || !contains(v))
            return;

        nodes.get(u).remove(v);
        nodes.get(v).remove(u);
    }

    @java.lang.Override
    public boolean contains(Integer v) {
        return nodes.containsKey(v);
    }

    @java.lang.Override
    public int degree(Integer v) {
        if (!contains(v))
            return 0;

        return nodes.get(v).size();
    }

    @java.lang.Override
    public boolean adjacent(Integer v, Integer w) {
        if (!contains(v))
            return false;

        return nodes.get(v).contains(w);
    }

    @java.lang.Override
    public Graph getCopy() {
        MyGraph newGraph = new MyGraph();
        for (Map.Entry<Integer, Set<Integer>> entry : nodes.entrySet()) {
            Set<Integer> newSet = new HashSet<>(entry.getValue());
            newGraph.nodes.put(entry.getKey(), newSet);
        }

        return newGraph;
    }

    @java.lang.Override
    public Set<Integer> getNeighbors(Integer v) {
        if (!contains(v))
            return null;

        return nodes.get(v);
    }

    @java.lang.Override
    public int size() {
        return nodes.size();
    }

    @java.lang.Override
    public int getEdgeCount() {
        int edgeCount = 0;
        for (Set<Integer> set : nodes.values()) {
            edgeCount += set.size();
        }

        /* assuming a correctly constructed graph */
        return edgeCount / 2;
    }

    @java.lang.Override
    public Set<Integer> getVertices() {
        return nodes.keySet();
    }
}
