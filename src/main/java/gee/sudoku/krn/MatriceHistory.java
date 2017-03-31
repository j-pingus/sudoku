package gee.sudoku.krn;

import java.util.Vector;

import gee.sudoku.solver.Strategies;
import org.apache.log4j.Logger;


public class MatriceHistory implements Cloneable {
	MatriceAction currentAction;
	Vector<MatriceAction> undoActions = new Vector<MatriceAction>();
	Vector<MatriceAction> redoActions = new Vector<MatriceAction>();
	Logger log = Logger.getLogger(MatriceHistory.class);


	public void log(ActionType action, CellReference ref, int value) {
		ActionStep a = new ActionStep(action,ref,value);
		getCurrentAction().add(a);
		log.debug(String.format(" -=- %s %d (r%d:c%d) -=-", action, value, ref
				.getRow(), ref.getCol()));
	}

	public void commit(Strategies s) {
		if (getCurrentAction().size() > 0) {
			getCurrentAction().strategy = s;
			undoActions.add(getCurrentAction());
			redoActions = new Vector<MatriceAction>();
			log.info(String.format("%4d:%s", undoActions.size(), currentAction));
			currentAction = null;
		}
	}

	private MatriceAction getCurrentAction() {
		if (currentAction == null) {
			currentAction = new MatriceAction(Strategies.HUMAN);
		}
		return currentAction;
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
		mat.setHistoryOn(false);
		try {
			if (undoActions.size() > 0) {
				MatriceAction a = undoActions.lastElement();
				a.undo(mat);
				undoActions.removeElement(a);
				redoActions.add(a);
				log.info(String.format("%4d:%4d - %s", undoActions.size(), redoActions.size(),a));
				return a;
			}
		} finally {
			mat.setHistoryOn(true);
		}
		return null;
	}

	public void redo(Matrice mat) {
		mat.setHistoryOn(false);
		try {
			if (redoActions.size() > 0) {
				MatriceAction a = redoActions.lastElement();
				a.apply(mat);
				redoActions.removeElement(a);
				undoActions.add(a);
				log.info(String.format("%4d:%4d - %S", undoActions.size(), redoActions.size(),a));
			}
		} finally {
			mat.setHistoryOn(true);
		}
	}
}
