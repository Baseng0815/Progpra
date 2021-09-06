package plt.basic;

public interface BasicRPNConverter {

    /**
     * Converts a given arithmetic expression in reverse Polish notation to an equivalent expression in infix notation.
     * @param expression the RPN expression to be converted.
     * @return the equivalent expression in infix notation.
     */
    public String convertRPNToInfix(String expression);

}
