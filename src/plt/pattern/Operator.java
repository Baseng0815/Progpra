package plt.pattern;

import java.util.Map;

public abstract class Operator implements Expression {
    Expression left;
    Expression right;

    public Operator(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toInfixExpression() {
        return String.format("(%s %s %s)",
        left.toInfixExpression(), getOperatorSymbol(), right.toInfixExpression());
    }

    public abstract String getOperatorSymbol();
}
