package gee.math;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by gerald on 01/04/17.
 */
public class CombinatoryTest {

    @Test
    public void testGetPairs() throws Exception {
        int sols[][] = Combinatory.getPairs(1, 2, 3, 4, 5, 6);
        for (int sol[] : sols) {
            System.out.println(Arrays.toString(sol));
        }
    }

    @Test
    public void testGetTriplets() throws Exception {
        int sols[][] = Combinatory.getTriplets(1, 2, 3, 4, 5, 6);
        for (int sol[] : sols) {
            System.out.println(Arrays.toString(sol));
        }
    }

    @Test
    public void testGetQuads() throws Exception {
        int sols[][] = Combinatory.getQuads(1, 2, 3, 4, 5, 6);
        for (int sol[] : sols) {
            System.out.println(Arrays.toString(sol));
        }
    }

    @Test
    public void testGetTuples() throws Exception {
        int sols[][] = Combinatory.getTuples(2, 1, 2, 3, 4);
        for (int sol[] : sols) {
            System.out.println(Arrays.toString(sol));
        }
        sols = Combinatory.getTuples(4, 1, 2, 3, 4, 5, 6);
        for (int sol[] : sols) {
            System.out.println(Arrays.toString(sol));
        }
    }
}