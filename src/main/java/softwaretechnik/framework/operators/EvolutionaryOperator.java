package softwaretechnik.framework.operators;

import java.util.List;

import softwaretechnik.framework.model.Solution;

public interface EvolutionaryOperator {
	public List<Solution> evolvePopulation(List<Solution> population)
			throws EvolutionException;
}
