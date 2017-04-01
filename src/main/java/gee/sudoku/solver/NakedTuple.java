package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 31/03/17.
 */
public class NakedTuple extends AbstractZoneStrategy {
    int size;
    Strategies name;

    public NakedTuple(int size, Strategies name) {
        this.size = size;
        this.name = name;
    }

    @Override
    public MatriceAction getAction(Matrice matrice, MatriceZone zone) {
        // Naked pair
        for (Cell cell : zone.getOptionCells())
            if (cell.getChoices().length == size) {
                Integer pair[] = cell.getChoices();
                Vector<Cell> options = zone.findChoice(cell);
                if (options.size() == size) {
                    MatriceAction matriceAction = new MatriceAction(name, zone);
                    matriceAction.removeChoices(matrice, zone, pair);
                    matriceAction.doNotTouch(options.toArray(new Cell[0]));
                    if (matriceAction.size() > 0) {
                        matriceAction.setHintValues(pair);
                        return matriceAction;
                    }
                }
            }
        return null;
    }

}
