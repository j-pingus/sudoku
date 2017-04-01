package gee.sudoku.solver;

import gee.math.Combinatory;
import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.Vector;

/**
 * Created by gerald on 01/04/17.
 */
public class HiddenTuple extends AbstractZoneStrategy {
    int size;
    Strategies name;

    public HiddenTuple(int size, Strategies name) {
        this.size = size;
        this.name = name;
    }
    @Override
    public MatriceAction getAction(Matrice matrice, MatriceZone zone) {
        if (zone.getChoices().length >= size) {
            int zoneChoices[] = Combinatory.getInts(zone.getChoices());
            int[][] triplets = Combinatory.getTuples(size, zoneChoices);
            for (int triplet[] : triplets) {
                Vector<Cell> options = zone.findCellsWithAnyChoice(triplet);
                if (options.size() == size) {
                    MatriceAction matriceAction = new MatriceAction(name, zone);
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
}
