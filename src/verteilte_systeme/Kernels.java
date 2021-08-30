package verteilte_systeme;

/**
 * Holds some kernels which can be convolved with an image.
 */
public class Kernels {

	/**
	 * Multiplies each element in a 2D matrix with a scalar.
	 * 
	 * @param m 2D array
	 * @param v
	 * @return
	 */
	private static double[][] multiplyElementwise(double[][] m, double v) {
		for (int x = 0; x < m[0].length; x++)
			for (int y = 0; y < m.length; y++)
				m[x][y] *= v;
		return m;
	}		

	public static Kernel GaussianBlur5x5() {
		double[][] m = { 
				{ 1,  4,  6,  4, 1 },
				{ 4, 16, 24, 16, 4 },
				{ 6, 24, 36, 24, 6 },
				{ 4, 16, 24, 16, 4 },
				{ 1,  4,  6,  4, 1 } };		
		m = multiplyElementwise(m, 1. / 256);
		return new Kernel(m);
	}
	
	public static Kernel BoxBlur3x3() {
		return new Kernel(new double[][] { 
				{ 1./9,  1./9,  1./9 }, 
				{ 1./9,  1./9,  1./9 },
				{ 1./9,  1./9,  1./9 } });
	}

	public static Kernel EdgeDetection() {
		return new Kernel(new double[][] { 
				{ -1, -1, -1},
				{ -1,  8, -1},
				{ -1, -1, -1} });
	}

	public static Kernel Sharpen() {
		return new Kernel(new double[][] { 
				{  0, -1,  0 },
				{ -1,  5, -1 },
				{  0, -1,  0 } });
	}
	
	public static Kernel Identity() {
		return new Kernel(new double[][] { 
				{ 0, 0, 0 },
				{ 0, 1, 0 },
				{ 0, 0, 0 } });
	}
	
	public static Kernel Relief() {
		return new Kernel(new double[][] { 
				{ -2, -1, 0 },
				{ -1,  1, 1 },
				{  0,  1, 2 } });
	}
	
	public static Kernel MotionBlur() {
		double[][] m = {
			  {1, 0, 0, 0, 0, 0, 0, 0, 0},
			  {0, 1, 0, 0, 0, 0, 0, 0, 0},
			  {0, 0, 1, 0, 0, 0, 0, 0, 0},
			  {0, 0, 0, 1, 0, 0, 0, 0, 0},
			  {0, 0, 0, 0, 1, 0, 0, 0, 0},
			  {0, 0, 0, 0, 0, 1, 0, 0, 0},
			  {0, 0, 0, 0, 0, 0, 1, 0, 0},
			  {0, 0, 0, 0, 0, 0, 0, 1, 0},
			  {0, 0, 0, 0, 0, 0, 0, 0, 1}
		};
		m = multiplyElementwise(m, 1. / 9);
		return new Kernel(m);
	}
}
