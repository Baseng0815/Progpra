package vertexcover;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SearchTree i = new SearchTree();
        Graph g = new MyGraph("Aufgaben/Data/outadjnoun_adjacency_adjacency.sec");
        long start = System.currentTimeMillis();
        int k = i.solve(g);
        long end = System.currentTimeMillis();
        long dtMs = end - start;
        System.out.printf("|V|=%d, |E|=%d, k=%d (took %dms)\n", g.getVertices().size(), g.getEdgeCount(), k, dtMs);
    }
}