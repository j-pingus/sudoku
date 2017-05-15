package gee.sudoku.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

/**
 * Created by gerald on 02/04/17.
 */
public class Candidates implements Strategy {
	@Override
	public MatriceAction getAction(Matrice matrice) {
		for (MatriceZone squares[] : matrice.getSquares()) {
			for (MatriceZone square : squares) {
				Integer choices[] = square.getChoices();
				for (int choice : choices) {
					Vector<Cell> cells = square.findChoice(choice);
					int row = cells.get(0).getRow();
					int col = cells.get(0).getCol();
					boolean rowFound = true;
					boolean colFound = true;
					for (Cell cell : cells) {
						if (cell.getCol() != col)
							colFound = false;
						if (cell.getRow() != row)
							rowFound = false;
					}
					if (rowFound) {
						MatriceAction action = new MatriceAction(
								Strategies.CANDIDATE_ROW,
								matrice.getRows()[row]);
						action.setHintValues(new int[] { choice });
						for (Cell c : matrice.getRows()[row].getCells()) {
							if (!cells.contains(c))
								action.removeChoices(matrice, c, choice);
						}
						if (action.size() > 0) {
							return action;
						}
					}
					if (colFound) {
						MatriceAction action = new MatriceAction(
								Strategies.CANDIDATE_COL,
								matrice.getCols()[col]);
						action.setHintValues(new int[] { choice });
						for (Cell c : matrice.getCols()[col].getCells()) {
							if (!cells.contains(c))
								action.removeChoices(matrice, c, choice);
						}
						if (action.size() > 0) {
							return action;
						}
					}

				}
			}
		}
		for (MatriceZone row : matrice.getRows()) {
			Map<Integer, Integer> choiceSquare = new HashMap<>();
			for (Cell c : row.getCells()) {
				for (int choice : c.getChoices()) {
					Integer square = choiceSquare.get(choice);
					if (square == null) {
						choiceSquare.put(choice, c.getSquare());
					} else if (square != c.getSquare()) {
						choiceSquare.put(choice, -1);
					}
				}
			}
			for (Integer choice : choiceSquare.keySet()) {
				Integer square = choiceSquare.get(choice);
				if (square != null && square != -1) {
					MatriceZone squareZone = matrice.getSquares()[square
							/ 3][square % 3];
					MatriceAction action = new MatriceAction(
							Strategies.CANDIDATE_BOX_ROW, row, squareZone);
					action.setHintValues(new int[] { choice });
					for (Cell c : squareZone.getCells()) {
						action.removeChoices(matrice, squareZone,
								new Integer[] { choice });
						action.doNotTouch(row.getCells().toArray(new Cell[0]));
					}
					if (action.size() > 0) {
						return action;
					}
				}
			}
		}
		for (MatriceZone col : matrice.getCols()) {
			Map<Integer, Integer> choiceSquare = new HashMap<>();
			for (Cell c : col.getCells()) {
				for (int choice : c.getChoices()) {
					Integer square = choiceSquare.get(choice);
					if (square == null) {
						choiceSquare.put(choice, c.getSquare());
					} else if (square != c.getSquare()) {
						choiceSquare.put(choice, -1);
					}
				}
			}
			for (Integer choice : choiceSquare.keySet()) {
				Integer square = choiceSquare.get(choice);
				if (square != null && square != -1) {
					MatriceZone squareZone = matrice.getSquares()[square
							/ 3][square % 3];
					MatriceAction action = new MatriceAction(
							Strategies.CANDIDATE_BOX_COL, col, squareZone);
					action.setHintValues(new int[] { choice });
					for (Cell c : squareZone.getCells()) {
						action.removeChoices(matrice, squareZone,
								new Integer[] { choice });
						action.doNotTouch(col.getCells().toArray(new Cell[0]));
					}
					if (action.size() > 0) {
						return action;
					}
				}
			}
		}
		return null;
	}

}
