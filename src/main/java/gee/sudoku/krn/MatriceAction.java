package gee.sudoku.krn;

import gee.sudoku.solver.Strategies;

import java.util.Vector;

public class MatriceAction {
    Strategies strategy;
    private Vector<ActionStep> steps;
    private MatriceZone[] hints;
    private int[] hintValues;

    public int[] getHintValues() {
        return hintValues;
    }

    public MatriceAction setHintValues(int... hintValues) {
        this.hintValues = hintValues;
        return this;
    }

    public MatriceZone[] getHints() {
        return hints;
    }

    public MatriceAction(Strategies strategy,MatriceZone ... hints) {
        this.steps = new Vector<ActionStep>();
        this.strategy = strategy;
        this.hints =  hints;
    }

    public Strategies getStrategy() {
        return strategy;
    }
    public void add(ActionStep step){
        if(!steps.contains(step)){
            steps.add(step);
        }
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
            setHintValues(value);
            if (mat.getCell(ref).hasChoice(value)) {
                add(new ActionStep(ActionType.SET_VALUE, ref, value));
                removeOtherChoices(mat, ref, value);
                removeChoice(mat, mat.getRow(ref), value);
                removeChoice(mat, mat.getCol(ref), value);
                removeChoice(mat, mat.getSquare(ref), value);
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

    private void removeChoice(Matrice mat, MatriceZone zone, int  value) {
        for (Cell ref : zone.getCells()) {
            removeChoice(mat, ref, value);
        }
    }

    private MatriceAction removeChoice(Matrice mat, CellReference ref, int value) {
        if (mat.getCell(ref).hasChoice(value)) {
            add(new ActionStep(ActionType.REMOVE_OPTION, ref, value));
        }
        return this;
    }

    public void apply(Matrice mat) {
        for(ActionStep step:steps){
            step.apply(mat);
        }
    }

    public int size() {
        return steps.size();
    }

    public void undo(Matrice mat) {
        for(ActionStep step:steps){
            step.undo(mat);
        }
    }
}