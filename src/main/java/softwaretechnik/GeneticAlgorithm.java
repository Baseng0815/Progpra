package softwaretechnik;

import softwaretechnik.framework.model.*;
import softwaretechnik.framework.operators.EvolutionException;
import softwaretechnik.framework.operators.EvolutionaryOperator;
import softwaretechnik.framework.operators.FitnessEvaluator;
import softwaretechnik.framework.operators.SelectionOperator;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    private final Problem problem;
    private final int populationSize;
    private final List<EvolutionaryOperator> evolutionaryOperators;
    private final FitnessEvaluator fitnessEvaluator;
    private final SelectionOperator selectionOperator;
    private final int iterationCount;

    private GeneticAlgorithm(Problem problem, int populationSize,
                            List<EvolutionaryOperator> evolutionaryOperators,
                            FitnessEvaluator fitnessEvaluator, SelectionOperator selectionOperator,
                            int iterationCount) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.evolutionaryOperators = evolutionaryOperators;
        this.fitnessEvaluator = fitnessEvaluator;
        this.selectionOperator = selectionOperator;
        this.iterationCount = iterationCount;
    }

    public static class ProblemSpecified {
        private final Problem problem;
        private ProblemSpecified(Problem problem) {
            this.problem = problem;
        }

        public class PopulationSizeSpecified {
            private final int populationSize;
            private PopulationSizeSpecified(int populationSize) {
                this.populationSize = populationSize;
            }

            public class EvolutionaryOperatorsSpecified {
                private final List<EvolutionaryOperator> evolutionaryOperators;
                private EvolutionaryOperatorsSpecified(List<EvolutionaryOperator> evolutionaryOperators) {
                    this.evolutionaryOperators = evolutionaryOperators;
                }

                public class FitnessEvaluatorSpecified {
                    private final FitnessEvaluator fitnessEvaluator;
                    private FitnessEvaluatorSpecified(FitnessEvaluator fitnessEvaluator) {
                        this.fitnessEvaluator = fitnessEvaluator;
                    }

                    public class SelectionOperatorSpecified {
                        private final SelectionOperator selectionOperator;
                        private SelectionOperatorSpecified(SelectionOperator selectionOperator) {
                            this.selectionOperator = selectionOperator;
                        }

                        public GeneticAlgorithm stoppingAtEvolution(int iterationCount) {
                            return new GeneticAlgorithm(problem, populationSize, evolutionaryOperators,
                                    fitnessEvaluator, selectionOperator, iterationCount);
                        }
                    }

                    public SelectionOperatorSpecified performingSelectionWith(SelectionOperator selectionOperator) {
                        return new SelectionOperatorSpecified(selectionOperator);
                    }
                }

                public EvolutionaryOperatorsSpecified evolvingSolutionsWith(EvolutionaryOperator evolutionaryOperator) {
                    evolutionaryOperators.add(evolutionaryOperator);
                    return new EvolutionaryOperatorsSpecified(evolutionaryOperators);
                }

                public FitnessEvaluatorSpecified evaluatingSolutionsBy(FitnessEvaluator fitnessEvaluator) {
                    return new FitnessEvaluatorSpecified(fitnessEvaluator);
                }
            }

            public EvolutionaryOperatorsSpecified evolvingSolutionsWith(EvolutionaryOperator evolutionaryOperator) {
                List<EvolutionaryOperator> evolutionaryOperators = new ArrayList<>();
                evolutionaryOperators.add(evolutionaryOperator);
                return new EvolutionaryOperatorsSpecified(evolutionaryOperators);
            }
        }

        public PopulationSizeSpecified withPopulationOfSize(int populationOfSize) {
            return new PopulationSizeSpecified(populationOfSize);
        }
    }

    public static ProblemSpecified solve(Problem problem) {
        return new ProblemSpecified(problem);
    }

    public List<Solution> runOptimization() {
        List<Solution> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            try {
                population.add(problem.createNewSolution());
            } catch (NoSolutionException e) {
                System.err.println("No initial solution could be found: " + e);
                return null;
            }
        }

        for (int iteration = 0; iteration < iterationCount; iteration++) {
            EvolutionaryOperator operator = evolutionaryOperators.get((int) (Math.random() * evolutionaryOperators.size()));
            try {
                List<Solution> evolvedPopulation = operator.evolvePopulation(population);
                fitnessEvaluator.evaluate(evolvedPopulation);
                population = selectionOperator.selectPopulation(evolvedPopulation, populationSize);
            } catch (EvolutionException e) {
                System.err.println("Error in evolution step: " + e);
                return null;
            }
        }

        return population;
    }
}
