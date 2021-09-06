package plt.pattern;

import java.util.Map;

public class OperatorAdd extends Operator {
    public OperatorAdd(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variableMapping) {
        return left.evaluate(variableMapping) + right.evaluate(variableMapping);
    }

    @Override
    public String getOperatorSymbol() {
        return "+";
    }
}
