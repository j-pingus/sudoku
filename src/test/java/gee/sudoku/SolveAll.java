package gee.sudoku;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.apache.log4j.Logger;

import gee.sudoku.io.MatriceFile;
import gee.sudoku.krn.Matrice;
import gee.sudoku.solver.Solver;
import gee.sudoku.solver.Strategies;

/**
 * 
 * @author Even
 * @version $Revision: 1.3 $
 * @log $Log: SolveAll.java,v $
 * @log Revision 1.3 2009/10/28 08:16:06 gev
 * @log from VE
 * @log
 * @log Revision 1.2 2009/09/30 21:40:31 gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.1 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log
 */
public class SolveAll {
	private static final Logger LOG = Logger.getLogger(SolveAll.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Vector<File> success = new Vector<File>();
		Vector<File> screwed = new Vector<File>();
		Vector<File> fail = new Vector<File>();
		for (File sudoku : new File(".").listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".sudoku") || name.endsWith(".wordoku");
			}
		})) {
			Matrice mat = MatriceFile.read(sudoku);
			Solver solver = new Solver(mat);
			try {
				solver.solve();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error("Error", e);
				System.out.println(mat.toStringExtended());
				System.out.println(solver.getAppliedStrategies());
				System.out.println(sudoku.getName());
			}
			System.out.print(mat.getStatus() + ":" + sudoku.getName() + ":");
			switch (mat.getStatus()) {
			case SOLVED:
				success.add(sudoku);
				break;
			case UNSOLVED:
				fail.add(sudoku);
				break;
			case SCREWED:
				screwed.add(sudoku);
				break;
			}
			for (Strategies s : Strategies.values())
				System.out.print(s + ":"
						+ solver.getAppliedStrategies().contains(s) + ":");
			System.out.println(mat == null ? 81 : mat.countOptions());

		}
		System.out.println(
				String.format("Success(%d):%s", success.size(), success));
		System.out.println(String.format("Failures(%d):%s", fail.size(), fail));
		System.out.println(
				String.format("Screwed(%d):%s", screwed.size(), screwed));
	}

}
