package gee.sudoku.branchsolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuickSolver {
	protected class Options {
		int choices[] = new int[81];

		public Options() {
			choices = new int[81];
			for (int i = 0; i < 81; i++) {
				choices[i] = alloptions;
			}
		}

		public Options(int... cells) {
			this();
			for (int i = 0; i < cells.length && i < 81; i++) {
				if (cells[i] != 0) {
					choices[i] = masks[cells[i] - 1];
					removeOption(i, cells[i] - 1);
				}
			}
		}

		public int value(int pos) {
			int val = choices[pos];
			for (int i = 0; i < 9; i++) {
				if ((val & masks[i]) > 0) {
					return i + 1;
				}
			}
			return 0;
		}

		private boolean removeOption(int pos, int val) {
			int row = pos / 9;
			int col = pos % 9;
			int zoneCol = (col / 3) * 3;
			int zoneRow = (row / 3) * 3;
			for (int i = 0; i < 9; i++) {
				int posRow = (row * 9) + i;
				if (posRow != pos && (choices[posRow] &= notmasks[val]) == 0) {
					return false;
				}
				int posCol = (i * 9) + col;
				if (posCol != posRow && posCol != pos && (choices[posCol] &= notmasks[val]) == 0) {
					return false;
				}
				int cZone = (i / 3) + zoneCol;
				int rZone = (i % 3) + zoneRow;
				if (cZone != col && rZone != row) {
					int posZone = (rZone * 9) + cZone;
					if (posZone != pos && (choices[posZone] &= notmasks[val]) == 0) {
						return false;
					}
				}
			}
			return true;
		}

		protected Options clone() {
			Options o = new Options();
			System.arraycopy(choices, 0, o.choices, 0, 81);
			return o;
		}
	}

	int masks[] = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
	int notmasks[] = new int[] { ~1, ~2, ~4, ~8, ~16, ~32, ~64, ~128, ~256 };
	int alloptions = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256;
	int solutions = 0;

	public Sudoku[] search(int sudoku[], int maxSolutions) {
		solutions = 0;
		Options o = new Options(sudoku);
		Sudoku ret[] = new Sudoku[maxSolutions];
		search(0, o, ret);
		Sudoku ret2[] = new Sudoku[solutions];
		System.arraycopy(ret, 0, ret2, 0, solutions);
		return ret2;
	}

	private void search(int pos, Options o, Sudoku ret[]) {
		if (ret.length <= solutions) {
			return;
		}
		if (pos == 81) {
			ret[solutions++] = new Sudoku(o);
			return;
		}
		for (int i = 0; i < 9; i++) {
			if ((o.choices[pos] & masks[i]) > 0) {
				Options o2 = (Options) o.clone();
				if (o2.removeOption(pos, i)) {
					search(pos + 1, o2, ret);
				}
			}
		}
	}

	public Sudoku getRandom() {
		List<Integer> pos = new ArrayList<>();
		// Fill pos with all sudoku positions
		for (int p = 0; p < 81; p++)
			pos.add(p);
		// Randomize the order
		Collections.shuffle(pos);
		Sudoku sudo = new Sudoku();
		// Put numbers from 1 to 9 in random places, this will "branch" the
		// solver towards different solutions each time
		for (int i = 0; i < 9; i++) {
			int p = pos.get(i);
			sudo.cells[p] = i + 1;
		}
		//Based on the 9 first set numbers search for max 1000 possible solutions
		Sudoku solutions[] = search(sudo.cells, 1000);
		int r = new Random(System.currentTimeMillis()).nextInt(solutions.length);
		//Select one randomly
		return solutions[r];
	}

}
