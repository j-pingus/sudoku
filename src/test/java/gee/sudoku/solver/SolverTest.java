package gee.sudoku.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import gee.sudoku.io.MatriceFile;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;

/**
 * Created by gerald on 31/03/17.
 */
public class SolverTest {
	private static final Logger LOG = Logger.getLogger(SolverTest.class);
	Matrice matrice;

	@Test
	public void testSwordFish() {
		matrice = MatriceFile.read(new File("swordfish.sudoku"));
		System.out.println(Arrays.toString(matrice.getValues()));
		Solver solver = new Solver(matrice);
		MatriceAction matriceAction = solver.getNextAction();
		assertNotNull(matriceAction);
		matriceAction.apply(matrice);

		matriceAction = solver.getNextAction();
		assertNotNull(matriceAction);
		matriceAction.apply(matrice);

		matriceAction = solver.getNextAction();
		assertNotNull(matriceAction);
		assertEquals(Strategies.SWORDFISH, matriceAction.getStrategy());
		matriceAction.apply(matrice);

	}

	@Before

	public void setUp() throws Exception {
	}

	@Test
	public void testYWing() throws Exception {
		matrice = new Matrice(9);
		matrice.init(new int[][] {

				new int[] { 3, 5, 7, 0, 0, 0, 4, 0, 0 },
				new int[] { 0, 6, 2, 0, 7, 0, 9, 3, 0 },
				new int[] { 0, 9, 4, 2, 0, 3, 7, 0, 0 },
				// ---
				new int[] { 2, 3, 5, 8, 4, 7, 6, 1, 9 },
				new int[] { 7, 1, 6, 0, 0, 2, 8, 4, 3 },
				new int[] { 9, 4, 8, 1, 3, 6, 2, 0, 0 },
				// ---
				new int[] { 0, 2, 9, 7, 1, 5, 3, 8, 0 },
				new int[] { 5, 7, 3, 0, 2, 8, 1, 9, 0 },
				new int[] { 0, 8, 1, 3, 0, 0, 5, 0, 0 }
				// ---
		});
		MatriceFile.write(matrice, new File("y-wing.sudoku"));
		Solver solver = new Solver(matrice);
		MatriceAction action;
		action = solver.getNextAction();
		assertEquals(Strategies.NAKED_PAIR, action.getStrategy());
		action.apply(matrice);

		action = solver.getNextAction();
		assertEquals(Strategies.NAKED_PAIR, action.getStrategy());
		action.apply(matrice);

		LOG.info(matrice);
		action = solver.getNextAction();
		assertNotNull(action);
		// Not implemented yet but this is the next to be applied
		assertEquals(Strategies.Y_WING, action.getStrategy());
		LOG.info(matrice);
		LOG.info(solver.getNextAction());
	}

	@Test
	public void testGetNextAction() throws Exception {
		matrice = new Matrice(9);
		matrice.init(new int[][] { new int[] { 0, 0, 3, 9, 0, 0, 7, 6, 0 },
				new int[] { 0, 4, 0, 0, 0, 6, 0, 0, 9 },
				new int[] { 6, 0, 7, 0, 1, 0, 0, 0, 4 },
				// ---
				new int[] { 2, 0, 0, 6, 7, 0, 0, 9, 0 },
				new int[] { 0, 0, 4, 3, 0, 5, 6, 0, 0 },
				new int[] { 0, 1, 0, 0, 4, 9, 0, 0, 7 },
				// ---
				new int[] { 7, 0, 0, 0, 9, 0, 2, 0, 1 },
				new int[] { 3, 0, 0, 2, 0, 0, 0, 4, 0 },
				new int[] { 0, 2, 9, 0, 0, 8, 5, 0, 0 } });
		LOG.info(matrice);
		Solver solver = new Solver(matrice);
		MatriceAction action;
		while ((action = solver.getNextAction()) != null) {
			action.apply(matrice);
			matrice.check();
			LOG.info(action.getStrategy());
		}
		LOG.info(matrice);

		assertEquals(Matrice.Status.SOLVED, matrice.getStatus());
	}

	@Test
	public void measureSudoku() throws Exception {
		for (File input : getSudokus("/")) {
			measure(input);
		}
	}

	@Test
	public void retryMeasureSudoku() throws Exception {
		for (File input : getSudokus("UNSOLVED")) {
			measure(input);
		}
		for (File input : getSudokus("SOLVED/SOLE_OPTION")) {
			measure(input);
		}
		for (File input : getSudokus("SOLVED/X_WING_COL")) {
			measure(input);
		}
	}

	private File[] getSudokus(String string) {
		return new File("target", string).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sudoku");
			}
		});
	}

	private void measure(File input) throws Exception {
		matrice = MatriceFile.read(input);
		Solver solver = new Solver(matrice);
		MatriceAction best = null, action;
		while ((action = solver.getNextAction()) != null) {
			if (best == null || best.getStrategy().weight < action
					.getStrategy().weight) {
				best = action;
			}
			action.apply(matrice);
		}
		matrice.check();
		if (matrice.getStatus() == Matrice.Status.SOLVED) {
			if(!move(input, "target/" + matrice.getStatus().toString() + "/"
					+ best.getStrategy().toString())){
				LOG.error("can't move "+input);
			};
		}

	}

	private boolean move(File input, String s) {
		File dest = new File(s);
		if (!dest.exists()) {
			dest.mkdirs();
		}
		dest = new File(dest, input.getName());
		boolean ret = input.renameTo(dest);
		LOG.info("moved to :" + dest);
		return ret;
	}
}
