package algorithmik;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String[] files = {
                "bio-dmelamtx.sec", "inf-openflightsedges.sec", "inf-USAir97mtx.sec",
                "outarenas-email.sec", "outcontiguous-usa.sec", "outmoreno_zebra_zebra.sec",
                "sample", "soc-brightkitemtx.sec", "ca-sandi_authsmtx.sec",
                "inf-powermtx.sec", "outadjnoun_adjacency_adjacency.sec", "outarenas-jazz.sec",
                "outdolphins.sec", "outucidata-zachary.sec", "sample2"
        };

        Graph g = new MyGraph("Aufgaben/Data/" + files[3]);
        SearchTree i = new SearchTree(g);
        long start = System.currentTimeMillis();
        int k = i.solve(g);
        long end = System.currentTimeMillis();
        long dtMs = end - start;
        System.out.printf("|V|=%d, |E|=%d, k=%d (took %dms)\n", g.size(), g.getEdgeCount(), k, dtMs);
    }
}
