package vertexcover;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyGraphOptimized implements Graph {
    private Map<Integer, Set<Integer>> nodes;
    private int edgeCount, vertCount;

    public MyGraphOptimized() {
        this.nodes = new HashMap<>();
        this.edgeCount = 0;
        this.vertCount = 0;
    }

    public MyGraphOptimized(String filename) throws FileNotFoundException {
        this.nodes = new HashMap<>();
        this.edgeCount = 0;
        this.vertCount = 0;

        BufferedReader br = new BufferedReader(new FileReader(filename));
        Object[] lines = br.lines().toArray();
        for (Object line : lines) {
            if (((String) line).trim().equals("")) continue;
            String[] split = ((String) line).split(" ");
            int first = Integer.parseInt(split[0]), second = Integer.parseInt(split[1]);

            addEdge(first, second);
        }
    }

    @java.lang.Override
    public void addVertex(Integer v) {
        nodes.put(v, new HashSet<Integer>());
        vertCount++;
    }

    @java.lang.Override
    public void addEdge(Integer v, Integer w) {
        /* make sure vertices and adjacency lists are present */
        /* performance */
        if (!contains(v))
            addVertex(v);
        if (!contains(w))
            addVertex(w);

        nodes.get(v).add(w);
        nodes.get(w).add(v);
        edgeCount++;
    }

    @java.lang.Override
    public void deleteVertex(Integer v) {
        /* because undirected graph */
        Set<Integer> pointsToVSet = nodes.get(v);
        for (Integer i : pointsToVSet) {
            if (nodes.get(i).remove(v))
                edgeCount--;
        }

        if (nodes.remove(v) != null)
            vertCount--;
    }

    @java.lang.Override
    public void deleteEdge(Integer u, Integer v) {
        if (nodes.get(u).remove(v))
            edgeCount--;
        nodes.get(v).remove(u);
    }

    @java.lang.Override
    public boolean contains(Integer v) {
        return nodes.containsKey(v);
    }

    @java.lang.Override
    public int degree(Integer v) {
        return nodes.get(v).size();
    }

    @java.lang.Override
    public boolean adjacent(Integer v, Integer w) {
        return nodes.get(v).contains(w);
    }

    @java.lang.Override
    public Graph getCopy() {
        MyGraphOptimized newGraph = new MyGraphOptimized();
        for (Map.Entry<Integer, Set<Integer>> entry : nodes.entrySet()) {
            Set<Integer> newSet = new HashSet<>(entry.getValue());
            newGraph.nodes.put(entry.getKey(), newSet);
        }

        return newGraph;
    }

    @java.lang.Override
    public Set<Integer> getNeighbors(Integer v) {
        return nodes.get(v);
    }

    @java.lang.Override
    public int size() {
        return vertCount;
    }

    @java.lang.Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @java.lang.Override
    public Set<Integer> getVertices() {
        return nodes.keySet();
    }
}
