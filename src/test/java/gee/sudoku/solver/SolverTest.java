package gee.sudoku.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gee.sudoku.io.MatriceFile;
import org.junit.Before;
import org.junit.Test;

import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;

import java.io.File;

/**
 * Created by gerald on 31/03/17.
 */
public class SolverTest {
	Matrice matrice;

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
		MatriceFile.write(matrice,new File("y-wing.sudoku"));
		Solver solver = new Solver(matrice);
		MatriceAction action;
		action = solver.getNextAction();
		assertEquals(Strategies.NAKED_PAIR, action.getStrategy());
		action.apply(matrice);

		action = solver.getNextAction();
		assertEquals(Strategies.NAKED_PAIR, action.getStrategy());
		action.apply(matrice);

		System.out.println(matrice);
		action = solver.getNextAction();
		assertNotNull(action);
		// Not implemented yet but this is the next to be applied
		assertEquals(Strategies.Y_WING, action.getStrategy());
		System.out.println(matrice);
		System.out.println(solver.getNextAction());
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
		System.out.println(matrice);
		Solver solver = new Solver(matrice);
		MatriceAction action;
		while ((action = solver.getNextAction()) != null) {
			action.apply(matrice);
			matrice.check();
			System.out.println(action.getStrategy());
		}
		System.out.println(matrice);

		assertEquals(Matrice.Status.SOLVED, matrice.getStatus());
	}

}