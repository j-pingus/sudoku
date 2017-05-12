package gee.sudoku.solver;

import gee.sudoku.io.MatriceFile;
import gee.sudoku.krn.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * Solves the SUDOKU matrices
 *
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: Solver.java,v $
 * @log Revision 1.2  2009/10/28 08:16:06  gev
 * @log from VE
 * @log
 * @log Revision 1.1 2009/09/30 21:40:31 gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.3 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log Revision 1.2 2009/07/01 09:36:32 gev With log,
 * @see gee.sudoku.krn.Matrice
 */
public class Solver {
    Matrice mat;
    List<Strategy> strategies = new ArrayList<Strategy>();
    Vector<Strategies> appliedStrategies;

    private Solver() {
        appliedStrategies = new Vector<Strategies>();
        registerStrategies();
    }

    public Solver(Matrice mat) {
        this();
        this.mat = mat;
    }

    public static void main(String[] args) throws Exception {
        System.out.print("Please enter a valid path for sudoku file:");
        Scanner scan = new Scanner(System.in);
        String sudokuFileName = scan.next();
        File sudoku = new File(sudokuFileName);
        Matrice mat = sudoku.exists() ? MatriceFile.read(sudoku)
                : new MatriceFile().getSudokuSample();
        Solver solver = new Solver(mat);
        MatriceHistory logger = new MatriceHistory();
        try {
            mat.setHistory(logger);
            solver.solve();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(mat.toStringExtended());
            System.out.println(solver.getAppliedStrategies());
            System.out.println(sudoku.getName());
        }
        mat.check();
        System.out.println("Status?" + mat.getStatus());
        System.out.println(mat.toStringExtended());
        System.out.println(logger);
    }

    private void registerStrategies() {
        strategies.add(new SoleOption());
        strategies.add(new NakedTuple(2, Strategies.NAKED_PAIR));
        strategies.add(new NakedTuple(3, Strategies.NAKED_TRIPLET));
        strategies.add(new NakedTuple(4, Strategies.NAKED_QUAD));
        strategies.add(new HiddenTuple(2, Strategies.HIDDEN_PAIR));
        strategies.add(new HiddenTuple(3, Strategies.HIDDEN_TRIPLET));
        strategies.add(new HiddenTuple(4, Strategies.HIDDEN_QUAD));
        strategies.add(new Candidates());
        strategies.add(new YWing());
        strategies.add(new XWingRow());
        strategies.add(new XWingCol());
        strategies.add(new SwordFish());
    }

    public void loop() {
        MatriceAction matriceAction;
        while ((matriceAction = getNextAction()) != null)
        {
            matriceAction.apply(mat);
        }
    }

    public MatriceAction getNextAction() {
        for (Strategy strategy : strategies) {
            long start = System.currentTimeMillis();
            System.out.println(strategy.getClass().getSimpleName());
            MatriceAction matriceAction = strategy.getAction(mat);
            System.out.println(strategy.getClass().getSimpleName() + " " + (System.currentTimeMillis() - start));
            if (matriceAction != null) {
                return matriceAction;
            }
        }
        return null;
    }


    public Matrice.Status solve() throws Exception {
        loop();
        return mat.check();
    }

    public Cell chooseCell(Matrice mat) {
        for (MatriceZone row : mat.getRows())
            for (Cell cell : row.getCells())
                if (cell.getChoices().length == 2)
                    return cell;
        return null;
    }

    public Vector<Strategies> getAppliedStrategies() {
        return appliedStrategies;
    }

}
