package softwaretechnik.framework.operators;

import java.util.List;

import softwaretechnik.framework.model.Solution;

public interface FitnessEvaluator {
	public void evaluate(List<Solution> population);
}
