package plt.pattern;

import java.util.Map;

public class OperatorPow extends Operator {
    public OperatorPow(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variableMapping) {
        return Math.pow(left.evaluate(variableMapping), right.evaluate(variableMapping));
    }

    @Override
    public String getOperatorSymbol() {
        return "^";
    }
}
