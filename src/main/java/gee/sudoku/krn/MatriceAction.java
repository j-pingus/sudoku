package gee.sudoku.krn;

import gee.sudoku.solver.Strategies;

import java.util.Vector;

public class MatriceAction {
    Strategies strategy;
    Vector<ActionStep> steps;

    public MatriceAction(Strategies strategy) {
        this.steps = new Vector<ActionStep>();
        this.strategy = strategy;
    }

    public Strategies getStrategy() {
        return strategy;
    }

    public Vector<ActionStep> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(strategy);
        b.append(" ");
        b.append(steps);
        return b.toString();
    }


    public MatriceAction setValue(Matrice mat, CellReference ref, int value) {
        if (value > 0) {
            if (mat.getCell(ref).hasChoice(value)) {
                removeOtherChoices(mat, ref, value);
                steps.add(new ActionStep(ActionType.SET_VALUE, ref, value));
                removeOtherChoices(mat, mat.getRow(ref), value);
                removeOtherChoices(mat, mat.getCol(ref), value);
                removeOtherChoices(mat, mat.getSquare(ref), value);
            }
        }
        return this;
    }

    public void removeOtherChoices(Matrice mat, CellReference ref, int ... values) {
        for (int other : mat.getCell(ref).getOtherChoices(values)) {
            removeChoice(mat, ref, other);
        }
    }

    private void removeOtherChoices(Matrice mat, MatriceZone zone, int ... values) {
        for (Cell ref : zone.getCells()) {
            removeOtherChoices(mat, ref, values);
        }
    }

    private MatriceAction removeChoice(Matrice mat, CellReference ref, int value) {
        if (mat.getCell(ref).hasChoice(value)) {
            steps.add(new ActionStep(ActionType.REMOVE_OPTION, ref, value));
        }
        return this;
    }

}