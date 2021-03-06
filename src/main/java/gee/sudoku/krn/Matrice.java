package gee.sudoku.krn;

import java.util.Vector;

import gee.sudoku.solver.Strategies;

/**
 * 
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: Matrice.java,v $
 * @log Revision 1.2  2009/10/28 08:16:06  gev
 * @log from VE
 * @log
 * @log Revision 1.1 2009/09/30 21:40:31 gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.1 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log
 */
public class Matrice implements Cloneable {
	public enum Status {
		UNSOLVED, SOLVED, SCREWED;
	};

	Status status = Status.UNSOLVED;

	public Status getStatus() {
		return status;
	}

	char reference[];
	Cell cells[][];
	MatriceZone rows[];
	MatriceZone cols[];
	MatriceZone squares[][];
	boolean throwing = true;
	MatriceHistory history;
	boolean historyOn = true;

	boolean isHistoryOn() {
		return historyOn;
	}

	void setHistoryOn(boolean historyOn) {
		this.historyOn = historyOn;
	}

	public void log(ActionType action, CellReference ref, int value) {
		if (history != null && historyOn)
			history.log(action, ref, value);
	}

	public Matrice(int size) {
		reference = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		// SquareSize
		int ss = (int) Math.sqrt(size);
		cells = new Cell[size][size];
		rows = new MatriceZone[size];
		cols = new MatriceZone[size];
		squares = new MatriceZone[ss][ss];
		for (int row = 0; row < size; row++) {
			rows[row] = new MatriceZone("Line " + (row + 1));
			cols[row] = new MatriceZone("Column " + (row + 1));

			for (int col = 0; col < size; col++) {
				if (cells[row][col] == null)
					cells[row][col] = new Cell(size, row, col);
				if (cells[col][row] == null)
					cells[col][row] = new Cell(size, col, row);
				if (squares[row % ss][col % ss] == null)
					squares[row % ss][col % ss] = new MatriceZone(String
							.format("Square (%d,%d)", (row % ss) + 1,
									(col % ss) + 1));
				rows[row].addCell(cells[row][col]);
				cols[row].addCell(cells[col][row]);
				squares[row / ss][col / ss].addCell(cells[row][col]);
			}
		}
	}

	/**
	 * checks the status of the matrice.
	 * 
	 * @see getStatus for the result of the check.
	 * @throws Exception
	 */
	public void check() throws Exception {
		check(throwing);
	}

	/**
	 * Checks the completeness of a matrice
	 * 
	 * @see getStatus for the result of the check.
	 * @param throwing
	 * @throws Exception
	 */
	public void check(boolean throwing) throws Exception {
		boolean ret = true;
		for (MatriceZone lines[] : squares) {
			ret = ret && check(lines, throwing);
		}
		ret = ret && check(rows, throwing);
		ret = ret && check(cols, throwing);
		if (ret == true)
			status = Status.SOLVED;
	}

	public boolean check(MatriceZone zones[], boolean throwing)
			throws Exception {
		for (MatriceZone zone : zones) {
			for (int value = 1; value <= 9; value++) {
				int val = zone.countValue(value);
				switch (val) {
				case 0:
					if (zone.countChoice(value) == 0) {
						if (throwing)
							throw new Exception(
									String
											.format(
													"Value %d found %d times in %s no options left for that value",
													value, val, zone.getName()));
						status = Status.SCREWED;
						return false;
					}
					status = Status.UNSOLVED;
					return false;
				case 1:
					// Ok continue
					break;
				default:
					if (throwing)
						throw new Exception(String.format(
								"Value %d found %d times in %s", value, val,
								zone.getName()));
					status = Status.SCREWED;
					return false;
				}
			}

		}
		return true;
	}

