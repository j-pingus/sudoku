package gee.sudoku.solver;

import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;

/**
 * Created by gerald on 30/03/17.
 */
public abstract class AbstractZoneStrategy implements Strategy {
    public MatriceAction getAction(Matrice matrice) {
        for(MatriceZone zone:matrice.getZones()){
            MatriceAction action = getAction(matrice, zone);
            if(action!=null){
                return action;
            }
        }
        return null;
    }
    public abstract MatriceAction getAction(Matrice matrice, MatriceZone zone);
}
