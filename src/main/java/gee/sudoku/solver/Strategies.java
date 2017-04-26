package gee.sudoku.solver;

/**
 * This list all the available startegies to solve a matrice.
 * 
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: Strategies.java,v $
 * @log Revision 1.2 2009/10/28 08:16:06 gev
 * @log from VE
 * @log
 * @log Revision 1.1 2009/09/30 21:40:31 gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.1 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log
 */
public enum Strategies {
	NAKED_PAIR, NAKED_TRIPLET, NAKED_QUAD, NAKED_QUINT, NAKED_SEXTE, HIDDEN_PAIR, HIDDEN_TRIPLET, HIDDEN_QUAD, HIDDEN_QUINT, X_WING_COL, X_WING_ROW, GUESSING_FIRST, GUESSING_SECOND, SOLE_OPTION, REMOVE_OPTIONS, CANDIDATE_COL, CANDIDATE_ROW, Y_WING, HUMAN;
	String description;

	Strategies() {
		String key = Strategies.class.getName() + "." + toString();
		try {
			description = "TODO, i.e. read from HTML or properties";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			description = e.getLocalizedMessage();
		}
	}

	public String getDescription() {
		return description;
	}
};
