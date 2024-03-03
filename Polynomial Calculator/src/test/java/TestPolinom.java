import Polinom.Polinom;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPolinom {

    @Test
    public void testAddTerm() {
        Polinom p = new Polinom();
        p.addTerm(2.0, 3);
        p.addTerm(3.0, 2);
        p.addTerm(4.0, 1);
        p.addTerm(5.0, 0);
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(3, 2.0);
        expectedMap.put(2, 3.0);
        expectedMap.put(1, 4.0);
        expectedMap.put(0, 5.0);
        assertEquals(expectedMap, p.polinomMap);
    }

    @Test
    public void testGetCoeficient() {
        Polinom p = new Polinom();
        p.addTerm(2.0, 3);
        p.addTerm(3.0, 2);
        p.addTerm(4.0, 1);
        p.addTerm(5.0, 0);
        assertEquals(2.0, p.getCoeficient(3), 0.0001);
        assertEquals(3.0, p.getCoeficient(2), 0.0001);
        assertEquals(4.0, p.getCoeficient(1), 0.0001);
        assertEquals(5.0, p.getCoeficient(0), 0.0001);
        assertEquals(0.0, p.getCoeficient(4), 0.0001);
    }

    @Test
    public void testGetDegree() {
        Polinom p = new Polinom();
        assertEquals(-1, p.getDegree());
        p.addTerm(2.0, 3);
        assertEquals(3, p.getDegree());
        p.addTerm(3.0, 5);
        assertEquals(5, p.getDegree());
    }

    @Test
    public void testGetExponent() {
        Polinom p = new Polinom();
        p.addTerm(2.0, 3);
        p.addTerm(3.0, 2);
        p.addTerm(4.0, 1);
        p.addTerm(5.0, 0);
        assertEquals(4, p.getExponent().size());
        assertTrue(p.getExponent().contains(3));
        assertTrue(p.getExponent().contains(2));
        assertTrue(p.getExponent().contains(1));
        assertTrue(p.getExponent().contains(0));
    }

    @Test
    public void testFromString() {
        Polinom p = Polinom.fromString("2.0x^3+3x^2-4.5x-5");
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(3, 2.0);
        expectedMap.put(2, 3.0);
        expectedMap.put(1, -4.5);
        expectedMap.put(0, -5.0);
        assertEquals(expectedMap, p.polinomMap);
    }


    @Test
    public void testToString() {
        Polinom p1 = new Polinom();
        p1.addTerm(3.0, 2);
        p1.addTerm(2.0, 1);
        p1.addTerm(1.0, 0);
        assertEquals("3.0x^2+2.0x+1.0", p1.toString());
    }
}
