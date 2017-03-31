package gee.sudoku.solver;

import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by gerald on 31/03/17.
 */
public class SolverTest {
    Matrice matrice;

    @Before
    public void setUp() throws Exception {
        matrice = new Matrice(9);
        matrice.init(new int[][]{
                new int[]{0, 0, 3, 9, 0, 0, 7, 6, 0},
                new int[]{0, 4, 0, 0, 0, 6, 0, 0, 9},
                new int[]{6, 0, 7, 0, 1, 0, 0, 0, 4},
//---
                new int[]{2, 0, 0, 6, 7, 0, 0, 9, 0},
                new int[]{0, 0, 4, 3, 0, 5, 6, 0, 0},
                new int[]{0, 1, 0, 0, 4, 9, 0, 0, 7},
//---
                new int[]{7, 0, 0, 0, 9, 0, 2, 0, 1},
                new int[]{3, 0, 0, 2, 0, 0, 0, 4, 0},
                new int[]{0, 2, 9, 0, 0, 8, 5, 0, 0}
        });
    }

    @Test
    public void testGetNextAction() throws Exception {
        System.out.println(matrice);
        Solver solver = new Solver(matrice);
        MatriceAction action;
        while ((action = solver.getNextAction()) != null) {
            action.apply(matrice);
            matrice.check();
            System.out.println(action.getStrategy());
        }
        System.out.println(matrice);

        Assert.assertEquals(Matrice.Status.SOLVED,matrice.getStatus());


    }
}