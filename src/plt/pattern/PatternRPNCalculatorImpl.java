package plt.pattern;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;

public class PatternRPNCalculatorImpl implements PatternRPNCalculator {
    private final Map<Character, Class<? extends Operator>> operators;

    public PatternRPNCalculatorImpl() {
        this.operators = new HashMap<>();
        operators.put('+', OperatorAdd.class);
        operators.put('-', OperatorSub.class);
        operators.put('*', OperatorMul.class);
        operators.put('/', OperatorDiv.class);
        operators.put('%', OperatorMod.class);
        operators.put('^', OperatorPow.class);
    }
    @Override
    public double evaluate(String expression, Map<String, Double> variableAssignments) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Expression e = convertStringToExpression(expression);
        return e.evaluate(variableAssignments);
    }

    @Override
    public Expression convertStringToExpression(String expression) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Stack<Expression> expressions = new Stack<>();

        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            try {
                /* token is a double */
                expressions.push(new Number(Double.parseDouble(token)));
            } catch (NumberFormatException e) {
                if (operators.containsKey(token.charAt(0))) {
                    /* token is an operator */
                    Expression right = expressions.pop();
                    Expression left = expressions.pop();
                    expressions.push(operators.get(token.charAt(0))
                            .getDeclaredConstructor(Expression.class, Expression.class)
                            .newInstance(left, right));
                } else {
                    /* token is a variable */
                    expressions.push(new Variable(token));
                }
            }
        }

        return expressions.pop();
    }
}
