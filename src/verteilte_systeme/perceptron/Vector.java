package verteilte_systeme.perceptron;

public class Vector {

	private double[] p;

	/**
	 * Creates a vector with a minimum dimension of 2.
	 * 
	 * @param p values
	 */
	public Vector(double... p) {
		if (p.length < 2)
			throw new IllegalArgumentException("Minimum dimension is 2");
		this.p = p;
	}

	public double getX() {
		return p[0];
	}

	public double getY() {
		return p[1];
	}

	public double getDim(int d) {
		if (d >= p.length)
			throw new IllegalArgumentException("Maximum dimension is " + d);
		return p[d];
	}

	public int numDims() {
		return p.length;
	}

	/**
	 * Computes the dot product between the vector and another one.
	 * 
	 * @param v A vector of the same dimension
	 * @return The dot product between the two vectors
	 */
	public double dot(Vector v) {
		double sum = 0;
		for (int i = 0; i < numDims(); i++) {
			sum += this.getDim(i) * v.getDim(i);
		}

		return sum;
	}

	/**
	 * Adds the vector to another one.
	 * 
	 * @param v A vector of the same dimension
	 * @return A new vector
	 */
	public Vector add(Vector v) {
		double[] sum = new double[numDims()];
		for (int i = 0; i < numDims(); i++) {
			sum[i] = getDim(i) + v.getDim(i);
		}

		return new Vector(sum);
	}

	/**
	 * Multiplies the vector with a scalar.
	 * 
	 * @param s A scalar
	 * @return A new vector
	 */
	public Vector mult(double s) {
		double[] scaled = new double[numDims()];
		for (int i = 0; i < numDims(); i++) {
			scaled[i] = getDim(i) * s;
		}

		return new Vector(scaled);
	}

}
