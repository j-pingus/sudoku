package gee.sudoku.krn;

/**
 * 
 * @author Even
 * @version $Revision: 1.1 $
 * @log 
 * $Log: CellReference.java,v $
 * Revision 1.1  2009/09/30 21:40:31  gev
 * news
 *
 * Revision 1.1  2009/07/08 11:28:58  gev
 * refactored
 * 
 * Revision 1.1 2009/07/01 09:44:22 gev 
 * Header comment added with CVS link
 * 
 */
public class CellReference {
	int row;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CellReference that = (CellReference) o;

		if (row != that.row) return false;
		return col == that.col;

	}

	@Override
	public int hashCode() {
		int result = row;
		result = 31 * result + col;
		return result;
	}

	int col;

	public CellReference(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("Cell[%d,%d]:",row,col);
	}
}
