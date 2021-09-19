package plt.pattern;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface PatternRPNCalculator {

    /**
     * Evaluates an arithmetic expression in reverse Polish notation and returns the result.
     * The expression may contain variables, which have to be set via the given map.
     * @param expression the RPN expression.
     * @param variableAssignments the variable mappings.
     * @return the result of the arithmetic expression.
     */
    public double evaluate(String expression, Map<String, Double> variableAssignments) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    /**
     * Converts a string containing an arithmetic expression in reverse Polish notation to a tree of Expression objects.
     * @return the root Expression object.
     */
    public Expression convertStringToExpression(String expression) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
