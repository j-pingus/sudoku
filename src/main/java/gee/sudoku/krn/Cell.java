package gee.sudoku.krn;

import java.util.Vector;

/**
 * 
 * @author Even
 * @version $Revision: 1.1 $ $Log: Cell.java,v $
 * @version $Revision: 1.1 $ Revision 1.1  2009/09/30 21:40:31  gev
 * @version $Revision: 1.1 $ news
 * @version $Revision: 1.1 $
 * @version $Revision: 1.1 $ Revision 1.1 2009/07/08 11:28:58 gev
 * @version $Revision: 1.1 $ refactored
 * @version $Revision: 1.1 $
 * @version $Revision: 1.1 $ Revision 1.1 2009/07/01 09:44:22 gev
 * @version $Revision: 1.1 $ Header comment added with CVS link
 * @version $Revision: 1.1 $
 */
public class Cell extends CellReference implements Cloneable {
	int value;
	boolean choices[];

	public Cell(int size, int row, int col) {
		super(row, col);
		choices = new boolean[size];
		for (int i = 0; i < choices.length; i++)
			choices[i] = true;
	}

	/**
	 * Test purpose constructor...
	 * 
	 * @param size
	 * @param x
	 * @param y
	 * @param values
	 */
	public Cell(int size, int x, int y, int... values) {
		this(size, x, y);
		if (values.length == 1) {
			setValue(values[0]);
		}
		if (values.length > 1) {
			for (int value : getOtherChoices(values))
				removeChoice(value);
		}
	}

	public boolean isSet() {
		return value != 0;
	}

	public int getValue() {
		return value;
	}

	protected void setValue(int value) {
		this.value = value;
		if (value > 0)
			for (int i = 0; i < choices.length; i++)
				choices[i] = false;
	}
	protected void unsetValue(){
		for (int i = 0; i < choices.length; i++)
			choices[i] = true;
		choices[4] = false;
		this.value=0;
	}
	public int countChoices() {
		return getChoices().length;
	}

	protected Integer[] getOtherChoices(int... values) {
		Vector<Integer> ret = new Vector<Integer>();
		for (int i = 0; i < choices.length; i++)
			if (choices[i])
				ret.add(i + 1);
		for (Integer value : values)
			if (ret.contains(value))
				ret.remove(value);
		return ret.toArray(new Integer[] {});
	}

	protected boolean removeChoice(int value) {
		boolean ret = false;
		if (value > 0) {
			ret = choices[value - 1];
			choices[value - 1] = false;
		}
		return ret;
	}
	protected boolean addChoice(int value){
		boolean ret=false;
		if(value>0){
			ret = !choices[value - 1];
			choices[value - 1] = true;
		}
		return ret;
	}

	@Override
	public String toString() {
		if (value == 0)
			return " ";
		return "" + value;
	}

	public String toStringExtended() {
		String ret = "";
		if (!isSet()) {
			for (int i = 0; i < choices.length; i++)
				if (choices[i]) {
					ret += (i + 1);
				} else {
					ret += ".";
				}
			ret = "(" + ret + ")";
		}
		return String.format("%d%-11s", value, ret);
	}

	public int countChoicesFrom(int... choices) {
		int ret = 0;
		for (int choice : choices) {
			if (hasChoice(choice))
				ret++;
		}
		return ret;
	}

	public int countChoicesNotFrom(int... choices) {
		int ret = countChoices() - countChoicesFrom(choices);
		return ret;
	}

	public boolean hasChoice(int value) {
		if (value == 0)
			return false;
		return choices[value - 1];
	}

	public boolean hasChoices(int values[]) {
		for (int value : values)
			if (!hasChoice(value))
				return false;
		return true;
	}

	public Integer[] getChoices() {
		Vector<Integer> ret = new Vector<Integer>();
		for (int i = 0; i < choices.length; i++)
			if (choices[i])
				ret.add(i + 1);
		return ret.toArray(new Integer[] {});
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Cell cell = new Cell(choices.length, row, col);
		cell.value = getValue();
		for (int i = 0; i < choices.length; i++)
			cell.choices[i] = choices[i];
		return cell;
	}

	public boolean sameChoices(Cell cell) {
		if (cell.getChoices().length != getChoices().length)
			return false;
		for (int choice : cell.getChoices()) {
			if (!hasChoice(choice))
				return false;
		}
		return true;
	}
}
