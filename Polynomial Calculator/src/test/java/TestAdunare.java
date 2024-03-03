import Functii.Adunare;
import Polinom.Polinom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestAdunare {

    @Test
    public void testAdunare() {
        Polinom p1 = new Polinom();
        p1.addTerm(2, 3);
        p1.addTerm(2, 1);
        p1.addTerm(5, 0);

        Polinom p2 = new Polinom();
        p2.addTerm(3, 2);
        p2.addTerm(4, 1);
        p2.addTerm(-3, 0);

        Polinom expected = new Polinom();
        expected.addTerm(2, 3);
        expected.addTerm(3, 2);
        expected.addTerm(6, 1);
        expected.addTerm(2, 0);

        Polinom result = Adunare.adunare(p1, p2);

        assertEquals(expected, result);

    }
}

