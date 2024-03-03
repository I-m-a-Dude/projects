package org.example;

import Polinom.Polinom;

public class Demo_test {
    public static void main(String[] args) {
        // System.out.println("Hello world!");

        //ia hai un demo, daca pusca aia e, daca nu e foarte bine
        // Monomial m1 = new Monomial(2,3);
        //Monomial m2 = new Monomial(3,2);
        // Polinom p= new Polinom();
        Polinom p1 = new Polinom();
        Polinom p2 = new Polinom();
        p1.addTerm(2, 3);
        p1.addTerm(2, 1);
        p1.addTerm(5, 0);

        p2.addTerm(3, 2);
        p2.addTerm(4, 1);
        p2.addTerm(-3, 0);


        String input = "3x^2+2x-5";
        String input2 = "2x^3+3x^2+4x";
       /* Polinom polinom1 = Polinom.fromString(input);
        Polinom polinom2 = Polinom.fromString(input2);



        Polinom demo_add = Adunare.adunare(polinom1,polinom2);
        Polinom demo_sub = Scadere.scadere(polinom1,polinom2);
        Polinom demo_inmultire = Inmultire.inmultire(polinom1,polinom2);
        Polinom demo_derivare = Derivare.derivare(polinom1);
        Polinom demo_integrare = Integrare.integrare(polinom1);

      // Polinom demo_divide= Impartire.divide(p1,p2);

        //System.out.println(m1);  //ar fi 2.0x^3
        //System.out.println(m2);  //ar fi 3x^2
       //System.out.println(m1);  //ar fi 2x^3
        //System.out.println(p);  //ar fi 2x^3+3x^2+4x

        //System.out.println(p1); //2.0x^3-2.0x+5.0
        //System.out.println(p2); //3.0x^2+4.0x-3.0

       System.out.println(demo_add); //ar fi 2.0x^3+3.0x^2+2x.0+2.0
        System.out.println(demo_sub); //ar fi 2.0x^3-3.0x^2-2x.0+8
        System.out.println(demo_inmultire); //ar fi 6.0x^5+8.0x^4-12.0x^3+7.0x^2+26.0x-15.0
        //System.out.println(demo_divide);
        System.out.println(demo_derivare); //ar fi 6.0x^2-2.0
        System.out.println(demo_integrare); //ar fi 0.5x^4-1.0x^2+5.0x+C */

    }
}