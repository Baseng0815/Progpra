package plt.pattern;

import java.util.Map;

public interface Expression {

    /**
     * Evaluates this expression according to the given variable mappings.
     * @return the result of the expression.
     */
    public double evaluate(Map<String, Double> variableMapping);

    /**
     * Returns a String containing the infix representation of this expression.
     * @return the infix representation of this expression.
     */
    public String toInfixExpression();
}
