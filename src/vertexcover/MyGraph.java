package vertexcover;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyGraph implements Graph {
    Map<Integer, Set<Integer>> nodes = new HashMap<>();
    int edgeCount = 0;

    private MyGraph() { }

    public MyGraph(String filename) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        Map<String, Integer> mappings = new HashMap<>();
        int top = 0;

        Object[] lines = br.lines().toArray();
        for (Object line : lines) {
            /* empty line */
            if (((String) line).trim().equals("")) {
                continue;
            }
            String[] split = ((String) line).split("\t|\s+");
            String first = split[0], second = split[1];

            if (!mappings.containsKey(first)) {
                mappings.put(first, top);
                addVertex(top);
                top++;
            }
            if (!mappings.containsKey(second)) {
                mappings.put(second, top);
                addVertex(top);
                top++;
            }

            addEdge(mappings.get(first), mappings.get(second));
        }
    }

    @Override
    public void addVertex(Integer v) {
        nodes.putIfAbsent(v, new HashSet<>());
    }

    @Override
    public void addEdge(Integer v, Integer w) {
        nodes.get(v).add(w);
        nodes.get(w).add(v);
        edgeCount++;
    }

    @Override
    public void deleteVertex(Integer v) {
        /* because undirected graph */
        Set<Integer> pointsToVSet = nodes.get(v);
        for (Integer i : pointsToVSet) {
            nodes.get(i).remove(v);
            edgeCount--;
        }
        nodes.remove(v);
    }

    @Override
    public void deleteEdge(Integer u, Integer v) {
        nodes.get(u).remove(v);
        nodes.get(v).remove(u);
        edgeCount--;
    }

    @Override
    public boolean contains(Integer v) {
        return nodes.containsKey(v);
    }

    @Override
    public int degree(Integer v) {
        return nodes.get(v).size();
    }

    @Override
    public boolean adjacent(Integer v, Integer w) {
        return nodes.get(v).contains(w);
    }

    @Override
    public Graph getCopy() {
        MyGraph newGraph = new MyGraph();
        for (Map.Entry<Integer, Set<Integer>> entry : nodes.entrySet()) {
            Set<Integer> newSet = (Set<Integer>) ((HashSet<Integer>) entry.getValue()).clone();
            newGraph.nodes.put(entry.getKey(), newSet);
        }

        newGraph.edgeCount = edgeCount;
        return newGraph;
    }

    @Override
    public Set<Integer> getNeighbors(Integer v) {
        return nodes.get(v);
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @Override
    public Set<Integer> getVertices() {
        return nodes.keySet();
    }
}
