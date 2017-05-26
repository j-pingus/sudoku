package gee.sudoku.krn;

import java.util.Vector;

import gee.sudoku.solver.Strategies;

/**
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: Matrice.java,v $
 * @log Revision 1.2 2009/10/28 08:16:06 gev
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

	Status status = Status.UNSOLVED;
	char reference[];
	Cell cells[][];
	MatriceZone rows[];
	MatriceZone cols[];
	MatriceZone squares[][];
	private MatriceHistory history;

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
					squares[row % ss][col % ss] = new MatriceZone(String.format(
							"Square (%d,%d)", (row % ss) + 1, (col % ss) + 1));
				rows[row].addCell(cells[row][col]);
				cols[row].addCell(cells[col][row]);
				squares[row / ss][col / ss].addCell(cells[row][col]);
			}
		}
		setHistory(new MatriceHistory());

	}

	public Status getStatus() {
		return status;
	}

	public Cell getCell(CellReference ref) {
		return cells[ref.getRow()][ref.getCol()];
	}

	/**
	 * Checks the completeness of a matrice
	 *
	 * @throws Exception
	 * @see getStatus for the result of the check.
	 */
	public Status check() throws Exception {
		boolean ret = true;
		for (MatriceZone lines[] : squares) {
			ret = ret && check(lines);
		}
		ret = ret && check(rows);
		ret = ret && check(cols);
		if (ret == true)
			status = Status.SOLVED;
		return status;
	}

	public boolean check(MatriceZone zones[]) throws Exception {
		for (MatriceZone zone : zones) {
			for (int value = 1; value <= 9; value++) {
				int val = zone.countValue(value);
				switch (val) {
				case 0:
					if (zone.countChoice(value) == 0) {
						status = Status.SCREWED;
						return false;
					}
					status = Status.UNSOLVED;
					return false;
				case 1:
					// Ok continue
					break;
				default:
					status = Status.SCREWED;
					return false;
				}
			}

		}
		return true;
	}

	public void init(int... values) {
		int pos = 0;
		for (int value : values) {
			new MatriceAction(Strategies.HUMAN).setValue(this,
					new CellReference(pos / cells.length, pos++ % cells.length),
					value).apply(this);
		}
	}

	public void init(int values[][]) {
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values[i].length; j++) {
				new MatriceAction(Strategies.HUMAN)
						.setValue(this, new CellReference(i, j), values[i][j])
						.apply(this);
			}
	}

	public void init(byte... values) {
		for (int i = 0; i < values.length; i++) {
			new MatriceAction(Strategies.HUMAN)
					.setValue(this, new CellReference(i / 9, i % 9), values[i])
					.apply(this);
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
					mat.squares[i % ss][j % ss] = new MatriceZone(String.format(
							"Square (%d,%d)", (i % ss) + 1, (j % ss) + 1));
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

	public void setReference(char[] reference) {
		this.reference = reference;
	}

	public String getReferenceString() {
		String ret = "";
		for (char c : reference)
			ret += c;
		return ret;
	}

	public void setReferenceString(String reference) {
		for (int i = 0; i < 9; i++)
			this.reference[i] = 0;
		for (int i = 0; i < reference.length(); i++)
			this.reference[i] = reference.charAt(i);
	}

	public Cell[] getPairs() {
		Vector<CellReference> ret = new Vector<CellReference>();
		for (MatriceZone row : getRows()) {
			for (Cell cell : row.getCells()) {
				if (cell.countChoices() == 2) {
					ret.add(cell);
				}
			}
		}
		return ret.toArray(new Cell[0]);

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

	public void setHistory(MatriceHistory history) {
		this.history = history;
	}

	public MatriceZone getRow(CellReference ref) {
		return rows[ref.getRow()];
	}

	public MatriceZone getCol(CellReference ref) {
		return cols[ref.getCol()];
	}

	public MatriceZone getSquare(CellReference ref) {
		return squares[ref.getRow() / 3][ref.getCol() / 3];
	}

	public Cell findFirstPair() {
		for (MatriceZone row : getRows()) {
			for (Cell cell : row.getCells()) {
				if (cell.countChoices() == 2) {
					return cell;
				}
			}
		}
		return null;
	}

	public enum Status {
		UNSOLVED, SOLVED, SCREWED
	}
}
