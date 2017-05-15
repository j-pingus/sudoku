package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerald on 30/04/17.
 */
public class SwordFish implements Strategy {
    @Override
    public MatriceAction getAction(Matrice matrice) {
        for (int choice = 1; choice <= 9; choice++) {
            MatriceZone zones[][] = getColsByChoiceMaxOccuring(matrice, choice, 3);
            if (zones == null) {
                zones = getRowsByChoiceMaxOccuring(matrice, choice, 3);
            }
            if (zones != null) {
                MatriceAction action = new MatriceAction(Strategies.SWORDFISH, zones[1]);
                for (MatriceZone row : zones[1])
                    action.removeChoices(matrice, row, new int[]{choice});
                action.doNotTouch(zones[0]);
                if (action.size() > 0)
                    return action;
            }
        }
        return null;
    }

    private MatriceZone[][] getColsByChoiceMaxOccuring(Matrice matrice, int choice, int count) {
        List<MatriceZone> cols = new ArrayList<>();
        List<MatriceZone> rows = new ArrayList<>();
        for (MatriceZone col : matrice.getCols()) {
            List<Cell> found = col.findChoice(choice);
            if (found.size() <= count)
                for (Cell cell : found) {
                    if (!cols.contains(col))
                        cols.add(col);
                    MatriceZone row = matrice.getRow(cell);
                    if (!rows.contains(row))
                        rows.add(row);
                }
        }
        if (cols.size() == 3 && rows.size() == 3) {
            return new MatriceZone[][]{
                    cols.toArray(new MatriceZone[0]),
                    rows.toArray(new MatriceZone[0])
            };
        }
        return null;
    }

    private MatriceZone[][] getRowsByChoiceMaxOccuring(Matrice matrice, int choice, int count) {
        List<MatriceZone> cols = new ArrayList<>();
        List<MatriceZone> rows = new ArrayList<>();
        for (MatriceZone row : matrice.getRows()) {
            List<Cell> found = row.findChoice(choice);
            if (found.size() <= count)
                for (Cell cell : found) {
                    if (!rows.contains(row))
                        rows.add(row);
                    MatriceZone col = matrice.getCol(cell);
                    if (!cols.contains(col))
                        cols.add(col);
                }
        }
        if (cols.size() == 3 && rows.size() == 3) {
            return new MatriceZone[][]{
                    rows.toArray(new MatriceZone[0]),
                    cols.toArray(new MatriceZone[0])
            };
        }
        return null;
    }
}
