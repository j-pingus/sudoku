package gee.sudoku.solver;

import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

/**
 * Created by gerald on 30/03/17.
 */
public interface Strategy {
    public MatriceAction getAction(Matrice matrice);
}
