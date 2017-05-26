package gee.sudoku.branchsolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import gee.sudoku.io.MatriceFile;

public class QuickSolver {
	private static final Logger LOG = Logger.getLogger(QuickSolver.class);

	public static Sudoku getRandom() {
		Sudoku s = getRandom(10);
		if (s != null) {
			int t2 = new Random(System.currentTimeMillis()).nextInt(3);
			SudokuTransformation ts[][] = s.getAllTransformations();
			LOG.debug("s:\n" + s);
			for (int i = 0; i < t2; i++) {
				int t = new Random(System.currentTimeMillis())
						.nextInt(ts.length);
				s = s.transform(ts[t][0]);
				LOG.debug("transformed s:\n" + s);
			}
		}
		return s;
	}

	public static Sudoku getRandom(int attempts) {
		if (attempts == 0)
			return null;
		List<Integer> pos = new ArrayList<>();
		// Fill pos with all sudoku positions
		for (int p = 0; p < 81; p++)
			pos.add(p);
		// Randomize the order
		Collections.shuffle(pos);
		Sudoku sudo = new Sudoku();
		// Put numbers from 1 to 9 in random places, this will "branch" the
		// solver towards different solutions each time
		for (byte i = 0; i < 9; i++) {
			int p = pos.get(i);
			sudo.cells[p] = b(i + 1);
		}
		LOG.debug("random seed" + sudo);
		MatriceFile.write(sudo, new File("target", "random_seed.sudoku"));
		// Based on the 9 first set numbers search for max 1000 possible
		// solutions

		Sudoku solutions[] = search(sudo, 1000);
		if (solutions.length == 0) {
			// Somtimes (once on 10000) it is not possible to get a solution
			// even with only 9 seeds.
			return getRandom(attempts - 1);
		}
		int r = new Random(System.currentTimeMillis())
				.nextInt(solutions.length);
		// Select one randomly and apply a random (non destructive)
		// transformation to it n
		return solutions[r];
	}

	private static byte b(int i) {
		return (byte) i;
	}

	private static void remove(Sudoku sudo, int pos) {
		if (pos < 0 || pos > sudo.cells.length)
			return;
		byte val = sudo.cells[pos];
		if (val == 0)
			return;
		sudo.cells[pos] = 0;
		if (search(sudo, 2).length != 1) {
			sudo.cells[pos] = val;
		}
	}

	public static int none(int i) {
		return i;
	}

	public static Sudoku[] search(Sudoku sudoku, int maxSolutions) {
		long ts = System.currentTimeMillis();
		SudokuTransformation transfos[] = sudoku.getLowScoreTransformations();
		// verify which transformation has the lowest score and select it
		Options o = new Options(sudoku.transform(transfos[0]).cells);
		Sudoku ret[] = new Sudoku[maxSolutions];
		int solutions = search(0, 0, o, ret);
		Sudoku ret2[] = new Sudoku[solutions];
		for (int i = 0; i < solutions; i++)
			ret2[i] = ret[i].transform(transfos[1]);
		if ((System.currentTimeMillis() - ts) > 700)
			LOG.info("Searched " + (System.currentTimeMillis() - ts)
					+ " found: " + solutions + "\n" + sudoku.toStringMe());
		return ret2;
	}

	;

	private static int search(int solutions, int pos, Options o, Sudoku ret[]) {
		LOG.debug("\n" + o);
		if (ret.length <= solutions) {
			return solutions;
		}
		if (pos == 81) {
			ret[solutions++] = new Sudoku(o);
			return solutions;
		}
		for (int i = 0; i < 9; i++) {
			if (o.hasChoice(pos, i)) {
				Options o2 = o.clone();
				if (o2.removeOption(pos, i)) {
					solutions = search(solutions, pos + 1, o2, ret);
				}
			}
		}
		return solutions;
	}

	public static Sudoku newPuzzle(int removals) {
		Sudoku sudo = getRandom();
		// Randomize the order
		List<Integer> pos = getPos(81);
		Collections.shuffle(pos);
		// Try to remove each cell except if it makes the sudoku have no more
		// unique solution
		for (int i = 0; i < removals; i++) {
			int p = pos.get(i);
			remove(sudo, p);
			remove(sudo, 80 - p);
			remove(sudo, p + (((p % 9) - 4) * -2));
			remove(sudo, p + (((p / 9) - 4) * -18));
		}
		return sudo;
	}

	private static List<Integer> getPos(int size) {
		List<Integer> pos;
		pos = new ArrayList<>();
		// Fill pos with all sudoku positions
		for (int p = 0; p < size; p++)
			pos.add(p);
		return pos;
	}

}
