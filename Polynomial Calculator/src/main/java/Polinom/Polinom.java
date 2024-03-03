package Polinom;

import java.util.*;

public class Polinom {
    public HashMap<Integer, Double> polinomMap;

    public Polinom() {
        this.polinomMap = new HashMap<>();
    }

    public static Polinom fromString(String s) {
        Polinom polinom = new Polinom();
        String[] terms = s.split("(?=[+-])");
        for (String term : terms) {
            term = term.trim();
            if (term.equals("")) {
                continue;
            }
            double coeficient;
            int exponent;
            if (term.matches("^[+-]?\\d*\\.?\\d*x(\\^\\d+)?$")) {
                // Termenul este de forma ax^n, ax sau x^n
                String[] parts = term.split("x\\^?");
                if (parts.length == 1) {
                    coeficient = (parts[0].equals("+") || parts[0].equals("")) ? 1.0 : Double.parseDouble(parts[0]);
                    exponent = 1;
                } else {
                    coeficient = (parts[0].equals("+") || parts[0].equals("-") || parts[0].equals("")) ? Double.parseDouble(parts[0] + "1") : Double.parseDouble(parts[0]);
                    exponent = Integer.parseInt(parts[1]);
                }
            } else if (term.matches("^[+-]?\\d+\\.?\\d*$")) {
                // Termenul e o constanta
                coeficient = Double.parseDouble(term);
                exponent = 0;
            } else {
                throw new IllegalArgumentException("Invalid term: " + term);
            }
            polinom.addTerm(coeficient, exponent);
        }
        return polinom;
    }

    public void addTerm(double coeficient, int exponent) {
        for (Map.Entry<Integer, Double> entry : polinomMap.entrySet()) {
            if (entry.getKey() == exponent) {
                double newCoeficient = entry.getValue() + coeficient;
                polinomMap.put(entry.getKey(), newCoeficient);
                return;
            }
        }
        polinomMap.put(exponent, coeficient);
    }

    public double getCoeficient(int exponent) {
        for (Map.Entry<Integer, Double> entry : polinomMap.entrySet()) {
            if (entry.getKey() == exponent) {
                return entry.getValue();
            }
        }
        return 0.0f;
    }

    public int getDegree() {
        if (polinomMap.isEmpty()) {
            return -1;
        }
        int maxDegree = Integer.MIN_VALUE;
        for (int degree : polinomMap.keySet()) {
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    public Set<Integer> getExponent() {
        return polinomMap.keySet();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Integer> sortedExponents = new ArrayList<>(polinomMap.keySet());
        Collections.sort(sortedExponents, Collections.reverseOrder());
        for (int exponent : sortedExponents) {
            double coefficient = polinomMap.get(exponent);
            if (coefficient == 0.0f) {
                continue;
            }
            if (sb.length() > 0) {
                if (coefficient > 0.0f) {
                    sb.append("+");
                }
            }
            if (exponent == 0) {
                sb.append(coefficient);
            } else if (exponent == 1) {
                sb.append(coefficient).append("x");
            } else {
                sb.append(coefficient).append("x^").append(exponent);
            }
        }
        return sb.toString();
    }
}