import static java.awt.Color.green;

public class ColorSort {

    enum Color { RED, GREEN, BLUE }; //muudetud suurteks täheteks

    public static void main(String[] param) {
        // Testmassiiv
        Color[] balls = {
                Color.BLUE, Color.RED, Color.GREEN, Color.RED, Color.BLUE, Color.GREEN
        };

        reorder(balls);

        // Väljasta tulemus
        for (Color c : balls) {
            System.out.print(c + " ");
        }
    }

    public static void reorder(Color[] balls) {
        // Kui massiiv on tühi või liiga väike, pole vaja sorteerida
        if (balls == null || balls.length < 2) return;


        // muutujate nimed nüüd on selged
        int red = 0;                 // järgmine koht punase (red) jaoks
        int current = 0;                 // hetkel vaadeldav element
        int blue = balls.length - 1; // järgmine koht sinise (blue) jaoks

        // Läbime massiivi ühe korraga
        while (current <= blue) {
            switch (balls[current]) {

                case RED:
                    // Vaheta current element (mid) vasakule (low)
                    Color tmp1 = balls[red];
                    balls[red] = balls[current];
                    balls[current] = tmp1;

                    // Mõlemad liiguvad edasi
                    red++;
                    current++;
                    break;

                case GREEN:
                    // Green jääb keskele → liigume lihtsalt edasi
                    current++;
                    break;

                case BLUE:
                    // Vaheta current element (mid) paremale (high)
                    Color tmp2 = balls[current];
                    balls[current] = balls[blue];
                    balls[blue] = tmp2;

                    // Vähendame high-i
                    // NB! mid ei suurene, sest uus element tuleb üle kontrollida
                    blue--;
                    break;
            }
        }
    }
}