package gee.sudoku.branchsolver;

import gee.sudoku.branchsolver.QuickSolver.Options;

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
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 81; i++) {
			if (cells[i] > 0) {
				sb.append(cells[i]);
			} else {
				sb.append(' ');
			}
			if (i % 9 == 8) {
				sb.append('\n');
			} else if (i % 3 == 2) {
				sb.append("|");
			} else {
				sb.append(' ');
			}
			if (i < 55 && i % 27 == 26) {
				sb.append("-----+-----+-----\n");
			}
		}
		return sb.toString();
	}
}
