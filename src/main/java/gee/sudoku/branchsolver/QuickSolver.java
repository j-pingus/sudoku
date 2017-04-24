package gee.sudoku.branchsolver;

import java.util.Arrays;

public class QuickSolver {
	class Sudoku {
		int cells[] = new int[81];

		public Sudoku() {
			cells = new int[81];
			for (int i = 0; i < 81; i++) {
				cells[i] = 0;
			}
		}

		public Sudoku(int... init) {
			this();
			for (int i = 0; i < cells.length && i < 81; i++) {
				this.cells[i] = init[i];
			}
		}

		public Sudoku(Options c) {
			this();
			for (int i = 0; i < 81; i++) {
				this.cells[i] = c.value(i);
			}
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(81);
			for(int i=0;i<81;i++){
				sb.append(cells[i]);
				if(i%9==8){
					sb.append('\n');
				}else{
					sb.append(' ');
				}
			}
			return sb.toString();
		}
	}

	private class Options {
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

}
