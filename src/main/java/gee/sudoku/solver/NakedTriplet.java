package gee.sudoku.solver;

import gee.math.Combinatory;
import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 31/03/17.
 */
public class NakedTriplet extends AbstractZoneStrategy {
    @Override
    public MatriceAction getAction(Matrice matrice, MatriceZone zone) {
        // Naked triplet
        if (zone.getChoices().length > 3) {
            int zoneChoices[] = Combinatory.getInts(zone.getChoices());
            int[][] triplets = Combinatory.getTriplets(zoneChoices);
            for (int triplet[] : triplets) {
                Vector<Cell> options = zone.findCellsWithChoices(triplet);
                if (options.size() == 3) {
                    MatriceAction matriceAction = new MatriceAction(Strategies.NAKED_TRIPLET, zone);
                    matriceAction.removeChoices(matrice, zone, triplet);
                    for (Cell cell : options) {
                        matriceAction.doNotTouch(cell);
                        matriceAction.removeOtherChoices(matrice, cell, triplet);
                    }
                    if(matriceAction.size()>0){
                        matriceAction.setHintValues(triplet);
                        return matriceAction;
                    }
                }
            }
        }
    return null;

    }

    public boolean nakedTriplet(MatriceZone zone) {
        boolean ret = false;
        return ret;
    }

}
