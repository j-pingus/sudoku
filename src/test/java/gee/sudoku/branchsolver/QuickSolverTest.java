package gee.sudoku.branchsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QuickSolverTest {
	private final int TEST_SIZE = 1_000_000;
	private Sudoku solutions[];
	long start;
	QuickSolver s;
	List<Integer> pos ;

	@Before
	public void startTime() {
		pos = new ArrayList<>();
		// Fill pos with all sudoku positions
		for (int p = 0; p < 81; p++)
			pos.add(p);
		start = System.currentTimeMillis();
		s = new QuickSolver();
		solutions = new Sudoku[]{};
	}

	@After
	public void ellapsed() {
		System.out.printf("%d solutions in %,.3f s\n", solutions.length, (System.currentTimeMillis() - start) / 1000f);
	}

	@Test
	public void test() {
		solutions = s.search(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, TEST_SIZE);

	}

	@Test
	public void test2() {
		solutions = s.search(new int[] { 4, 0, 0, 1, 0, 0, 0, 0, 0,

				0, 0, 0, 2, 0, 9, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 6, 3, 0,

				0, 9, 5, 0, 0, 6, 0, 0, 0,

				0, 7, 0, 0, 3, 0, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 1, 0, 8,

				0, 0, 9, 0, 0, 0, 0, 5, 0,

				0, 0, 0, 0, 8, 1, 0, 0, 0,

				0, 3, 7, 0, 0, 0, 0, 9, 0 }, TEST_SIZE);
	}

	@Test
	public void searchNewGame() {
		Sudoku sudo = s.getRandom();
		// Randomize the order
		Collections.shuffle(pos);
		//Try to remove each cell except if it makes the sudoku have no more unique solution
		for (int i = 0; i < 81; i++) {
			int p = pos.get(i);
			int val = sudo.cells[p];
			sudo.cells[p] = 0;
			if (s.search(sudo.cells, 2).length != 1) {
				sudo.cells[p] = val;
			}
		}
		System.out.println(sudo);
	}
	@Test
	public void searchNewEasyGame() {
		Sudoku sudo = s.getRandom();
		// Randomize the order
		Collections.shuffle(pos);
		//Try to remove each cell except if it makes the sudoku have no more unique solution
		for (int i = 0; i < 55; i++) {
			int p = pos.get(i);
			int val = sudo.cells[p];
			sudo.cells[p] = 0;
			if (s.search(sudo.cells, 2).length != 1) {
				sudo.cells[p] = val;
			}
		}
		System.out.println(sudo);
	}
}
