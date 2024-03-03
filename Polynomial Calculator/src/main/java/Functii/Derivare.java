package Functii;

import Polinom.Polinom;

public class Derivare {
    public static Polinom derivare(Polinom p) {
        Polinom derivata = new Polinom();
        for (int exponent : p.getExponent()) {
            double coeficient = p.getCoeficient(exponent);
            if (exponent > 0) {
                double coeficient_nou = coeficient * exponent;
                int exponent_nou = exponent - 1;
                derivata.addTerm(coeficient_nou, exponent_nou);
            }
        }
        return derivata;
    }
}
