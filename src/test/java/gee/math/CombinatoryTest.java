package gee.math;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by gerald on 01/04/17.
 */
public class CombinatoryTest {
	private static final Logger LOG = Logger.getLogger(CombinatoryTest.class);

	@Test
	public void testGetPairs() throws Exception {
		int sols[][] = Combinatory.getPairs(1, 2, 3, 4, 5, 6);
		for (int sol[] : sols) {
			LOG.info(Arrays.toString(sol));
		}
	}

	@Test
	public void testGetTriplets() throws Exception {
		int sols[][] = Combinatory.getTriplets(1, 2, 3, 4, 5, 6);
		for (int sol[] : sols) {
			LOG.info(Arrays.toString(sol));
		}
	}

	@Test
	public void testGetQuads() throws Exception {
		int sols[][] = Combinatory.getQuads(1, 2, 3, 4, 5, 6);
		for (int sol[] : sols) {
			LOG.info(Arrays.toString(sol));
		}
	}

	@Test
	public void testGetTuples() throws Exception {
		int sols[][] = Combinatory.getTuples(2, 1, 2, 3, 4);
		for (int sol[] : sols) {
			LOG.info(Arrays.toString(sol));
		}
		sols = Combinatory.getTuples(4, 1, 2, 3, 4, 5, 6);
		for (int sol[] : sols) {
			LOG.info(Arrays.toString(sol));
		}
	}
}