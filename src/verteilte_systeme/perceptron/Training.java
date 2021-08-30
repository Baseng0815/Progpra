package verteilte_systeme.perceptron;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.util.Collection;
import java.util.Collections;

public class Training {

    private static final double alpha = 0.05; // learning rate
    private static final int epochs = 100; // number of epochs

    /**
     * A perceptron is trained on a dataset. After each epoch the perceptron's
     * parameters are updated, the dataset is shuffled and the accuracy is computed.
     *
     * @param perceptron
     * @param dataset
     */
    private static void train(Perceptron perceptron, Dataset dataset) {
        Visualization visualization = new Visualization(dataset, perceptron.w, perceptron.b);
        for (int epoch = 0; epoch < epochs; epoch++) {
            Perceptron newModel = new Perceptron(perceptron);
            for (DataPoint dataPoint : dataset) {
                int predicted = newModel.predict(dataPoint);
                if (predicted != dataPoint.getLabel()) {
                    newModel.w = newModel.w.add(dataPoint.mult(alpha * (dataPoint.getLabel() - predicted)));
                    newModel.b += alpha * (dataPoint.getLabel() - predicted);
                }
            }

            if (Evaluation.accuracy(newModel, dataset) > Evaluation.accuracy(perceptron, dataset)) {
                perceptron = newModel;
            }

            if (epoch % 9 == 0) {
                visualization.update(perceptron.w, perceptron.b, epoch);
            }
            System.out.println("accuracy=" + Evaluation.accuracy(perceptron, dataset));
            Collections.shuffle(dataset);
        }
    }

    public static void main(String[] args) {
        Dataset dataset = new Dataset(1000);
        Perceptron perceptron = new Perceptron();
        train(perceptron, dataset);
    }

}
