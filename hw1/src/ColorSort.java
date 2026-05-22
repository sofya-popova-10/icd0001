public class ColorSort {

    enum Color { red, green, blue };

    public static void main(String[] param) {
        // Testmassiiv
        Color[] balls = {
                Color.blue, Color.red, Color.green, Color.red, Color.blue, Color.green
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

        int low = 0;                 // järgmine koht punase (red) jaoks
        int mid = 0;                 // hetkel vaadeldav element
        int high = balls.length - 1; // järgmine koht sinise (blue) jaoks

        // Läbime massiivi ühe korraga
        while (mid <= high) {
            switch (balls[mid]) {

                case red:
                    // Vaheta current element (mid) vasakule (low)
                    Color tmp1 = balls[low];
                    balls[low] = balls[mid];
                    balls[mid] = tmp1;

                    // Mõlemad liiguvad edasi
                    low++;
                    mid++;
                    break;

                case green:
                    // Green jääb keskele → liigume lihtsalt edasi
                    mid++;
                    break;

                case blue:
                    // Vaheta current element (mid) paremale (high)
                    Color tmp2 = balls[mid];
                    balls[mid] = balls[high];
                    balls[high] = tmp2;

                    // Vähendame high-i
                    // NB! mid ei suurene, sest uus element tuleb üle kontrollida
                    high--;
                    break;
            }
        }
    }
}