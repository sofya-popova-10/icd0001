import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack {

    private static class Node {
        double value;
        Node next;

        Node(double value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node top;

    public static void main (String[] argum) {
        // TODO!!! Your tests here!
        DoubleStack s = new DoubleStack();
        System.out.println(s);                   // []
        s.push(2.);
        s.push(15.);
        System.out.println(s);                   // [2.0, 15.0]
        s.op("-");
        System.out.println(s);                   // [-13.0]
        System.out.println(DoubleStack.interpret("2. 15. -"));   // -13.0
    }

    DoubleStack() {
        // TODO!!! Your constructor here!
        top = null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // return this; // TODO!!! Your code here!
        DoubleStack copy = new DoubleStack();
        ArrayList<Double> values = new ArrayList<>();

        Node curr = top;
        while (curr != null) {
            values.add(curr.value);
            curr = curr.next;
        }

        for (int i = values.size() - 1; i >= 0; i--) {
            copy.push(values.get(i));
        }

        return copy;
    }

    public boolean stEmpty() {
        // return false; // TODO!!! Your code here!
        return top == null;
    }

    public void push (double a) {
        // TODO!!! Your code here!
        top = new Node(a, top);
    }

    public double pop() {
        // return 0.; // TODO!!! Your code here!
        if (stEmpty()) {
            throw new RuntimeException("Stack underflow: cannot pop from an empty stack.");
        }
        double value = top.value;
        top = top.next;
        return value;
    } // pop

    public void op (String s) {
        // TODO!!!
        if (s == null) {
            throw new RuntimeException("Operator error: operator is null.");
        }

        if (!s.equals("+") && !s.equals("-") && !s.equals("*") && !s.equals("/")) {
            throw new RuntimeException("Illegal operator \"" + s + "\": only +, -, * and / are allowed.");
        }

        if (top == null || top.next == null) {
            throw new RuntimeException("Stack underflow while applying operator \"" + s
                    + "\": two numbers are required on the stack.");
        }

        double right = pop();
        double left = pop();
        double result;

        switch (s) {
            case "+":
                result = left + right;
                break;
            case "-":
                result = left - right;
                break;
            case "*":
                result = left * right;
                break;
            case "/":
                result = left / right;
                break;
            default:
                throw new RuntimeException("Unexpected operator: \"" + s + "\".");
        }

        push(result);
    }

    public double tos() {
        // return 0.; // TODO!!! Your code here!
        if (stEmpty()) {
            throw new RuntimeException("Top-of-stack error: stack is empty.");
        }
        return top.value;
    }

    @Override
    public boolean equals (Object o) {
        // return true; // TODO!!! Your code here!
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoubleStack)) {
            return false;
        }

        DoubleStack other = (DoubleStack) o;
        Node a = this.top;
        Node b = other.top;

        while (a != null && b != null) {
            if (Double.compare(a.value, b.value) != 0) {
                return false;
            }
            a = a.next;
            b = b.next;
        }

        return a == null && b == null;
    }

    @Override
    public String toString() {
        // return null; // TODO!!! Your code here!
        ArrayList<Double> values = new ArrayList<>();
        Node curr = top;

        while (curr != null) {
            values.add(curr.value);
            curr = curr.next;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = values.size() - 1; i >= 0; i--) {
            sb.append(values.get(i));
            if (i > 0) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    public static double interpret (String pol) {
        // Kontrollime, et sisendstring üldse eksisteeriks.
        if (pol == null) {
            throw new RuntimeException("RPN expression error: expression is null.");
        }

        // Eemaldame algusest ja lõpust tühikud.
        String trimmed = pol.trim();

        // Kui pärast trim'i ei jäänud midagi alles, siis avaldis on tühi.
        if (trimmed.isEmpty()) {
            throw new RuntimeException("RPN expression error: expression is empty or contains only whitespace.");
        }

        // Loome abipinu, mille peal hakkame avaldist samm-sammult läbi mängima.
        DoubleStack st = new DoubleStack();

        // Jagame sisendi tokeniteks kõigi whitespace-märkide järgi.
        String[] tokens = trimmed.split("\\s+");

        // Töötleme kõik tokenid vasakult paremale.
        for (String token : tokens) {
            try {
                // Tavalised aritmeetilised tehted delegeerime olemasolevale op() meetodile.
                if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                    st.op(token);

                    // SWAP: a b -> b a
                    // Võtame kaks pealmist elementi ja paneme need tagasi vastupidises järjekorras.
                } else if (token.equals("SWAP")) {
                    // Kontrollime, et vähemalt kaks elementi oleks olemas.
                    if (st.stEmpty()) {
                        throw new RuntimeException("Stack underflow while applying operator \"SWAP\": two elements are required on the stack.");
                    }
                    double first = st.pop();   // peamine element b

                    if (st.stEmpty()) {
                        throw new RuntimeException("Stack underflow while applying operator \"SWAP\": two elements are required on the stack.");
                    }
                    double second = st.pop();  // järgmine element a

                    // Tagasi pannes tekib järjestus b a.
                    st.push(first);
                    st.push(second);

                    // ROT: a b c -> b c a
                    // Kolmas pealmine element tõstetakse kõige peale.
                } else if (token.equals("ROT")) {
                    // Kontrollime, et vähemalt kolm elementi oleks olemas.
                    if (st.stEmpty()) {
                        throw new RuntimeException("Stack underflow while applying operator \"ROT\": three elements are required on the stack.");
                    }
                    double first = st.pop();   // c

                    if (st.stEmpty()) {
                        throw new RuntimeException("Stack underflow while applying operator \"ROT\": three elements are required on the stack.");
                    }
                    double second = st.pop();  // b

                    if (st.stEmpty()) {
                        throw new RuntimeException("Stack underflow while applying operator \"ROT\": three elements are required on the stack.");
                    }
                    double third = st.pop();   // a

                    // Tagasi pannes tahame saada b c a.
                    st.push(second);  // b
                    st.push(first);   // c
                    st.push(third);   // a

                    // DUP: a -> a a
                    // Loome pealmisest elemendist koopia.
                } else if (token.equals("DUP")) {
                    // tos() kontrollib ise, et pinu ei oleks tühi.
                    double value = st.tos();
                    st.push(value);

                    // DROP: a -> (eemaldatakse)
                    // Eemaldame pealmise elemendi.
                } else if (token.equals("DROP")) {
                    // pop() kontrollib ise, et pinu ei oleks tühi.
                    st.pop();

                    // Kui token ei ole ükski teadaolev operaator, proovime seda lugeda arvuna.
                } else {
                    st.push(Double.parseDouble(token));
                }

                // Kui token ei olnud arv, anname arusaadava veateate.
            } catch (NumberFormatException e) {
                throw new RuntimeException("RPN expression error in \"" + pol
                        + "\": illegal token \"" + token
                        + "\". Token must be a double number or one of + - * / SWAP ROT DUP DROP.");

                // Kui ükskõik milline stackioperatsioon ebaõnnestub, lisame kogu avaldise konteksti.
            } catch (RuntimeException e) {
                throw new RuntimeException("RPN expression error in \"" + pol + "\": " + e.getMessage());
            }
        }

        // Pärast kõigi tokenite töötlemist peab pinus olema täpselt üks tulemus.
        double result;
        try {
            result = st.pop();
        } catch (RuntimeException e) {
            throw new RuntimeException("RPN expression error in \"" + pol + "\": " + e.getMessage());
        }

        // Kui pinusse jäi midagi veel alles, siis avaldis ei olnud tasakaalus.
        if (!st.stEmpty()) {
            throw new RuntimeException("RPN expression error in \"" + pol
                    + "\": redundant elements remain on stack " + st + ".");
        }

        // Tagastame lõpptulemuse.
        return result;
    }

}