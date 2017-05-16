package gee.sudoku.krn;

import java.util.Vector;

import org.apache.log4j.Logger;

public class MatriceHistory implements Cloneable {
	Vector<MatriceAction> undoActions = new Vector<MatriceAction>();
	Vector<MatriceAction> redoActions = new Vector<MatriceAction>();
	Logger log = Logger.getLogger(MatriceHistory.class);

	public void add(MatriceAction action) {
		if (action.size() > 0) {
			undoActions.add(action);
			redoActions = new Vector<MatriceAction>();
			log.debug(String.format("%4d:%s", undoActions.size(), action));
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return undoActions.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		MatriceHistory newHisto = (MatriceHistory) super.clone();
		newHisto.undoActions = (Vector<MatriceAction>) undoActions.clone();
		newHisto.redoActions = (Vector<MatriceAction>) redoActions.clone();
		return newHisto;
	}

	public MatriceAction undo(Matrice mat) {
		if (undoActions.size() > 0) {
			MatriceAction a = undoActions.lastElement();
			a.undo(mat);
			undoActions.removeElement(a);
			redoActions.add(a);
			log.info(String.format("%4d:%4d - %s", undoActions.size(),
					redoActions.size(), a));
			return a;
		}
		return null;
	}

	public void redo(Matrice mat) {
		if (redoActions.size() > 0) {
			MatriceAction a = redoActions.lastElement();
			a.apply(mat);
			redoActions.removeElement(a);
			undoActions.add(a);
			log.info(String.format("%4d:%4d - %S", undoActions.size(),
					redoActions.size(), a));
		}
	}
}
