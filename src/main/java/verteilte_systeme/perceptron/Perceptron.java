package verteilte_systeme.perceptron;

import java.util.Arrays;
import java.util.Random;

/**
 * A Perceptron holds weights and bias and can be applied to a data vector to
 * predict its class. Weights and bias are initialized randomly.
 */
public class Perceptron {
    public Vector w; /* weight vector */
    public double b; /* bias */

    public Perceptron() {
        double[] weights = new double[2];
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble();
        }

        w = new Vector(weights);
        b = random.nextDouble();
    }

    public Perceptron(Perceptron p) {
        double[] weights = new double[2];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = p.w.getDim(i);
        }

        w = new Vector(weights);
        b = p.b;
    }

    public int predict(Vector input) {
        double sum = input.dot(w) + b;
        if (sum > 0) return 1;
        else return 0;
    }
}
