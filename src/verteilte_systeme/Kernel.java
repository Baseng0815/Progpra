package verteilte_systeme;

/**
 * A kernel is a 2D-array. The array is transposed after initialization which
 * enables a more intuitive way of initializing a kernel. E.g a non-symmetric
 * kernel can be initialized by Kernel({{0,0,1} {0,1,0} {1,0,0}}) although the
 * array dimensions are actually [height][width].
 */
public class Kernel {

    private double[][] k;
    private int height;
    private int width;

    /**
     * Initializes the kernel by its transpose.
     *
     * @param k
     */
    Kernel(double[][] k) {
        // transpose
        this.k = new double[k.length][k[0].length]; // swap
        for (int x = 0; x < k[0].length; x++)
            for (int y = 0; y < k.length; y++)
                this.k[y][x] = k[x][y];
        this.width = this.k.length;
        this.height = this.k[0].length;

        if (this.width % 2 != 1 || this.height % 2 != 1)
            throw new IllegalArgumentException("Kernel size need to be odd-numbered");
        if (this.width < 3 || this.height < 3)
            throw new IllegalArgumentException("Minimum dimension is 3");
    }

    /**
     * Convolves a single-channel image with the kernel.
     *
     * @param img A single-channel image
     * @return The convolved image
     */
    public int[][] convolve(int[][] img) {
        int[][] out = new int[img.length][img[0].length];
        int padX = width / 2;
        int padY = height / 2;

        /* resize image so we get the same size after applying the kernel
        * the strategy is copying the nearest pixel from the existing image if
        we are in an edge with no pixel data */
        int[][] adjusted = new int[img.length + padX * 2][img[0].length + padY * 2];
        {
            /* old image dimensions (pre-adjusting) */
            int owidth = img.length;
            int oheight = img[0].length;
            for (int x = 0; x < adjusted.length; x++) {
                for (int y = 0; y < adjusted[0].length; y++) {
                    /* upper left corner */
                    if (x < padX && y < padY) adjusted[x][y] = img[0][0];
                    /* lower left corner */
                    else if (x < padX && y >= padY + oheight) adjusted[x][y] = img[0][oheight - 1];
                    /* upper right corner */
                    else if (x >= padX + owidth && y < padY) adjusted[x][y] = img[owidth - 1][0];
                    /* lower right corner */
                    else if (x >= padX + owidth && y >= padY + oheight) adjusted[x][y] = img[owidth - 1][oheight - 1];
                    /* left column */
                    else if (x < padX) adjusted[x][y] = img[0][y - padY];
                    /* right column */
                    else if (x >= padX + owidth) adjusted[x][y] = img[owidth - 1][y - padY];
                    /* upper row */
                    else if (y < padY) adjusted[x][y] = img[x - padX][0];
                    /* lower row */
                    else if (y >= padY + oheight) adjusted[x][y] = img[x - padX][oheight - 1];
                    /* dead center */
                    else adjusted[x][y] = img[x - padX][y - padY];
                }
            }
        }

        for (int x = padX; x < adjusted.length - padX; x++) {
            for (int y = padY; y < adjusted[0].length - padY; y++) {
                int sum = 0;
                for (int fx = 0; fx < width; fx++) {
                    for (int fy = 0; fy < height; fy++) {
                        sum += adjusted[fx + x - padX][fy + y - padY] * k[fx][fy];
                    }
                }
                out[x - padX][y - padY] = Math.max(0, Math.min(255, sum));
            }
        }

        return out;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
