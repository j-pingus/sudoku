package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 02/04/17.
 */
public class XWingRow implements Strategy {
    public boolean xWingRow() {
        boolean ret = false;
        return ret;
    }

    @Override
    public MatriceAction getAction(Matrice matrice) {
        for (MatriceZone row : matrice.getRows()) {
            for (int value : row.getChoices()) {
                if (row.countChoice(value) == 2) {
                    for (MatriceZone row2 : matrice.getRows()) {
                        if (row2 != row) {
                            if (row2.countChoice(value) == 2) {
                                Vector<Cell> p = row.findChoice(value);
                                if (row2.getCells().get(p.get(0).getCol())
                                        .hasChoice(value)) {
                                    if (row2.getCells().get(p.get(1).getCol())
                                            .hasChoice(value)) {
                                        Vector<Cell> xWing = new Vector<Cell>();
                                        xWing.addAll(p);
                                        xWing.addAll(row2.findChoice(value));
                                        MatriceAction action = new MatriceAction(Strategies.X_WING_ROW, row, row2);
                                        action.setHintValues(row.getChoices());
                                        for (Cell c : matrice.getCols()[p.get(0)
                                                .getCol()].getOptionCells()) {
                                            if (!xWing.contains(c))
                                                action.removeChoices(matrice, c, value);
                                        }
                                        for (Cell c : matrice.getCols()[p.get(1)
                                                .getCol()].getOptionCells()) {
                                            if (!xWing.contains(c))
                                                action.removeChoices(matrice, c, value);
                                        }
                                        if (action.size() > 0) {
                                            return action;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
