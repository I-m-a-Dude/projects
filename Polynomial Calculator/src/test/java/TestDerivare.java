import Functii.Derivare;
import Polinom.Polinom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDerivare {

    @Test
    public void testDerivare() {
        Polinom p1 = new Polinom();
        p1.addTerm(2, 3);
        p1.addTerm(2, 1);
        p1.addTerm(5, 0);

        Polinom expected = new Polinom();
        expected.addTerm(6, 2);
        expected.addTerm(2, 0);

        Polinom result = Derivare.derivare(p1);

        assertEquals(expected, result);
    }
}


