package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

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
                        MatriceAction action = new MatriceAction(Strategies.CANDIDATE_ROW,matrice.getRows()[row]);
                        action.setHintValues(new int[]{choice});
                        for (Cell c : matrice.getRows()[row].getCells()) {
                            if (!cells.contains(c))
                                action.removeChoices(matrice,c,choice);
                        }
                        if (action.size()>0) {
                            return action;
                        }
                    }
                    if (colFound) {
                        MatriceAction action = new MatriceAction(Strategies.CANDIDATE_COL,matrice.getCols()[col]);
                        action.setHintValues(new int[]{choice});
                        for (Cell c : matrice.getCols()[col].getCells()) {
                            if (!cells.contains(c))
                                action.removeChoices(matrice,c,choice);
                        }
                        if (action.size()>0) {
                            return action;
                        }
                    }

                }
            }
        }
        return null;
    }


}
