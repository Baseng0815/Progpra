package plt.basic;

import javax.naming.OperationNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;

public class BasicRPNCalculatorImpl implements BasicRPNCalculator, BasicRPNConverter {
    private final Map<Character, BiFunction<Double, Double, Double>> operators;

    public BasicRPNCalculatorImpl() {
        this.operators = new HashMap<>();
        operators.put('+', (x, y) -> x + y);
        operators.put('-', (x, y) -> x - y);
        operators.put('*', (x, y) -> x * y);
        operators.put('/', (x, y) -> x / y);
        operators.put('%', (x, y) -> x % y);
        operators.put('^', (x, y) -> Math.pow(x, y));
    }

    @Override
    public double evaluate(String expression) throws Exception {
        return evaluate(expression, null);
    }

    @Override
    public double evaluate(String expression, Map<String, Double> variableAssignments) throws Exception {
        Stack<Double> values = new Stack<>();

        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            try {
                /* token is a double */
                values.push(Double.parseDouble(token));
            } catch (NumberFormatException e) {
                /* token is an operator */
                if (operators.containsKey(token.charAt(0))) {
                    double y = values.pop();
                    double x = values.pop();
                    values.push(operators.get(token.charAt(0)).apply(x, y));
                } else {
                    /* token is a variable */
                    Double variable = variableAssignments.get(token);
                    if (variable == null)
                        throw new Exception("Variable not found.");
                    values.push(variable);
                }
            }
        }

        return values.pop();
    }

    @Override
    public String convertRPNToInfix(String expression) {
        Stack<String> values = new Stack<>();

        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            if (token.length() == 1 && operators.containsKey(token.charAt(0))) {
                /* token is an operator */
                char op = token.charAt(0);
                String y = values.pop();
                String x = values.pop();
                values.push(String.format("(%s %s %s)", x, op, y));
            } else {
                /* token is a value */
                values.push(token);
            }
        }

        return values.pop();
    }
}
