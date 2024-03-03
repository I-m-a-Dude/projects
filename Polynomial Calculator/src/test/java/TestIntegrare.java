import Functii.Integrare;
import Polinom.Polinom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestIntegrare {

    @Test
    public void testIntegrare() {
        Polinom p1 = new Polinom();
        p1.addTerm(2, 3);
        p1.addTerm(2, 1);
        p1.addTerm(5, 0);

        Polinom expected = new Polinom();
        expected.addTerm(0.5, 4);
        expected.addTerm(1, 2);
        expected.addTerm(5, 1);

        Polinom result = Integrare.integrare(p1);

        assertEquals(expected, result);
    }
}