	public void init(int values[][]) {
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++) {
				setValue(i, j, values[i][j]);
			}
	}

	public int countValues() {
		int count = 0;
		for (MatriceZone aLine : rows) {
			count += aLine.countValues();
		}
		return count;
	}

	public int countOptions() {
		int count = 0;
		for (MatriceZone aLine : rows) {
			count += aLine.countOptions();
		}
		return count;
	}

	@Override
	public String toString() {
		String ret = "";
		for (MatriceZone aLine : rows) {
			ret += aLine + "\n";
		}
		ret += "(" + countValues() + " values)\n";
		return ret;
	}

	public String toStringExtended() {
		String ret = "";
		int r = 0;
		for (MatriceZone aLine : rows) {
			ret += aLine.toStringExtended() + "\n";
			if (++r % 3 == 0)
				ret += "\n";
		}
		ret += "(" + countValues() + " values)\n";
		return ret;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public MatriceZone[] getRows() {
		return rows;
	}

	public MatriceZone[] getCols() {
		return cols;
	}

	public MatriceZone[][] getSquares() {
		return squares;
	}

	public MatriceZone[] getZones() {
		MatriceZone ret[] = new MatriceZone[cols.length + rows.length
				+ (squares.length * squares.length)];
		int id = 0;
		for (MatriceZone zone : cols)
			ret[id++] = zone;
		for (MatriceZone zone : rows)
			ret[id++] = zone;
		for (MatriceZone zones[] : squares)
			for (MatriceZone zone : zones)
				ret[id++] = zone;
		return ret;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Matrice mat = new Matrice(cells.length);
		int ss = (int) Math.sqrt(cells.length);
		mat.cells = new Cell[cells.length][cells.length];
		mat.rows = new MatriceZone[cells.length];
		mat.cols = new MatriceZone[cells.length];
		mat.squares = new MatriceZone[ss][ss];
		for (int i = 0; i < cells.length; i++) {
			mat.rows[i] = new MatriceZone("Line " + (i + 1));
			mat.cols[i] = new MatriceZone("Column " + (i + 1));

			for (int j = 0; j < cells[i].length; j++) {
				if (mat.cells[i][j] == null)
					mat.cells[i][j] = (Cell) cells[i][j].clone();
				if (mat.cells[j][i] == null)
					mat.cells[j][i] = (Cell) cells[j][i].clone();
				if (mat.squares[i % ss][j % ss] == null)
					mat.squares[i % ss][j % ss] = new MatriceZone(String
							.format("Square (%d,%d)", (i % ss) + 1,
									(j % ss) + 1));
				mat.rows[i].addCell(mat.cells[i][j]);
				mat.cols[i].addCell(mat.cells[j][i]);
				mat.squares[i / ss][j / ss].addCell(mat.cells[i][j]);
			}
		}
		mat.setReferenceString(getReferenceString());
		if (history != null)
			mat.setHistory((MatriceHistory) history.clone());
		return mat;
	}

	public byte[] getValues() {
		byte ret[] = new byte[cells.length * cells.length];
		int i = 0;
		for (MatriceZone row : getRows())
			for (Cell cell : row.getCells())
				ret[i++] = (byte) cell.getValue();
		return ret;
	}

	public char[] getReference() {
		return reference;
	}

	public String getReferenceString() {
		String ret = "";
		for (char c : reference)
			ret += c;
		return ret;
	}

	public void setValue(Cell c, int value) {
		setValue(c.getRow(), c.getCol(), value);
	}

	public void setValue(int row, int col, int value) {
		if (value > 0) {
			if (cells[row][col].hasChoice(value)) {
				removeOtherChoices(cells[row][col], value);
				cells[row][col].setValue(value);
				log(ActionType.SET_VALUE, cells[row][col], value);
				removeRowChoices(row, value);
				removeColChoices(col, value);
				removeSquareChoices(row, col, value);
			}
		} else {
			unsetValue(row, col);
		}
	}

	public void unsetValue(int row, int col, int value) {
		if (value > 0) {
			cells[row][col].unsetValue();
			addRowChoices(row,value);
			addColChoices(col,value);
			addSquareChoices(row,col,value);
		} else {
			unsetValue(row, col);
		}
	}

	public boolean addChoice(int row, int col, int value) {
		if (cells[row][col].addChoice(value)) {
			log(ActionType.ADD_OPTION, cells[row][col], value);
			return true;
		}
		return false;
	}

	public boolean removeChoice(int row, int col, int value) {
		if (cells[row][col].removeChoice(value)) {
			log(ActionType.REMOVE_OPTION, cells[row][col], value);
			return true;
		}
		return false;
	}

	public void unsetValue(int row, int col) {
		int value = cells[row][col].getValue();
		if (value != 0) {
			log(ActionType.UNSET_VALUE, cells[row][col], value);
			cells[row][col].unsetValue();
			// addRowChoices(row, value);
			// addColChoices(col, value);
			// addSquareChoices(row, col, value);
		}
	}

	public boolean removeChoice(int value, Cell... cells) {
		boolean ret = false;
		for (Cell cell : cells) {
			if (removeChoice(cell.row, cell.col, value)) {
				ret |= true;
			}
		}
		return ret;
	}

	public boolean addChoice(int value, Cell... cells) {
		boolean ret = false;
		for (Cell cell : cells) {
			if (addChoice(cell.row, cell.col, value)) {
				ret |= true;
			}
		}
		return ret;
	}

	public boolean removeOtherChoices(Cell cell, int... values) {
		boolean ret = false;
		for (int value : cell.getOtherChoices(values))
			ret = removeChoice(value, cell) | ret;
		return ret;
	}

	public boolean removeChoices(Cell cell, int... values) {
		boolean ret = false;
		for (int value : values)
			ret = removeChoice(value, cell) | ret;
		return ret;
	}

	public boolean addChoices(Cell cell, int... values) {
		boolean ret = false;
		for (int value : values)
			ret = addChoice(value, cell) | ret;
		return ret;
	}

	public boolean removeSquareChoices(int row, int col, int... values) {
		return removeSquareChoices(row, col, true, values);
	}

	boolean removeSquareChoices(int row, int col, boolean withHistory,
			int... values) {
		boolean ret = false;
		for (Cell cell : getSquares()[row / 3][col / 3].getCells())
			ret = removeChoices(cell, values) | ret;
		return ret;
	}

	public boolean removeRowChoices(int row, int... values) {
		boolean ret = false;
		for (Cell cell : getRows()[row].getCells())
			ret = removeChoices(cell, values) | ret;
		return ret;
	}

	boolean removeColChoices(int col, int... values) {
		boolean ret = false;
		for (Cell cell : getCols()[col].getCells())
			ret = removeChoices(cell, values) | ret;
		return ret;
	}

	public boolean addSquareChoices(int row, int col, int... values) {
		boolean ret = false;
		for (Cell cell : getSquares()[row / 3][col / 3].getCells())
			ret = addChoices(cell, values) | ret;
		return ret;
	}

	public boolean addRowChoices(int row, int... values) {
		boolean ret = false;
		for (Cell cell : getRows()[row].getCells())
			ret = addChoices(cell, values) | ret;
		return ret;
	}

	public boolean addColChoices(int col, int... values) {
		boolean ret = false;
		for (Cell cell : getCols()[col].getCells())
			ret = addChoices(cell, values) | ret;
		return ret;
	}

	public void setReference(char[] reference) {
		this.reference = reference;
	}

	public void setReferenceString(String reference) {
		for (int i = 0; i < 9; i++)
			this.reference[i] = 0;
		for (int i = 0; i < reference.length(); i++)
			this.reference[i] = reference.charAt(i);
	}

	public boolean isThrowing() {
		return throwing;
	}

	public void setThrowing(boolean throwing) {
		this.throwing = throwing;
	}

	public void setHistory(MatriceHistory history) {
		this.history = history;
	}

	public void commit(Strategies s) {
		if (history != null)
			history.commit(s);
	}
	public CellReference[] getPairs(){
		Vector<CellReference> ret = new Vector<CellReference>();
		for (MatriceZone row : getRows()) {
			for (Cell cell : row.getCells()) {
				if (cell.countChoices()==2) {
					ret.add(cell);
				}
			}
		}
		return ret.toArray(new CellReference[0]);
		
	}
	public Vector<Cell> getCells(int value) {
		Vector<Cell> ret = new Vector<Cell>();
		for (MatriceZone row : getRows()) {
			for (Cell cell : row.getCells()) {
				if (cell.getValue() == value || cell.hasChoice(value)) {
					ret.add(cell);
				}
			}
		}
		return ret;
	}

	public MatriceHistory getHistory() {
		return history;
	}
}
