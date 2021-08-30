package verteilte_systeme;

import org.junit.jupiter.api.Assertions;

class KernelTest {
    @org.junit.jupiter.api.Test
    void convolve() {
        int[][] gray = new int[][] {
                {1, 1, 1},
                {0, 1, 1},
                {0, 0, 1}};
        Kernel kernel = new Kernel(new double[][] {
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        });

        int[][] result = kernel.convolve(gray);
        /* will fail because it doesn't take image padding into account! */
        Assertions.assertEquals(4, result[0][0]);
        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(1, result[0].length);
    }
}