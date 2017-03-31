package gee.sudoku.krn;

public class ActionStep {
	ActionType action;
	CellReference cellReference;
	int value;

	public ActionType getAction() {
		return action;
	}

	public CellReference getCellReference() {
		return cellReference;
	}

	public int getValue() {
		return value;
	}

	public ActionStep(ActionType action,CellReference ref, int value) {
		this.action=action;
		this.cellReference=ref;
		this.value=value;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(String.format("Cell[%d,%d]:", cellReference.row+1,
				cellReference.col+1));
		b.append(action);
		b.append(" ");
		b.append(value);
		return b.toString();
	}
}