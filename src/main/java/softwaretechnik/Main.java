package softwaretechnik;

public class Main {
    public static void main(String[] args) {
        GeneticAlgorithm ga = GeneticAlgorithm.solve(null)
                .withPopulationOfSize(10)
                .evolvingSolutionsWith(null)
                .evolvingSolutionsWith(null)
                .evolvingSolutionsWith(null)
                .evaluatingSolutionsBy(null)
                .performingSelectionWith(null)
                .stoppingAtEvolution(100);

        ga.runOptimization();
    }
}
