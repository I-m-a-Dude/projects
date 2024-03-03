package Functii;

import Polinom.Polinom;


public class Adunare {
    public static Polinom adunare(Polinom p1, Polinom p2) {
        Polinom p3 = new Polinom();
        for (int exponent : p1.polinomMap.keySet()) {
            double coefficient = p1.polinomMap.get(exponent);
            p3.addTerm(coefficient, exponent);
        }
        for (int exponent : p2.polinomMap.keySet()) {
            double coefficient = p2.polinomMap.get(exponent);
            p3.addTerm(coefficient, exponent);
        }
        return p3;
    }

}
