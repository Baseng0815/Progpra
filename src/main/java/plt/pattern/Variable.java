package plt.pattern;

import java.util.Map;

public class Variable implements Expression {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(Map<String, Double> variableMapping) {
        Double value = variableMapping.get(name);
        if (value == null)
            throw new ArithmeticException("Variable '" + name + "' could not be found.");
        return value;
    }

    @Override
    public String toInfixExpression() {
        return name;
    }
}
