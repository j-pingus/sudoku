package gee.sudoku.branchsolver;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gee.sudoku.io.MatriceFile;

public class QuickSolverTest {
	private static final Logger LOG = Logger.getLogger(QuickSolverTest.class);
	private final int TEST_SIZE = 1_000_000;
	long start;
	QuickSolver s;
	private Sudoku solutions[];

	@Before
	public void startTime() {
		start = System.currentTimeMillis();
		s = new QuickSolver();
		solutions = new Sudoku[] {};
	}

	@After
	public void ellapsed() {
		LOG.info(String.format("%d solutions in %,.3f s\n", solutions.length,
				(System.currentTimeMillis() - start) / 1000f));
	}

	@Test
	public void test() {
		solutions = s.search(
				new Sudoku(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }),
				TEST_SIZE);

	}

	@Test
	public void test2() {
		solutions = s.search(new Sudoku(new byte[] { 4, 0, 0, 1, 0, 0, 0, 0, 0,

				0, 0, 0, 2, 0, 9, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 6, 3, 0,

				0, 9, 5, 0, 0, 6, 0, 0, 0,

				0, 7, 0, 0, 3, 0, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 1, 0, 8,

				0, 0, 9, 0, 0, 0, 0, 5, 0,

				0, 0, 0, 0, 8, 1, 0, 0, 0,

				0, 3, 7, 0, 0, 0, 0, 9, 0 }), TEST_SIZE);
	}

	@Test
	public void testSlow() {
		Sudoku sudo = new Sudoku(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,

				2, 0, 0, 0, 0, 0, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 0, 0, 0,

				0, 0, 0, 1, 0, 0, 0, 0, 0,

				5, 0, 0, 0, 0, 0, 0, 0, 0,

				0, 4, 0, 0, 0, 0, 0, 0, 0,

				0, 9, 0, 0, 0, 0, 0, 0, 0,

				0, 8, 6, 0, 0, 0, 0, 0, 0,

				7, 3, 0, 0, 0, 0, 0, 0, 0

		});
		LOG.info(sudo);
		solutions = s.search(sudo, 1_000);
		LOG.info(solutions[0]);
	}

	@Test
	public void searchNewGame() {
		Sudoku sudo = QuickSolver.newPuzzle(81);
		LOG.info(sudo);
	}

	@Test
	public void searchNewEasyGame() {
		Sudoku sudo = QuickSolver.newPuzzle(55);
		LOG.info(sudo);
	}

	@Test
	public void generate1000Puzzles() {
		for (int i = 0; i < 1000; i++) {
			Sudoku sudo = QuickSolver.newPuzzle(81);
			MatriceFile.write(sudo, new File("target", sudo.getFileName()));
		}
	}
}
