package plt.basic;

import java.util.Map;

public interface BasicRPNCalculator {

    /**
     * Evaluates an arithmetic expression in reverse Polish notation and returns the result.
     * @param expression the RPN expression.
     * @return the result of the arithmetic expression.
     */
    public double evaluate(String expression) throws Exception;

    /**
     * Evaluates an arithmetic expression in reverse Polish notation and returns the result.
     * The expression may contain variables, which have to be set via the given map.
     * @param expression the RPN expression.
     * @param variableAssignments the variable mappings.
     * @return the result of the arithmetic expression.
     */
    public double evaluate(String expression, Map<String, Double> variableAssignments) throws Exception;

}
