package gee.sudoku.branchsolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class QuickSolver {
	private static final Logger LOG = Logger.getLogger(QuickSolver.class);

	public static Sudoku getRandom() {
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
		LOG.info("random seed" + sudo);
		// Based on the 9 first set numbers search for max 1000 possible
		// solutions
		Sudoku solutions[] = search(sudo, 1000);
		int r = new Random(System.currentTimeMillis())
				.nextInt(solutions.length);
		// Select one randomly
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
		long millis = System.currentTimeMillis();
		if (search(sudo, 2).length != 1) {
			sudo.cells[pos] = val;
		}
		millis = System.currentTimeMillis() - millis;
		if (millis > 1000) {
			LOG.warn("taking time :" + millis);
		}
	}

	public static Sudoku[] search(Sudoku sudoku, int maxSolutions) {
		Sudoku flip = sudoku.flip();
		Sudoku mirrorV = sudoku.mirrorV();
		Sudoku mirrorH = sudoku.mirrorH();
		Sudoku invert = sudoku.invert();
		Sudoku rotateh = sudoku.rotateH(-3);
		Transfo t = Transfo.NONE;
		int score = sudoku.score();
		int score2 = flip.score();
		if (score > score2) {
			t = Transfo.FLIP;
			sudoku = flip;
			score = score2;
		}
		score2 = mirrorH.score();
		if (score > score2) {
			t = Transfo.MIRROR_H;
			sudoku = mirrorH;
			score = score2;
		}
		score2 = mirrorV.score();
		if (score > score2) {
			t = Transfo.MIRROR_V;
			sudoku = mirrorV;
			score = score2;
		}
		score2 = invert.score();
		if (score > score2) {
			t = Transfo.INVERT;
			sudoku = invert;
		}
		score2 = rotateh.score();
		if (score > score2) {
			t = Transfo.ROTATE_H;
			sudoku = rotateh;
		}
		Options o = new Options(sudoku.cells);
		Sudoku ret[] = new Sudoku[maxSolutions];
		int solutions = search(0, 0, o, ret, t);
		Sudoku ret2[] = new Sudoku[solutions];
		System.arraycopy(ret, 0, ret2, 0, solutions);
		return ret2;
	}

	;

	private static int search(int solutions, int pos, Options o, Sudoku ret[],
			Transfo t) {
		LOG.debug("\n" + o);
		if (ret.length <= solutions) {
			return solutions;
		}
		if (pos == 81) {
			Sudoku sol = new Sudoku(o);
			switch (t) {
			case ROTATE_H:
				sol = sol.rotateH(3);
				break;
			case FLIP:
				sol = sol.flip();
				break;
			case INVERT:
				sol = sol.invert();
				break;
			case MIRROR_V:
				sol = sol.mirrorV();
				break;
			case MIRROR_H:
				sol = sol.mirrorH();
				break;
			}
			ret[solutions++] = sol;
			return solutions;
		}
		for (int i = 0; i < 9; i++) {
			if (o.hasChoice(pos, i)) {
				Options o2 = o.clone();
				if (o2.removeOption(pos, i)) {
					solutions = search(solutions, pos + 1, o2, ret, t);
				}
			}
		}
		return solutions;
	}

	public static Sudoku newPuzzle(int removals) {
		Sudoku sudo = getRandom();
		LOG.debug("New random");
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
			if (i % 10 == 9)
				LOG.debug("Removed...");
		}
		LOG.debug("done");
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

	private enum Transfo {
		NONE, INVERT, MIRROR_V, MIRROR_H, FLIP, ROTATE_H
	}

}
