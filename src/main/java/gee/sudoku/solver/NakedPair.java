package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 31/03/17.
 */
public class NakedPair extends AbstractZoneStrategy {
    @Override
    public MatriceAction getAction(Matrice matrice, MatriceZone zone) {
        // Naked pair
        for (Cell cell : zone.getOptionCells())
            if (cell.getChoices().length == 2) {
                Integer pair[]=cell.getChoices();
                Vector<Cell> options = zone.findChoice(cell);
                if (options.size() == 2) {
                    MatriceAction matriceAction = new MatriceAction(Strategies.NAKED_PAIR,zone);
                    matriceAction.removeChoices(matrice, zone, pair[0], pair[1]);
                    matriceAction.doNotTouch(options.get(0),options.get(1));
                    if(matriceAction.size()>0){
                        matriceAction.setHintValues(pair[0],pair[1]);
                        return matriceAction;}
                }
            }
        return null;
    }

}
