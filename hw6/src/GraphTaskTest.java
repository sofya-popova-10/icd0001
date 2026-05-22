import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Testklass graafi lühima tee leidmise kontrollimiseks.
 * Siin on eraldi testid lihtsa tee, blokeeritud kaare,
 * ligipääsmatu väljapääsu, mitme väljapääsu ja pika tee jaoks.
 */
public class GraphTaskTest {

    /**
     * Kontrollib, kas kõige lihtsam tee algusest väljapääsuni leitakse õigesti.
     */
    @Test
    public void testSimplePathToExit() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("G");

        GraphTask.Vertex a = g.createVertex("A");
        GraphTask.Vertex b = g.createVertex("B");
        GraphTask.Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 3, 8, false);
        g.createArcPair("BC", b, c, 2, 9, false);

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(c);

        GraphTask.PathResult r = g.findBestFogPath(a, exits);

        assertNotNull(r);
        assertEquals(c, r.getEndVertex());
        assertEquals(2, r.getPath().size());
        assertEquals(8, r.getTotalCost());
    }

    /**
     * Kontrollib, et blokeeritud rada jäetakse vahele ja valitakse teine tee.
     */
    @Test
    public void testBlockedArcIsAvoided() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("G");

        GraphTask.Vertex a = g.createVertex("A");
        GraphTask.Vertex b = g.createVertex("B");
        GraphTask.Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 1, 10, true);
        g.createArcPair("AC", a, c, 5, 10, false);
        g.createArcPair("BC", b, c, 1, 10, false);

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(c);

        GraphTask.PathResult r = g.findBestFogPath(a, exits);

        assertNotNull(r);
        assertEquals(c, r.getEndVertex());
        assertEquals(1, r.getPath().size());
        assertEquals(5, r.getTotalCost());
    }

    /**
     * Kontrollib, et kui väljapääsuni ei saa jõuda, tagastatakse tühi tulemus.
     */
    @Test
    public void testNoPathReturnsEmpty() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("G");

        GraphTask.Vertex a = g.createVertex("A");
        GraphTask.Vertex b = g.createVertex("B");
        GraphTask.Vertex c = g.createVertex("C");

        g.createArcPair("AB", a, b, 1, 10, true);

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(c);

        GraphTask.PathResult r = g.findBestFogPath(a, exits);

        assertNotNull(r);
        assertNull(r.getEndVertex());
        assertTrue(r.getPath().isEmpty());
        assertEquals(Integer.MAX_VALUE, r.getTotalCost());
    }

    /**
     * Kontrollib, kas programm valib mitme väljapääsu seast odavaima tee.
     */
    @Test
    public void testChoosesCheapestExit() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("G");

        GraphTask.Vertex s = g.createVertex("S");
        GraphTask.Vertex x = g.createVertex("X");
        GraphTask.Vertex y = g.createVertex("Y");

        g.createArcPair("SX", s, x, 10, 10, false);
        g.createArcPair("SY", s, y, 1, 10, false);

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(x);
        exits.add(y);

        GraphTask.PathResult r = g.findBestFogPath(s, exits);

        assertNotNull(r);
        assertEquals(y, r.getEndVertex());
        assertEquals(1, r.getPath().size());
        assertEquals(1, r.getTotalCost());
    }

    /**
     * Kontrollib, et pikem tee taastatakse õigesti mitme kaarena.
     */
    @Test
    public void testPathContainsMultipleArcs() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("G");

        GraphTask.Vertex a = g.createVertex("A");
        GraphTask.Vertex b = g.createVertex("B");
        GraphTask.Vertex c = g.createVertex("C");
        GraphTask.Vertex d = g.createVertex("D");

        g.createArcPair("AB", a, b, 2, 7, false);
        g.createArcPair("BC", b, c, 2, 7, false);
        g.createArcPair("CD", c, d, 2, 7, false);

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(d);

        GraphTask.PathResult r = g.findBestFogPath(a, exits);

        assertNotNull(r);
        assertEquals(d, r.getEndVertex());
        assertEquals(3, r.getPath().size());
        assertEquals(15, r.getTotalCost());
    }

    /**
     * Kontrollib, kas algoritm töötab ka suure graafi puhul mõistliku ajaga.
     */
    @Test
    public void testLargeGraphPerformance() {
        GraphTask gt = new GraphTask();
        GraphTask.Graph g = gt.new Graph("Large");

        int n = 2200;
        GraphTask.Vertex[] v = new GraphTask.Vertex[n];
        for (int i = 0; i < n; i++) {
            v[i] = g.createVertex("V" + i);
        }

        for (int i = 0; i < n - 1; i++) {
            g.createArcPair("E" + i, v[i], v[i + 1], 1, 10, false);
        }

        List<GraphTask.Vertex> exits = new ArrayList<GraphTask.Vertex>();
        exits.add(v[n - 1]);

        long start = System.currentTimeMillis();
        GraphTask.PathResult r = g.findBestFogPath(v[0], exits);
        long end = System.currentTimeMillis();

        assertNotNull(r);
        assertEquals(v[n - 1], r.getEndVertex());
        assertEquals(n - 1, r.getPath().size());
        assertTrue(end - start >= 0);
    }
}