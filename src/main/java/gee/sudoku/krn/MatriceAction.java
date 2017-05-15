package gee.sudoku.krn;

import gee.sudoku.solver.Strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class MatriceAction {
    Strategies strategy;
    private Vector<ActionStep> steps;
    private MatriceZone[] hints;
    private int[] hintValues;

    public MatriceAction(Strategies strategy, MatriceZone... hints) {
        this.steps = new Vector<ActionStep>();
        this.strategy = strategy;
        this.hints = hints;
    }

    public int[] getHintValues() {
        return hintValues;
    }

    public MatriceAction setHintValues(Integer... hintValues) {
        this.hintValues = new int[hintValues.length];
        int i = 0;
        for (int val : hintValues)
            this.hintValues[i++] = val;
        return this;
    }

    public MatriceAction setHintValues(int... hintValues) {
        this.hintValues = hintValues;
        return this;
    }

    public MatriceZone[] getHints() {
        return hints;
    }

    public Strategies getStrategy() {
        return strategy;
    }

    public void add(ActionStep step) {
        if (!steps.contains(step)) {
            steps.add(step);
        }
    }



    public MatriceAction setValue(Matrice mat, CellReference ref, int value) {
        if (value > 0) {
            setHintValues(new int[]{value});
            if (mat.getCell(ref).hasChoice(value)) {
                add(new ActionStep(ActionType.SET_VALUE, ref, value));
                removeOtherChoices(mat, ref, value);
                removeChoices(mat, mat.getRow(ref), new int[]{value});
                 removeChoices(mat, mat.getCol(ref), new int[]{value});
                removeChoices(mat, mat.getSquare(ref), new int[]{value});
            }
        }
        return this;
    }

    public MatriceAction unsetValue(Matrice mat, CellReference ref, int value) {
        if (value > 0) {
            setHintValues(new int[]{value});
            add(new ActionStep(ActionType.UNSET_VALUE, ref, value));
            addChoices(mat, ref, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            addChoices(mat, mat.getRow(ref), value);
            addChoices(mat, mat.getCol(ref), value);
            addChoices(mat, mat.getSquare(ref), value);
        }
        return this;
    }

    private void addChoices(Matrice mat, CellReference ref, int... values) {
        for (int value : values)
            addChoice(mat, ref, value);
    }

    private void addChoices(Matrice mat, MatriceZone zone, int... values) {
        for (Cell ref : zone.getCells())
            for (int value : values)
                addChoice(mat, ref, value);
    }

    public void removeOtherChoices(Matrice mat, CellReference ref, int... values) {
        for (int other : mat.getCell(ref).getOtherChoices(values)) {
            removeChoices(mat, ref, other);
        }
    }

    public void removeChoices(Matrice mat, MatriceZone zone, int... values) {
        for (Cell ref : zone.getCells()) {
            removeChoices(mat, ref, values);
        }
    }

    public MatriceAction addChoice(Matrice mat, int i, int j, int value) {
        addChoice(mat, new CellReference(i, j), value);
        return this;
    }

    public MatriceAction addChoice(Matrice mat, CellReference c, int value) {
        if (!mat.getCell(c).hasChoice(value)) {
            add(new ActionStep(ActionType.ADD_OPTION, c, value));
        }
        return this;
    }

    public void removeChoices(Matrice mat, MatriceZone zone, Integer... values) {
        for (Cell ref : zone.getCells()) {
            for (int value : values)
                removeChoices(mat, ref, value);
        }
    }

    public MatriceAction removeChoices(Matrice mat, CellReference ref, int... values) {
        for (int value : values)
            if (mat.getCell(ref).hasChoice(value)) {
                add(new ActionStep(ActionType.REMOVE_OPTION, ref, value));
            }
        return this;
    }

    public void apply(Matrice mat) {
        for (ActionStep step : steps) {
            step.apply(mat);
        }
        mat.getHistory().add(this);
    }

    public int size() {
        return steps.size();
    }

    public void undo(Matrice mat) {
        for (ActionStep step : steps) {
            step.undo(mat);
        }
    }

    public void doNotTouch(CellReference... cellReferences) {
        List<ActionStep> toRemove = new ArrayList<>();
        for (ActionStep step : steps) {
            for (CellReference cellReference : cellReferences) {
                if (step.cellReference.equals(cellReference))
                    toRemove.add(step);
            }
        }
        for (ActionStep step : toRemove) {
            steps.remove(step);
        }
    }

	@Override
	public String toString() {
		return strategy+" [hintValues="
				+ Arrays.toString(hintValues) + ", steps=" + steps + "]";
	}

    public void doNotTouch(MatriceZone... zones) {
        for (MatriceZone zone : zones)
            for (Cell cell : zone.getCells())
                doNotTouch(cell);
    }
}