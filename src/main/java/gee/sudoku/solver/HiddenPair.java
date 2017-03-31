package gee.sudoku.solver;

import gee.math.Combinatory;
import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 30/03/17.
 */
public class HiddenPair extends AbstractZoneStrategy {
    @Override
    public MatriceAction getAction(Matrice matrice, MatriceZone zone) {
        if (zone.getChoices().length > 2) {
            int zoneChoices[] = Combinatory.getInts(zone.getChoices());
            int[][] pairs = Combinatory.getPairs(zoneChoices);
            for (int pair[] : pairs) {
                Vector<Cell> options = zone.findCellsWithAnyChoice(pair);
                if (options.size() == 2) {
                    MatriceAction action = new MatriceAction(Strategies.HIDDEN_PAIR);
                    for (Cell subLoopCell : options) {
                        action.removeOtherChoices(matrice, subLoopCell, pair);
                    }
                    if(action.size()>0){
                        action.setHintValues(pair);
                        return action;
                    }
                }
            }
        }
        return null;
    }
}
