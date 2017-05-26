package gee.sudoku.branchsolver;

@FunctionalInterface
public interface SudokuTransformation {
	int newPos(int pos);
}
