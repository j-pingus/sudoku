package gee.sudoku;

public class HowMuchSolutions {
	int matrix[][] = new int[][] { { -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, }, };

	public static void main(String[] args) {
		HowMuchSolutions sols = new HowMuchSolutions();
		while(!sols.search(0)){
			sols.print();
		}
		sols.print();
	}

	private void print() {

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				System.out.print(matrix[x][y] + 1);
				if(y%3==2)System.out.print("|");
			}
			System.out.println();
			if(x%3==2)System.out.println("---+---+---");
		}
		System.out.println("------------------------------");
	}

	private boolean search(int pos) {
		if (pos == 81)
			return true;
		for (int val = 0; val < 9; val++) {
			if (!searchColumn(val, pos / 9, pos % 9)
					&& !searchRow(val, pos / 9, pos % 9)
					&& !searchZone(val, pos / 9, pos % 9)) {
				matrix[pos / 9][pos % 9] = val;
				boolean ret= search(pos+1);
				if(ret)return ret;
			}
		}
		matrix[pos / 9][pos % 9] = -1;
		return false;
	}

	private boolean searchZone(int currVal, int x, int y) {
		int startX = (x / 3) * 3;
		int startY = (y / 3) * 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (startX + i != x || startY + j != y)
					if (matrix[startX + i][startY + j] == currVal)
						return true;
			}
		}
		return false;
	}

	private boolean searchColumn(int currVal, int x, int y) {
		for (int i = 0; i < 9; i++) {
			if (i != x)
				if (matrix[i][y] == currVal)
					return true;
		}
		return false;
	}

	private boolean searchRow(int currVal, int x, int y) {
		for (int i = 0; i < 9; i++) {
			if (i != y)
				if (matrix[x][i] == currVal)
					return true;
		}
		return false;
	}
}
