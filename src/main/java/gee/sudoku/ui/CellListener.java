package gee.sudoku.ui;

public interface CellListener {
	public void unsetHint(int row,int col,int value);
	public void setHint(int row,int col,int value);
	public void setValue(int row,int col,int value);
	public void unsetValue(int row, int col, int value);
}
