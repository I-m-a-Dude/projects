import Functii.Inmultire;
import Polinom.Polinom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestInmultire {

    @Test
    public void testInmultire() {
        Polinom p1 = new Polinom();
        p1.addTerm(2, 3);
        p1.addTerm(2, 1);
        p1.addTerm(5, 0);

        Polinom p2 = new Polinom();
        p2.addTerm(3, 2);
        p2.addTerm(4, 1);
        p2.addTerm(-3, 0);

        Polinom expected = new Polinom();
        expected.addTerm(6, 5);
        expected.addTerm(12, 4);
        expected.addTerm(4, 3);
        expected.addTerm(8, 2);
        expected.addTerm(-15, 1);
        expected.addTerm(-9, 0);

        Polinom result = Inmultire.inmultire(p1, p2);

        assertEquals(expected, result);
    }
}
