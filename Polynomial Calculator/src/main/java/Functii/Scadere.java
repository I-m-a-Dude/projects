package Functii;

import Polinom.Polinom;


public class Scadere {
    public static Polinom scadere(Polinom p1, Polinom p2) {
        Polinom p3 = new Polinom();
        for (int exponent : p2.polinomMap.keySet()) {
            double coeficient = p2.polinomMap.get(exponent);
            p3.addTerm(-(coeficient), exponent);
        }
        return Adunare.adunare(p1, p3);
    }

}
