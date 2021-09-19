package plt.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BasicRPNCalculatorImplTest {
    @Test
    void evaluate() throws Exception {
        BasicRPNCalculatorImpl impl = new BasicRPNCalculatorImpl();
        Assertions.assertEquals(3, impl.evaluate("1 2 +"));
        Map<String, Double> variables = new HashMap<>();
        variables.put("m", 3.0);
        variables.put("c", 4.0);
        Assertions.assertEquals(162, impl.evaluate("m c ^ 2 *", variables));
        Assertions.assertEquals(422.5, impl.evaluate("42 23 + 13 * 2 /"));
        Assertions.assertEquals(4, impl.evaluate("-1 -2 - 1 2 + +"));
    }

    @Test
    void convertRPNToInfix() {
        BasicRPNCalculatorImpl impl = new BasicRPNCalculatorImpl();
        Assertions.assertEquals("(1 + 2)", impl.convertRPNToInfix("1 2 +"));
        Assertions.assertEquals("((m ^ c) * 2)", impl.convertRPNToInfix("m c ^ 2 *"));
        Assertions.assertEquals("(((42 + 23) * 13) / 2)", impl.convertRPNToInfix("42 23 + 13 * 2 /"));
        Assertions.assertEquals("((-1 - -2) + (1 + 2))", impl.convertRPNToInfix("-1 -2 - 1 2 + +"));
    }
}