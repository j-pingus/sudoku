package gee.sudoku.branchsolver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gee.sudoku.branchsolver.QuickSolver.Sudoku;

public class QuickSolverTest {
	private final int TEST_SIZE = 1_000_000;
	private Sudoku solutions[];
	long start;
	QuickSolver s ;
	@Before
	public void startTime(){
		 start = System.currentTimeMillis();
			s = new QuickSolver();
	}
	@After
	public void ellapsed(){
		System.out.printf("%d solutions in %,.3f s\n", solutions.length, (System.currentTimeMillis() - start) / 1000f);
	}
	@Test
	public void test() {
		solutions = s.search(new int[] {1,2,3,4,5,6,7,8,9}, TEST_SIZE);
		
	}
	@Test
	public void test2() {
		solutions = s.search(new int[] {4, 0, 0, 1, 0, 0, 0, 0, 0,

				0, 0, 0, 2, 0, 9, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 6, 3, 0,

				0, 9, 5, 0, 0, 6, 0, 0, 0,

				0, 7, 0, 0, 3, 0, 0, 0, 0,

				0, 0, 0, 0, 0, 0, 1, 0, 8,

				0, 0, 9, 0, 0, 0, 0, 5, 0,

				0, 0, 0, 0, 8, 1, 0, 0, 0,

				0, 3, 7, 0, 0, 0, 0, 9, 0}, TEST_SIZE);
	}

}
