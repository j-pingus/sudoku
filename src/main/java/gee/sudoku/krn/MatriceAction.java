package gee.sudoku.krn;

import gee.sudoku.solver.Strategies;

import java.util.Vector;

public class MatriceAction {
	Strategies strategy;
	Vector<ActionStep> steps;

	public Strategies getStrategy() {
		return strategy;
	}

	public Vector<ActionStep> getSteps() {
		return steps;
	}

	public MatriceAction() {
	}
	@Override
	public String toString() {
		StringBuilder b=new StringBuilder();
		b.append(strategy);
		b.append(" ");
		b.append(steps);
		return b.toString();
	}
}