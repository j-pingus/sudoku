package gee.sudoku;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class HowMuchSolutions3 {
	int masks[] = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
	int notmasks[] = new int[] { ~1, ~2, ~4, ~8, ~16, ~32, ~64, ~128, ~256 };
	int alloptions = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256;
	double solutions = 0D;

	HowMuchSolutions3() {
	}

	public static void main(String[] args) throws IOException {
		final HowMuchSolutions3 sols = new HowMuchSolutions3();
		int sudoku[] = new int[] {

		4, 0, 0, 1, 0, 0, 0, 0, 0,

		0, 0, 0, 2, 0, 9, 0, 0, 0,

		0, 0, 0, 0, 0, 0, 6, 3, 0,

		0, 9, 5, 0, 0, 6, 0, 0, 0,

		0, 7, 0, 0, 3, 0, 0, 0, 0,

		0, 0, 0, 0, 0, 0, 1, 0, 8,

		0, 0, 9, 0, 0, 0, 0, 5, 0,

		0, 0, 0, 0, 8, 1, 0, 0, 0,

		0, 3, 7, 0, 0, 0, 0, 9, 0

		};
		int all[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		sols.search(all);
	}

	long start;

	private void search(int sudoku[]) {
		start = System.currentTimeMillis();
		int choices[] = new int[81];
		for (int i = 0; i < 81; i++) {
			choices[i] = alloptions;
		}
		for (int i = 0; i < 81; i++) {
			if (sudoku[i] != 0) {
				choices[i] = masks[sudoku[i] - 1];
				removeOption(choices, i, sudoku[i] - 1);
			}
		}
		search(0, choices);
		System.out.println("Solutions : " + solutions);
		// print();
	}

	private void search(int pos, int[] choices) {
		if (pos == 81) {
			solutions++;
			if (solutions == 1 || solutions % 1000000 == 0) {
				System.out.printf("%,.0f solutions in %,.3f s\n", solutions,
						(System.currentTimeMillis() - start) / 1000f);
				print(choices);
			}
			return;
		}
		for (int i = 0; i < 9; i++) {
			if ((choices[pos] & masks[i]) > 0) {
				int[] choices2 = new int[81];
				System.arraycopy(choices, 0, choices2, 0, 81);
				if (removeOption(choices2, pos, i)) {
					search(pos + 1, choices2);
				}
			}
		}
	}

	private void print(int[] choices) {
		for (int i = 0; i < 81; i++) {
			print(choices[i]);
			System.out.print(" | ");
			if (i % 9 == 8)
				System.out.println();
		}
		System.out.println("---------");

	}

	private boolean removeOption(int[] choices, int pos, int val) {
		int row = pos / 9;
		int col = pos % 9;
		int zoneCol = (col / 3) * 3;
		int zoneRow = (row / 3) * 3;
		for (int i = 0; i < 9; i++) {
			int posRow = (row * 9) + i;
			if (posRow != pos
					&& (choices[posRow] &= notmasks[val]) == 0) {
				return false;
			}
			int posCol = (i * 9) + col;
			if (posCol != posRow && posCol != pos
					&& (choices[posCol] &= notmasks[val]) == 0) {
				return false;
			}
			int cZone = (i / 3) + zoneCol;
			int rZone = (i % 3) + zoneRow;
			if (cZone != col && rZone != row) {
				int posZone = (rZone * 9) + cZone;
				if (posZone != pos
						&& (choices[posZone] &= notmasks[val]) == 0) {
					return false;
				}
			}
		}
		return true;
	}

	private void print(int val) {
		for (int i = 0; i < 9; i++) {
			if ((val & masks[i]) > 0) {
				System.out.print((i + 1));
			} else {
				System.out.print(" ");
			}
		}
	}
}
