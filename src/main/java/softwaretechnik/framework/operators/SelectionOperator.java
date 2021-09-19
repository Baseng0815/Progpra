package softwaretechnik.framework.operators;

import softwaretechnik.framework.model.Solution;

import java.util.List;

public interface SelectionOperator {
	public List<Solution> selectPopulation(List<Solution> candidates, int populationSize);
}
