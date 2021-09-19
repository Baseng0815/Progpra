package softwaretechnik;

import softwaretechnik.framework.model.Solution;
import softwaretechnik.framework.operators.SelectionOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TOPKSelection implements SelectionOperator {
    @Override
    public List<Solution> selectPopulation(List<Solution> candidates, int populationSize) {
        return candidates.stream()
                .sorted(Comparator.comparingDouble(Solution::getFitness))
                .limit(populationSize)
                .toList();
    }
}
