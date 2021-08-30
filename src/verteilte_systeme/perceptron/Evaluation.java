package verteilte_systeme.perceptron;

public class Evaluation {

	/**
	 * Applies the model to each data vector in the dataset and computes the
	 * accuracy.
	 * 
	 * @return accuracy
	 */
	public static double accuracy(Perceptron model, Dataset dataset) {
		int correct = 0;
		for (DataPoint dataPoint : dataset) {
			int predicted = model.predict(dataPoint);
			if (predicted == dataPoint.getLabel()) {
				correct++;
			}
		}

		return correct / (double)dataset.size();
	}

}
