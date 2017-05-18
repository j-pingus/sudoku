package gee.sudoku.branchsolver;

class Options {
	static final int masks[] = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
	static final int notmasks[] = new int[] { ~1, ~2, ~4, ~8, ~16, ~32, ~64,
			~128, ~256 };
	static final int alloptions = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256;
	int choices[] = new int[81];

	public Options() {
		choices = new int[81];
		for (int i = 0; i < 81; i++) {
			choices[i] = alloptions;
		}
	}

	public Options(byte... cells) {
		this();
		for (int i = 0; i < cells.length && i < 81; i++) {
			if (cells[i] != 0) {
				choices[i] = masks[cells[i] - 1];
				removeOption(i, cells[i] - 1);
			}
		}
	}

	public boolean hasChoice(int pos, int value) {
		return (choices[pos] & masks[value]) > 0;
	}

	public byte value(int pos) {
		int val = choices[pos];
		for (byte i = 0; i < 9; i++) {
			if ((val & masks[i]) > 0) {
				return b(i + 1);
			}
		}
		return 0;
	}

	private byte b(int value) {
		return (byte) value;
	}

	protected boolean removeOption(int pos, int val) {
		int row = pos / 9;
		int col = pos % 9;
		int zoneCol = (col / 3) * 3;
		int zoneRow = (row / 3) * 3;
		byte rowCheck[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte colCheck[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte squareCheck[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < 9; i++) {
			int posRow = (row * 9) + i;
			if (posRow != pos && (choices[posRow] &= notmasks[val]) == 0) {
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

	private int getValue(int o) {
		for (int i = 0; i < 9; i++)
			if (o == masks[i])
				return i;
		return -1;
	}

	@Override
	protected Options clone() {
		Options o = new Options();
		System.arraycopy(choices, 0, o.choices, 0, 81);
		return o;
	}

	// This eats a lot of CPU... so don't change back to toString signature...
	public String toStringMe() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < choices.length; i++) {
			sb.append(toString(choices[i]));
			if (i % 9 == 8) {
				sb.append("\n");
			} else if (i % 3 == 2) {
				sb.append("|");
			}
			if (i % 27 == 26)
				sb.append("---------+---------+---------\n");
		}
		return sb.toString();
	}

	private Object toString(int val) {
		String ret = null;
		int count = 0;
		for (int i = 0; i < masks.length; i++) {
			if ((masks[i] & val) > 0) {
				count++;
				ret = " " + (i + 1) + " ";
			}
		}
		if (count > 1)
			return "{" + count + "}";
		return ret;
	}
}
