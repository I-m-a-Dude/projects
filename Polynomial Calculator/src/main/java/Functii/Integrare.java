package Functii;

import Polinom.Polinom;

public class Integrare {
    public static Polinom integrare(Polinom polynomial) {
        Polinom integral = new Polinom();
        for (int exponent : polynomial.getExponent()) {
            double coeficient = polynomial.getCoeficient(exponent);
            double newCoeficient = coeficient / (exponent + 1);
            int newExponent = exponent + 1;
            integral.addTerm(newCoeficient, newExponent);
        }
        return integral;
    }

}

