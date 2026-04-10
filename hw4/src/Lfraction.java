import java.util.*;

public class Lfraction implements Comparable<Lfraction> {

    public static void main(String[] param) {
        Lfraction a = new Lfraction(1, 2);
        Lfraction b = new Lfraction(1, 3);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a + b = " + a.plus(b));
        System.out.println("a - b = " + a.minus(b));
        System.out.println("a * b = " + a.times(b));
        System.out.println("a / b = " + a.divideBy(b));
        System.out.println("-a = " + a.opposite());
        System.out.println("1/a = " + a.inverse());
        System.out.println("a == b? " + a.equals(b));
        System.out.println("a.compareTo(b) = " + a.compareTo(b));
        System.out.println("a.toDouble() = " + a.toDouble());
        System.out.println("toLfraction(0.5, 4) = " + Lfraction.toLfraction(0.5, 4));

        Lfraction c = new Lfraction(7, 3);
        System.out.println("c = " + c);
        System.out.println("integerPart = " + c.integerPart());
        System.out.println("fractionPart = " + c.fractionPart());

        Lfraction neg = new Lfraction(-7, 3);
        System.out.println("neg = " + neg);
        System.out.println("integerPart = " + neg.integerPart());
        System.out.println("fractionPart = " + neg.fractionPart());

        System.out.println("valueOf(\"3/4\") = " + Lfraction.valueOf("3/4"));
        System.out.println("valueOf(\"5\") = " + Lfraction.valueOf("5"));
        System.out.println("hashCode a=" + a.hashCode() + " b=" + b.hashCode());

        try {
            new Lfraction(1, 0);
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private long numerator;
    private long denominator;

    private static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public Lfraction(long a, long b) {
        if (b == 0) {
            throw new RuntimeException("Denominator must not be zero.");
        }
        if (b < 0) {
            a = -a;
            b = -b;
        }
        long g = gcd(Math.abs(a), b);
        numerator = a / g;
        denominator = b / g;
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object m) {
        if (this == m) return true;
        if (!(m instanceof Lfraction)) return false;
        Lfraction other = (Lfraction) m;
        return this.numerator == other.numerator && this.denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    public Lfraction plus(Lfraction m) {
        long newNum = this.numerator * m.denominator + m.numerator * this.denominator;
        long newDen = this.denominator * m.denominator;
        return new Lfraction(newNum, newDen);
    }

    public Lfraction times(Lfraction m) {
        return new Lfraction(this.numerator * m.numerator, this.denominator * m.denominator);
    }

    public Lfraction inverse() {
        if (numerator == 0) {
            throw new RuntimeException("Cannot invert a zero fraction.");
        }
        return new Lfraction(denominator, numerator);
    }

    public Lfraction opposite() {
        return new Lfraction(-numerator, denominator);
    }

    public Lfraction minus(Lfraction m) {
        return this.plus(m.opposite());
    }

    public Lfraction divideBy(Lfraction m) {
        if (m.numerator == 0) {
            throw new RuntimeException("Cannot divide by zero fraction.");
        }
        return this.times(m.inverse());
    }

    @Override
    public int compareTo(Lfraction m) {
        long lhs = this.numerator * m.denominator;
        long rhs = m.numerator * this.denominator;
        return Long.compare(lhs, rhs);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Lfraction(numerator, denominator);
    }

    public long integerPart() {
        return numerator / denominator;
    }

    public Lfraction fractionPart() {
        long intPart = integerPart();
        return new Lfraction(numerator - intPart * denominator, denominator);
    }

    public double toDouble() {
        return (double) numerator / (double) denominator;
    }

    public static Lfraction toLfraction(double f, long d) {
        if (d <= 0) {
            throw new RuntimeException("Denominator must be positive.");
        }
        long n = Math.round(f * d);
        return new Lfraction(n, d);
    }

    public static Lfraction valueOf(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new RuntimeException("Invalid fraction string: \"" + s + "\"");
        }
        s = s.trim();
        try {
            if (s.contains("/")) {
                String[] parts = s.split("/", 2);
                long num = Long.parseLong(parts[0].trim());
                long den = Long.parseLong(parts[1].trim());
                return new Lfraction(num, den);
            } else {
                long num = Long.parseLong(s);
                return new Lfraction(num, 1L);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid fraction string: \"" + s + "\"", e);
        }
    }
}