package gee.sudoku.solver;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceAction;
import gee.sudoku.krn.MatriceZone;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Solves the SUDOKU matrices
 *
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: Solver.java,v $
 * @log Revision 1.2 2009/10/28 08:16:06 gev
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
    private static final Logger LOG = Logger.getLogger(Solver.class);
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
        while ((matriceAction = getNextAction()) != null) {
            matriceAction.apply(mat);
        }
    }

    public MatriceAction getNextAction() {
        for (Strategy strategy : strategies) {
            long start = System.currentTimeMillis();
            MatriceAction matriceAction = strategy.getAction(mat);
            if (matriceAction != null) {
                LOG.info(strategy.getClass().getSimpleName() + " " + matriceAction.getStrategy() + " "
                        + (System.currentTimeMillis() - start));
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
