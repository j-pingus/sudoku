package gee.sudoku.solver;

import gee.math.Combinatory;
import gee.sudoku.krn.Cell;
import gee.sudoku.krn.CellReference;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceHistory;
import gee.sudoku.krn.MatriceZone;
import gee.sudoku.krn.Matrice.Status;
import gee.sudoku.io.MatriceFile;
import gee.sudoku.ui.ConsolePresenter;
import gee.sudoku.ui.SudokuPresenter;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

/**
 * Solves the SUDOKU matrices
 * 
 * @author Even
 * @version $Revision: 1.2 $
 * @see gee.sudoku.core.Matrice
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
 * 
 */
public class Solver {
	Matrice mat;
	SudokuPresenter presentation;
	boolean guessing = true;
	Vector<Strategies> appliedStrategies;
	boolean silent = true;

	public void appliedStrategy(Strategies s) {
		if (!appliedStrategies.contains(s))
			appliedStrategies.add(s);
		mat.commit(s);
	}

	public static void main(String[] args) throws Exception {
		System.out.print("Please enter a valid path for sudoku file:");
		Scanner scan = new Scanner(System.in);
		String sudokuFileName = scan.next();
		File sudoku = new File(sudokuFileName);
		Matrice mat = sudoku.exists() ? MatriceFile.read(sudoku)
				: new MatriceFile().getSudokuSample();
		Solver solver = new Solver(mat);
		solver.setPresentation(new ConsolePresenter());
		solver.setSilent(false);
		solver.setGuessing(false);
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

	private Solver() {
		appliedStrategies = new Vector<Strategies>();
	}

	public Solver(Matrice mat) {
		this();
		this.mat = mat;
	}

	public void killPresentation() {
		if (presentation != null)
			presentation.kill();
	}

	public void show() {
		if (presentation != null)
			presentation.show(mat);
	}

	public void loop() throws Exception {
		boolean ret = false;
		do {
			ret = false;
			show();
			ret |= processZones();
			ret |= processMatrice();
		} while (mat.getStatus() == Status.UNSOLVED && ret);
		show();
	}

	public boolean xWing() {
		return xWingRow() || xWingCol();
	}

	public boolean xWingRow() {
		boolean ret = false;
		for (MatriceZone row : mat.getRows()) {
			for (int value : row.getChoices()) {
				if (row.countChoice(value) == 2) {
					for (MatriceZone row2 : mat.getRows()) {
						if (row2 != row) {
							if (row2.countChoice(value) == 2) {
								Vector<Cell> p = row.findChoice(value);
								if (row2.getCells().get(p.get(0).getCol())
										.hasChoice(value)) {
									if (row2.getCells().get(p.get(1).getCol())
											.hasChoice(value)) {
										Vector<Cell> xWing = new Vector<Cell>();
										xWing.addAll(p);
										xWing.addAll(row2.findChoice(value));
										for (Cell c : mat.getCols()[p.get(0)
												.getCol()].getOptionCells()) {
											if (!xWing.contains(c))
												ret = mat.removeChoices(c,
														value)
														|| ret;
										}
										for (Cell c : mat.getCols()[p.get(1)
												.getCol()].getOptionCells()) {
											if (!xWing.contains(c))
												ret = mat.removeChoices(c,
														value)
														|| ret;
										}
										if (ret) {
											showMessage(Strategies.X_WING_ROW,
													xWing.toArray(new Cell[0]));
											show();
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public boolean xWingCol() {
		boolean ret = false;
		for (MatriceZone col : mat.getCols()) {
			for (int value : col.getChoices()) {
				if (col.countChoice(value) == 2) {
					for (MatriceZone col2 : mat.getCols()) {
						if (col2 != col) {
							if (col2.countChoice(value) == 2) {
								Vector<Cell> p = col.findChoice(value);
								if (col2.getCells().get(p.get(0).getRow())
										.hasChoice(value)) {
									if (col2.getCells().get(p.get(1).getRow())
											.hasChoice(value)) {
										Vector<Cell> xWing = new Vector<Cell>();
										xWing.addAll(p);
										xWing.addAll(col2.findChoice(value));
										for (Cell c : mat.getRows()[p.get(0)
												.getRow()].getOptionCells()) {
											if (!xWing.contains(c))
												ret = mat.removeChoices(c,
														value)
														|| ret;
										}
										for (Cell c : mat.getRows()[p.get(1)
												.getRow()].getOptionCells()) {
											if (!xWing.contains(c))
												ret = mat.removeChoices(c,
														value)
														|| ret;
										}
										if (ret) {
											showMessage(Strategies.X_WING_COL,
													xWing.toArray(new Cell[0]));
											show();
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public void showMessage(Strategies s, CellReference... cellReferences) {
		appliedStrategy(s);
		if (presentation == null)
			return;
		presentation.showMessage(s, cellReferences);
	}

	public boolean confirmGuess() {
		if (!guessing)
			return false;
		if (presentation == null)
			return true;
		return presentation.confirmMessage(Strategies.GUESSING_FIRST);
	}

	public Matrice solve() throws Exception {
		Matrice ret = null;
		loop();
		if (mat.getStatus() == Status.UNSOLVED && confirmGuess()) {
			try {
				Matrice mat2 = (Matrice) mat.clone();
				Cell c1 = chooseCell(mat);
				if (c1 != null) {
					// System.out.print(c1.toStringExtended());
					mat.setValue(c1, c1.getChoices()[0]);
					mat.commit(Strategies.GUESSING_FIRST);
					// System.out.println(" --> " + c1);
					show();
					mat.setThrowing(false);
					c1 = chooseCell(mat2);
					// System.out.println(c1.toStringExtended());
					mat2.setValue(c1, c1.getChoices()[1]);
					mat2.commit(Strategies.GUESSING_SECOND);
					mat2.setThrowing(false);
					Solver solver = new Solver(mat2);
					solver.setSilent(isSilent());
					solver.appliedStrategies.addAll(appliedStrategies);
					if (presentation != null)
						solver.setPresentation(presentation.duplicate());
					show();
					loop();
					solver.show();
					solver.loop();
					if (mat2.getStatus() == Status.SOLVED)
						ret = mat2;
					if (mat.getStatus() == Status.SOLVED)
						ret = mat;
					if (mat2.getStatus() == Status.SCREWED) {
						solver.killPresentation();
					}
					if (mat.getStatus() == Status.SCREWED) {
						killPresentation();
						mat = mat2;
					}
				}
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (ret == null)
			ret = mat;
		if (ret.getStatus() == Status.SOLVED && !silent) {
			for (Strategies s : appliedStrategies) {
				System.out.print(s + ",");
			}
			System.out.println("");
		}
		return ret;
	}

	public Cell chooseCell(Matrice mat) {
		for (MatriceZone row : mat.getRows())
			for (Cell cell : row.getCells())
				if (cell.getChoices().length == 2)
					return cell;
		return null;
	}

	public boolean processMatrice() throws Exception {
		return candidates()|| xWing();
	}

	public boolean processZones() throws Exception {
		boolean ret = false;
		for (MatriceZone zone : mat.getZones()) {
			ret = ret || soleOption(zone);
			ret = ret || nakedPair(zone);
			ret = ret || nakedTriplet(zone);
			ret = ret || nakedQuad(zone);
			ret = ret || hiddenPair(zone);
			ret = ret || hiddenTriplet(zone);
			ret = ret || hiddenQuad(zone);
			ret = ret || hiddenQuint(zone);
			mat.check(false);
		}
		return ret;
	}



	public boolean nakedPair(MatriceZone zone) {
		boolean ret = false;
		// Naked pair
		for (Cell cell : zone.getOptionCells())
			if (cell.getChoices().length == 2) {
				Vector<Cell> options = zone.findChoice(cell);
				if (options.size() == 2) {
					for (Cell subLoopCell : zone.getCells()) {
						if (!options.contains(subLoopCell)) {
							for (int value : cell.getChoices()) {
								ret = mat.removeChoices(subLoopCell, value)
										|| ret;
							}
						}
					}
					if (ret) {
						showMessage(Strategies.NAKED_PAIR, options
								.toArray(new Cell[0]));
						ret = false;
					}
				}
			}
		return ret;
	}

	public boolean nakedTriplet(MatriceZone zone) {
		boolean ret = false;
		// Naked triplet
		if (zone.getChoices().length > 3) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] triplets = Combinatory.getTriplets(zoneChoices);
			for (int triplet[] : triplets) {
				Vector<Cell> options = zone.findCellsWithChoices(triplet);
				if (options.size() == 3) {
					for (Cell subLoopCell : zone.getOptionCells()) {
						if (!options.contains(subLoopCell)) {
							ret = mat.removeChoices(subLoopCell, triplet) | ret;
						}
					}
					if (ret) {
						showMessage(Strategies.NAKED_TRIPLET, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean hiddenTriplet(MatriceZone zone) {
		boolean ret = false;
		// Hidden triplet
		if (zone.getChoices().length >= 3) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] triplets = Combinatory.getTriplets(zoneChoices);
			for (int triplet[] : triplets) {
				Vector<Cell> options = zone.findCellsWithAnyChoice(triplet);
				if (options.size() == 3) {
					for (Cell c : options)
						ret = mat.removeOtherChoices(c, triplet) | ret;
					if (ret) {
						showMessage(Strategies.HIDDEN_TRIPLET, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	// private boolean checkTriplets(Vector<Cell> cells, int triplet[]) {
	// boolean ret = true;
	// // // remove two choices that are identicals;
	// // if ((cells.get(0).hasChoices(
	// // Combinatory.getInts(cells.get(1).getChoices())) || cells.get(0)
	// // .hasChoices(Combinatory.getInts(cells.get(1).getChoices())))
	// // && cells.get(0).countChoicesNotFrom(triplet) != 0) {
	// //
	// // return false;
	// // }
	// // if ((cells.get(2).hasChoices(Combinatory.getInts(cells.get(1)
	// // .getChoices())))
	// // && cells.get(2).countChoicesNotFrom(triplet) != 0) {
	// // return false;
	// // }
	// int notin = 0;
	// for (Cell c : cells) {
	// notin += c.countChoicesNotFrom(triplet);
	// }
	// ret = notin < 2;
	// return ret;
	// }

	public boolean nakedQuad(MatriceZone zone) {
		// Naked quad
		boolean ret = false;
		if (zone.getChoices().length > 4) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] quads = Combinatory.getQuads(zoneChoices);
			for (int quad[] : quads) {
				Vector<Cell> options = zone.findCellsWithChoices(quad);
				if (options.size() == 4) {

					for (Cell subLoopCell : zone.getOptionCells()) {
						if (!options.contains(subLoopCell)) {
							ret = mat.removeChoices(subLoopCell, quad) | ret;
						}
					}
					if (ret) {
						showMessage(Strategies.NAKED_QUAD, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean nakedQuint(MatriceZone zone) {
		// Naked quad
		boolean ret = false;
		if (zone.getChoices().length > 5) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] quints = Combinatory.getQuints(zoneChoices);
			for (int quint[] : quints) {
				Vector<Cell> options = zone.findCellsWithChoices(quint);
				if (options.size() == 5) {

					for (Cell subLoopCell : zone.getOptionCells()) {
						if (!options.contains(subLoopCell)) {
							ret = mat.removeChoices(subLoopCell, quint) | ret;
						}
					}
					if (ret) {
						showMessage(Strategies.NAKED_QUINT, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean nakedSexte(MatriceZone zone) {
		boolean ret = false;
		if (zone.getChoices().length > 6) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] sextes = Combinatory.getSextes(zoneChoices);
			for (int sexte[] : sextes) {
				Vector<Cell> options = zone.findCellsWithChoices(sexte);
				if (options.size() == 6) {

					for (Cell subLoopCell : zone.getOptionCells()) {
						if (!options.contains(subLoopCell)) {
							ret = mat.removeChoices(subLoopCell, sexte) | ret;
						}
					}
					if (ret) {
						showMessage(Strategies.NAKED_SEXTE, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean hiddenQuad(MatriceZone zone) {
		// Naked quad
		boolean ret = false;
		if (zone.getChoices().length >= 4) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] quads = Combinatory.getQuads(zoneChoices);
			for (int quad[] : quads) {
				Vector<Cell> options = zone.findCellsWithAnyChoice(quad);
				if (options.size() == 4) {
					for (Cell c : options)
						ret = mat.removeOtherChoices(c, quad) | ret;
					if (ret) {
						showMessage(Strategies.HIDDEN_QUAD, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean hiddenQuint(MatriceZone zone) {
		// Naked quad
		boolean ret = false;
		if (zone.getChoices().length > 5) {
			int zoneChoices[] = Combinatory.getInts(zone.getChoices());
			int[][] quints = Combinatory.getQuints(zoneChoices);
			for (int quint[] : quints) {
				Vector<Cell> options = zone.findCellsWithAnyChoice(quint);
				if (options.size() == 5) {
					for (Cell c : options)
						ret = mat.removeOtherChoices(c, quint) | ret;
					if (ret) {
						showMessage(Strategies.HIDDEN_QUINT, options
								.toArray(new Cell[0]));
					}
				}
			}
		}
		return ret;
	}

	public boolean candidates() {
		boolean ret = false;
		for (MatriceZone squares[] : mat.getSquares()) {
			for (MatriceZone square : squares) {
				Integer choices[] = square.getChoices();
				for (int choice : choices) {
					Vector<Cell> cells = square.findChoice(choice);
					int row = cells.get(0).getRow();
					int col = cells.get(0).getCol();
					boolean rowFound = true;
					boolean colFound = true;
					for (Cell cell : cells) {
						if (cell.getCol() != col)
							colFound = false;
						if (cell.getRow() != row)
							rowFound = false;
					}
					if (rowFound) {
						boolean subret = false;
						for (Cell c : mat.getRows()[row].getCells()) {
							if (!cells.contains(c))
								subret = mat.removeChoices(c, choice) || subret;
						}
						if (subret) {
							// System.out.println(choice + " in "
							// + square.getName());
							showMessage(Strategies.CANDIDATE_ROW,
									mat.getRows()[row].getCellsArray());
							show();
							ret = true;
							break;
						}
					}
					if (colFound) {
						boolean subret = false;
						for (Cell c : mat.getCols()[col].getCells()) {
							if (!cells.contains(c))
								subret = mat.removeChoices(c, choice) || subret;
						}
						if (subret) {
							// System.out.println(choice + " in "
							// + square.getName());
							showMessage(Strategies.CANDIDATE_COL,
									mat.getCols()[col].getCellsArray());
							show();
							ret = true;
							break;
						}
					}

				}
			}
		}
		return ret;
	}

	public boolean isGuessing() {
		return guessing;
	}

	public void setGuessing(boolean guessing) {
		this.guessing = guessing;
	}

	public SudokuPresenter getPresentation() {
		return presentation;
	}

	public void setPresentation(SudokuPresenter presentation) {
		this.presentation = presentation;
	}

	public Vector<Strategies> getAppliedStrategies() {
		return appliedStrategies;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

}
