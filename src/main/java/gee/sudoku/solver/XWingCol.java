package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 02/04/17.
 */
public class XWingCol implements Strategy {
    @Override
    public MatriceAction getAction(Matrice matrice) {
        for (MatriceZone col : matrice.getCols()) {
            for (int value : col.getChoices()) {
                if (col.countChoice(value) == 2) {
                    for (MatriceZone col2 : matrice.getCols()) {
                        if (col2 != col) {
                            if (col2.countChoice(value) == 2) {
                                Vector<Cell> p = col.findChoice(value);
                                if (col2.getCells().get(p.get(0).getRow())
                                        .hasChoice(value)) {
                                    if (col2.getCells().get(p.get(1).getRow())
                                            .hasChoice(value)) {
                                        Vector<Cell> xWing = new Vector<Cell>();
                                        xWing.addAll(p);
                                        xWing.addAll(col2.findChoice(value));
                                        MatriceAction action = new MatriceAction(Strategies.X_WING_COL,col,col2);
                                        action.setHintValues(col.getChoices());
                                        for (Cell c : matrice.getRows()[p.get(0)
                                                .getRow()].getOptionCells()) {
                                            if (!xWing.contains(c))
                                                action.removeChoices(matrice,c,value);
                                        }
                                        for (Cell c : matrice.getRows()[p.get(1)
                                                .getRow()].getOptionCells()) {
                                            if (!xWing.contains(c))
                                                action.removeChoices(matrice,c,value);
                                        }
                                        if (action.size()>0) {
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
    public boolean xWingCol() {
        boolean ret = false;
        return ret;
    }

}
