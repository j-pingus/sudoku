package gee.sudoku.krn;

public class ActionStep {
	ActionType action;
	CellReference cellReference;
	int value;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ActionStep that = (ActionStep) o;

		if (value != that.value) return false;
		if (action != that.action) return false;
		return cellReference.equals(that.cellReference);

	}

	@Override
	public int hashCode() {
		int result = action.hashCode();
		result = 31 * result + cellReference.hashCode();
		result = 31 * result + value;
		return result;
	}

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

	public void apply(Matrice mat) {
		Cell cell = mat.getCell(cellReference);
		switch (action){
			case ADD_OPTION:
				cell.addChoice(value);
				break;
			case REMOVE_OPTION:
				cell.removeChoice(value);
				break;
			case SET_VALUE:
				cell.setValue(value);
				break;
			case UNSET_VALUE:
				cell.unsetValue();
				break;
		}
	}

	public void undo(Matrice mat) {
		Cell cell = mat.getCell(cellReference);
		switch (action){
			case ADD_OPTION:
				cell.removeChoice(value);
				break;
			case REMOVE_OPTION:
				cell.addChoice(value);
				break;
			case SET_VALUE:
				cell.unsetValue();
				break;
			case UNSET_VALUE:
				cell.setValue(value);
				break;
		}
	}
}