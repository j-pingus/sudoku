package gee.sudoku.solver;

/**
 * This list all the available startegies to solve a matrice.
 *
 * @author Even
 */
public enum Strategies {
    SOLE_OPTION(1),
    CANDIDATE_COL(2),
    CANDIDATE_ROW(2),
    NAKED_PAIR(2),
    NAKED_TRIPLET(2),
    NAKED_QUAD(2),
    HIDDEN_PAIR(3),
    HIDDEN_TRIPLET(4),
    HIDDEN_QUAD(5),
    X_WING_COL(8),
    X_WING_ROW(8),
    Y_WING(8),
    SWORDFISH(10),
    GUESSING(15),
    HUMAN(15);
    String description;
    int weight;

    Strategies(int weight) {
        this.weight = weight;
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
}
