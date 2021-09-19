package plt.pattern;

import java.util.Map;

public class Number implements Expression {
    private double value;

    public Number(double value) {
        this.value = value;
    }

    public Number(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public double evaluate(Map<String, Double> variableMapping) {
        return value;
    }

    @Override
    public String toInfixExpression() {
        return String.valueOf(value);
    }
}
