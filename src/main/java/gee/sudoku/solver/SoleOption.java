package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

/**
 * Created by gerald on 30/03/17.
 */
public class SoleOption extends AbstractZoneStrategy {
	@Override
	public MatriceAction getAction(Matrice mat, MatriceZone zone) {
		for (Cell c : zone.getCells()) {
			for (int value : c.getChoices()) {
				if (c.countChoices() == 1) {
					return new MatriceAction(Strategies.SOLE_OPTION_CELL, zone)
							.setValue(mat, c, value);
				}
				if (zone.countChoice(value) == 1) {
					return new MatriceAction(Strategies.SOLE_OPTION_ZONE, zone)
							.setValue(mat, c, value);
				}
			}
		}
		return null;
	}

}
