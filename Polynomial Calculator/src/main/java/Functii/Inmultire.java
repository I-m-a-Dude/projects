package Functii;

import Polinom.Polinom;

public class Inmultire {
    public static Polinom inmultire(Polinom p1, Polinom p2) {
        Polinom p3 = new Polinom();
        for (int exp1 : p1.polinomMap.keySet()) {
            double coef1 = p1.polinomMap.get(exp1);
            for (int exp2 : p2.polinomMap.keySet()) {
                double coef2 = p2.polinomMap.get(exp2);
                double c_final = coef1 * coef2;
                int e_final = exp1 + exp2;
                p3.addTerm(c_final, e_final);
            }
        }
        return p3;
    }
}
