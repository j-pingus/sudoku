package gee.sudoku.branchsolver;

import static org.junit.Assert.assertEquals;

public class Sudoku {
	byte cells[];
	int size;
	int squaredSize;
	int threeSquaresSize;

	public Sudoku(int size) {
		this.size = size;
		this.squaredSize = (int) Math.sqrt(size);
		this.threeSquaresSize = squaredSize * size;
		cells = new byte[size * size];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = 0;
		}
	}

	public Sudoku(int size, byte... init) {
		this(size);
		for (int i = 0; i < init.length && i < 81; i++) {
			this.cells[i] = init[i];
		}
	}

	// Most sudoku are 9 position based, not all.
	public Sudoku(byte... init) {
		this(9, init);
	}

	public Sudoku(Options c) {
		this((int) Math.sqrt(c.choices.length));
		for (int i = 0; i < cells.length; i++) {
			this.cells[i] = c.value(i);
		}
	}

	public byte[] getCells() {
		return cells;
	}

	public String getFileName() {
		StringBuilder sb = new StringBuilder();
		for (byte b : cells) {
			sb.append(b);
		}
		sb.append(".sudoku");
		return sb.toString();
	}

	// This eats a lot of CPU... so don't change back to toString signature...
	public String toStringMe() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] > 0) {
				sb.append(cells[i]);
			} else {
				sb.append(' ');
			}
			if (i % size == (size - 1)) {
				sb.append('\n');
			} else if (i % squaredSize == (squaredSize - 1)) {
				sb.append("|");
			} else {
				sb.append(' ');
			}

			if (i <= (threeSquaresSize * 2)
					&& i % threeSquaresSize == (threeSquaresSize - 1)) {
				sb.append("-----+-----+-----\n");
			}
		}
		return sb.toString();
	}

	public int score() {
		int ret = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] > 0)
				ret += i;
		}
		return ret;
	}

	public Sudoku transform(SudokuTransformation transfo) {
		byte dest[] = new byte[cells.length];
		for (int i = 0; i < cells.length; i++) {
			dest[transfo.newPos(i)] = cells[i];
		}
		return new Sudoku(dest);
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public int mirrorH(int pos) {
		assertEquals(size, 9);
		// FIXME: figure out the values for -4 and -2 if size != 9
		return pos + (((pos % size) - 4) * -2);
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public int flip(int i) {
		return (((i % size) * size) + ((i / size) % size));
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public int mirrorV(int i) {
		assertEquals(size, 9);
		// FIXME: figure out the values for -4 and -18 if size != 9
		return i + (((i / size) - 4) * -18);
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public int invert(int i) {
		return cells.length - 1 - i;
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public static int none(int i) {
		return i;
	}

	/**
	 * Lambda for transform
	 * 
	 * @param pos
	 * @return
	 */
	public int rotateH(int i, int rowDelta) {
		int row = ((i / size) + rowDelta + size) % size;
		int col = i % size;
		return (row * 9) + col;
	}

	/**
	 * get possible transformations function on sudoku and their opposite in an
	 * array of arrays [ [function,inverseFunction], [funciton2,
	 * inverseFunciton2], ... ] Those may be used with the transform method on
	 * sudoku
	 * 
	 * @return
	 */
	SudokuTransformation[][] getAllTransformations() {
		return new SudokuTransformation[][] { { this::flip, this::flip },
				{ this::mirrorV, new SudokuTransformation() {
					// Just showing off that Lambda brings nothing to the
					// concept except syntax sugar...
					@Override
					public int newPos(int pos) {
						return mirrorV(pos);
					}

				} }, { this::mirrorH, this::mirrorH },
				{ this::invert, this::invert },
				{ (i) -> this.rotateH(i, -squaredSize),
						(i) -> this.rotateH(i, squaredSize) },
				{ (i) -> this.rotateH(i, squaredSize),
						(i) -> this.rotateH(i, -squaredSize) }

		};
	}

	public SudokuTransformation[] getLowScoreTransformations() {
		int score = score();
		SudokuTransformation ret[] = { Sudoku::none, Sudoku::none };
		for (SudokuTransformation transfo[] : getAllTransformations()) {
			Sudoku sudo = transform(transfo[0]);
			int score2 = sudo.score();
			if (score2 < score) {
				score = score2;
				ret = transfo;
			}
		}
		return ret;
	}
}
