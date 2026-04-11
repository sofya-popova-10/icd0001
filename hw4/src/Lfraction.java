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

        // Uue meetodi näited reaalarvust murduks teisendamisel.
        System.out.println("valueOf(0.125) = " + Lfraction.valueOf(0.125));
        System.out.println("valueOf(-2.5) = " + Lfraction.valueOf(-2.5));
        System.out.println("valueOf(Math.PI) = " + Lfraction.valueOf(Math.PI));

        System.out.println("hashCode a=" + a.hashCode() + " b=" + b.hashCode());

        try {
            new Lfraction(1, 0);
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private long numerator;
    private long denominator;

    // Suurim ühistegur murru taandamiseks.
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
        // Nimetaja ei tohi olla null.
        if (b == 0) {
            throw new RuntimeException("Denominator must not be zero.");
        }

        // Hoiame märki alati lugejas.
        if (b < 0) {
            a = -a;
            b = -b;
        }

        // Taandame murru kohe normaalkujule.
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
        // Kui nimetaja on 1, kuvame lihtsalt täisarvu.
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

        // Kuna konstruktor taandab murru, piisab väljade otsesest võrdlusest.
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

    public static Lfraction valueOf(double df) {
        // Not A Number ja lõpmatus ei ole mõistlikud murrud.
        if (Double.isNaN(df) || Double.isInfinite(df)) {
            throw new RuntimeException("Invalid double value: " + df);
        }

        // Null standardkuju.
        if (df == 0.0) {
            return new Lfraction(0, 1);
        }

        // Jätame märgi meelde ja töötame positiivse absoluutväärtusega.
        boolean negative = df < 0;
        double x = Math.abs(df);

        // Ahelmurru esimene liige ehk täisarvuosa.
        long a0 = (long) Math.floor(x);

        // Konvergentide algväärtused:
        // p0/q0 = 1/0 ja p1/q1 = a0/1
        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;

        // Esimene murdosa pärast täisarvuosa eemaldamist.
        double frac = x - a0;

        // Kui arv oli juba täisarv, siis esimene konvergent ongi vastus.
        if (((double) p1 / (double) q1) == x) {
            return new Lfraction(negative ? -p1 : p1, q1);
        }

        while (true) {
            // Kui murdosa sai otsa, katkestame.
            if (frac == 0.0) {
                break;
            }

            // Ahelmurru järgmine samm: pöörame murdosa ümber.
            x = 1.0 / frac;
            long ai = (long) Math.floor(x);

            // Arvutame järgmise konvergendi:
            // p2 = ai * p1 + p0
            // q2 = ai * q1 + q0
            long p2 = ai * p1 + p0;
            long q2 = ai * q1 + q0;

            // Kui uus murd teisendub tagasi täpselt samaks double'iks,
            // siis oleme leidnud sobiva vähima nimetajaga esituse.
            double approx = (double) p2 / (double) q2;
            if (approx == Math.abs(df)) {
                return new Lfraction(negative ? -p2 : p2, q2);
            }

            // Nihutame akna ühe sammu võrra edasi.
            p0 = p1;
            q0 = q1;
            p1 = p2;
            q1 = q2;

            // Järgmise sammu murdosa.
            frac = x - ai;
        }

        // Tagastame viimase leitud konvergendi.
        return new Lfraction(negative ? -p1 : p1, q1);
    }

    public static Lfraction valueOf(String s) {
        // Null või tühi string ei ole korrektne murruesitus.
        if (s == null || s.trim().isEmpty()) {
            throw new RuntimeException("Invalid fraction string: \"" + s + "\"");
        }

        s = s.trim();

        try {
            // Kui string sisaldab kaldkriipsu, eeldame kuju "lugeja/nimetaja".
            if (s.contains("/")) {
                String[] parts = s.split("/", 2);
                long num = Long.parseLong(parts[0].trim());
                long den = Long.parseLong(parts[1].trim());
                return new Lfraction(num, den);
            } else {
                // Vastasel juhul muudame sisendit täisarvuna.
                long num = Long.parseLong(s);
                return new Lfraction(num, 1L);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid fraction string: \"" + s + "\"", e);
        }
    }
}