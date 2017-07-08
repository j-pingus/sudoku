package gee.sudoku.solver;


import gee.sudoku.krn.Matrice;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by gerald on 18/06/17.
 */
public class StrategySolverTest {
    private static final Logger LOG = Logger.getLogger(Solver.class);
    Matrice soleOption, nakedPair;
    private Matrice hiddenPair;

    @Before
    public void init() {
        soleOption = new Matrice(9);
        soleOption.init(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 3, 7,
                0, 8, 0, 2, 6, 3, 0, 0, 0,
                0, 0, 4, 0, 3, 0, 0, 0, 0,
                0, 0, 3, 9, 2, 0, 4, 0, 0,
                0, 0, 8, 0, 0, 6, 7, 0, 0,
                0, 3, 0, 0, 8, 1, 0, 0, 0,
                0, 1, 5, 0, 0, 0, 9, 2, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 6);
        nakedPair = new Matrice(9);
        nakedPair.init(
                9, 8, 0, 6, 0, 2, 0, 0, 3,
                0, 0, 1, 0, 0, 0, 0, 0, 0,
                0, 5, 0, 0, 0, 0, 0, 7, 0,
                0, 6, 0, 0, 8, 0, 0, 0, 2,
                8, 0, 0, 0, 0, 0, 0, 0, 0,
                3, 0, 0, 0, 4, 0, 5, 0, 9,
                7, 9, 0, 0, 0, 0, 0, 2, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                2, 0, 0, 7, 0, 5, 0, 4, 6);
        hiddenPair = new Matrice(9);
        hiddenPair.init(9, 0, 7, 0, 0, 0, 2, 0, 0,
                4, 0, 0, 9, 8, 7, 0, 0, 3,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 7, 6, 0, 0, 0, 0,
                0, 7, 6, 2, 4, 0, 0, 1, 0,
                5, 0, 0, 0, 1, 0, 0, 0, 8,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                8, 0, 0, 1, 3, 4, 0, 0, 7,
                0, 0, 4, 0, 0, 0, 1, 0, 0);
    }

    @Test
    public void testSolveSO() throws Exception {
        Solver
                solver = new Solver(soleOption);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, soleOption.getStatus());
        LOG.info("Solved one...");
    }

    @Test
    public void testSolveNP() throws Exception {
        Solver
                solver = new Solver(nakedPair);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, nakedPair.getStatus());
    }

    @Test
    public void testSolveHP() throws Exception {
        Solver
                solver = new Solver(hiddenPair);
        solver.solve();
    }

    @Test
    public void testSolveCBC() throws Exception {
        Matrice sudoku = new Matrice(9);
        sudoku.init(0, 0, 0, 0, 6, 0, 0, 7, 0, 7, 0, 1, 0, 0, 0, 2, 0,
                4, 9, 6, 0, 0, 0, 2, 0, 3, 0, 6, 0, 0, 0, 1, 0, 0, 0, 0, 0, 3,
                7, 9, 0, 0, 8, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 8, 0, 6, 0,
                5, 0, 2, 0, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0,
                0);
        Solver solver = new Solver(sudoku);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, sudoku.getStatus());

    }

    @Test
    public void testSolveYW() throws Exception {
        Matrice sudoku = new Matrice(9);
        sudoku.init(7, 1, 0, 0, 0, 0, 0, 5, 8, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 9, 0, 0, 0, 6, 0, 0, 0, 3, 0, 2, 0, 3, 0, 0, 0, 0, 0, 6, 0,
                9, 0, 4, 8, 2, 0, 0, 0, 0, 0, 0, 0, 1, 4, 0, 0, 4, 0, 3, 0, 7,
                0, 5, 0, 6, 0, 0, 0, 0, 9, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0,
                7);
        Solver solver = new Solver(sudoku);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, sudoku.getStatus());

    }

    @Test
    public void testSolveXWR() throws Exception {
        Matrice sudoku = new Matrice(9);
        sudoku.init(1, 2, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 8, 0, 0,
                7, 0, 0, 8, 0, 5, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 9, 0, 0, 7, 6,
                2, 0, 3, 0, 0, 0, 1, 0, 0, 4, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 1,
                0, 4, 0, 0, 4, 0, 0, 0, 0, 3, 0, 0, 6, 0, 5, 0, 0, 7, 0, 0, 0,
                0);
        Solver solver = new Solver(sudoku);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, sudoku.getStatus());

    }

    @Test
    public void testSolveXWC() throws Exception {
        Matrice sudoku = new Matrice(9);
        sudoku.init(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 5, 0, 9, 0, 2, 8,
                0, 0, 8, 0, 0, 3, 6, 0, 4, 0, 3, 2, 0, 0, 0, 0, 0, 0, 8, 0, 4,
                7, 0, 8, 0, 9, 3, 0, 5, 0, 0, 0, 0, 4, 0, 0, 1, 0, 0, 0, 0, 4,
                9, 0, 6, 0, 0, 0, 0, 0, 1, 0, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0);
        Solver solver = new Solver(sudoku);
        solver.solve();
        Assert.assertEquals(Matrice.Status.SOLVED, sudoku.getStatus());

    }

}