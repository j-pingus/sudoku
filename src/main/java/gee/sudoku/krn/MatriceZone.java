package gee.sudoku.krn;

import gee.math.Combinatory;

import java.util.Vector;

/**
 * 
 * @author Even
 * @version $Revision: 1.2 $ 
 * @log $Log: MatriceZone.java,v $
 * @log Revision 1.2  2009/10/28 08:16:06  gev
 * @log from VE
 * @log
 * @log Revision 1.1  2009/09/30 21:40:31  gev
 * @log news
 * @log
 * @log Revision 1.1  2009/07/08 11:28:58  gev
 * @log refactored
 * @log
 * @log Revision 1.1  2009/07/01 09:44:22  gev
 * @log Header comment added with CVS link
 * @log
 */
public class MatriceZone {
	String name;
	public MatriceZone(String name) {
		this.name = name;
	}

	private Vector<Cell> cells;

	public void addCell(Cell aCell) {
		getCells().add(aCell);
	}

	public Vector<Cell> getCells() {
		if (cells == null)
			cells = new Vector<Cell>();
		return cells;
	}

	public Vector<Cell> getOptionCells() {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell cell : getCells()) {
			if (!cell.isSet()) {
				ret.add(cell);
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = "";
		for (Cell aCell : cells)
			ret += "[" + aCell.toStringExtended() + "]";
		return ret;
	}

	public String toStringExtended() {
		String ret = "";
		int c=0;
		for (Cell aCell : cells) {
			ret += aCell.toStringExtended() ;
			if(++c%3==0)
				ret+="  ";
		}
		return ret;
	}

	public int countValues() {
		int count = 0;
		for (Cell aCell : cells) {
			count += aCell.isSet() ? 1 : 0;
		}
		return count;
	}

	public int countOptions() {
		int count = 0;
		for (Cell aCell : cells) {
			count += aCell.getChoices().length;
		}
		return count;
	}

	public int countChoice(int value) {
		int count = 0;
		for (Cell aCell : cells) {
			if (aCell.hasChoice(value))
				count++;
		}
		return count;
	}

	public int countValue(int value) {
		int count = 0;
		for (Cell aCell : cells) {
			if (aCell.getValue() == value)
				count++;
		}
		return count;
	}

	public boolean hasValue(int value) {
		for (Cell aCell : cells) {
			if (aCell.getValue() == value)
				return true;
		}
		return false;
	}

	/**
	 * Returns the cell containing exactly the same choices
	 * 
	 * @param cell
	 * @return all cells in the zone whith same choices
	 */
	public Vector<Cell> findChoice(Cell cell) {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell lookup : cells) {
			if (cell.sameChoices(lookup))
				ret.add(lookup);
		}
		return ret;
	}

	/**
	 * returns cells having same choices (or more) in the zone
	 * 
	 * @param cell
	 * @param choices
	 * @return
	 */
	public Vector<Cell> findChoice(int... choices) {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell lookup : cells) {
			if (lookup.hasChoices(choices))
				ret.add(lookup);
		}
		return ret;

	}

	/**
	 * returns cells having some or all of the choices (but no others) in the
	 * zone
	 * 
	 * @param choices
	 * @return
	 */
	public Vector<Cell> findCellsWithChoices(int... choices) {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell lookup : getOptionCells()) {
			if (Combinatory.contains(choices, Combinatory.getInts(lookup
					.getChoices())))
				ret.add(lookup);
		}
		return ret;

	}

	/**
	 * returns cells having some or all of the choices (or others) in the zone
	 * 
	 * @param choices
	 * @return
	 */
	public Vector<Cell> findCellsWithAnyChoice(int... choices) {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell lookup : getOptionCells()) {
			for (int choice : choices) {
				if (lookup.hasChoice(choice)) {
					ret.add(lookup);
					break;
				}
			}
		}
		return ret;

	}

	public Vector<Cell> findCellsWithTwoOrMoreChoices(int... choices) {
		Vector<Cell> ret = new Vector<Cell>();
		for (Cell lookup : getOptionCells()) {
			if (lookup.countChoicesFrom(choices) >= 2) {
//				if (lookup.countChoicesNotFrom(choices) <= 1) {
					ret.add(lookup);
//				} else {
//					ret.clear();
//					return ret;
//				}
			}
		}
		return ret;
	}

	public Integer[] getChoices() {
		Vector<Integer> ret = new Vector<Integer>();
		for (Cell cell : cells) {
			for (int choice : cell.getChoices()) {
				if (!ret.contains(choice))
					ret.add(choice);
			}
		}
		return ret.toArray(new Integer[0]);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
