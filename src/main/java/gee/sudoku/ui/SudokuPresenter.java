package gee.sudoku.ui;

import gee.sudoku.krn.CellReference;
import gee.sudoku.krn.Matrice;
import gee.sudoku.solver.Strategies;

/**
 * 
 * @author Even
 * @version $Revision: 1.2 $ 
 * @log $Log: SudokuPresenter.java,v $
 * @log Revision 1.2  2009/09/30 21:40:31  gev
 * @log news
 * @log
 * @log Revision 1.1  2009/07/08 11:28:58  gev
 * @log refactored
 * @log
 * @log Revision 1.1  2009/07/01 09:44:22  gev
 * @log Header comment added with CVS link
 * @log
 */
public interface SudokuPresenter {
	public void show(Matrice m);

	public void kill();

	public SudokuPresenter duplicate();

	public void showMessage(Strategies s, CellReference... cellReferences);

	public boolean confirmMessage(Strategies s,
			CellReference... cellReferences);

}
