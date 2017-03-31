package gee.sudoku.ui;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.CellReference;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceHistory;
import gee.sudoku.io.ExcellFile;
import gee.sudoku.io.MatriceFile;
import gee.sudoku.solver.Solver;
import gee.sudoku.solver.Strategies;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Presentation layer (AWT + SWT) for matrices Handles basic commands to
 * save/load files and resolve Sudoku
 * 
 * @author Even
 * @version $Revision: 1.3 $
 * @log $Log: SudokuFrame.java,v $
 * @log Revision 1.3  2009/10/28 08:16:06  gev
 * @log from VE
 * @log
 * @log Revision 1.2  2009/09/30 21:40:31  gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.2 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log Revision 1.1 2009/07/01 09:41:37 gev With header
 * 
 */
public class SudokuFrame extends Frame implements ComponentListener,
		KeyListener, SudokuPresenter {
	enum Mode {
		SET_VALUE, SET_OPTION;
	};

	Mode mode = Mode.SET_VALUE;

	class CellField extends TextField {
		int row, col;

		private CellField(int row, int col) {
			this.row = row;
			this.col = col;
		}

		private int getRow() {
			return row;
		}

		private int getCol() {
			return col;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Panel panelReferences = null;
	Panel panelDisplay = null;
	Panel panelHelp = null;
	Label modeLabel = null;
	boolean prompt = false;
	CellField cells[][];
	Label references[];
	Matrice mat;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SudokuFrame().setVisible(true);
	}

	public SudokuFrame() {
		super();
		cells = new CellField[9][9];
		initialize();
		initSudoku();
		setVisible(true);
	}

	void initSudoku() {
		clear();
		mat.setReferenceString("123456789");
		show(mat);
		cells[0][0].requestFocus();
	}

	void clear() {
		mat = new Matrice(9);
		mat.setReferenceString("         ");
		show(mat);
		cells[0][0].requestFocus();
	}

	public SudokuPresenter duplicate() {
		SudokuFrame frame = new SudokuFrame();
		Point p = getLocation();
		frame.setLocation(p.x + 20, p.y + 20);
		try {
			frame.show((Matrice) mat.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return frame;
	}

	void open() {
		JFileChooser fc = new JFileChooser(new File("."));
		fc.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				return f.getName().endsWith(".sudoku")
						|| f.getName().endsWith(".wordoku");
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Any-Doku";
			}
		});
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			mat = MatriceFile.read(file);
			show(mat);
			// This is where a real application would open the file.
		}

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		mat = new Matrice(9);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.setTitle("Sudoku Editor");
		this.setSize(686, 569);
		this.setLayout(new BorderLayout());
		this.add(getPanelReference(), BorderLayout.NORTH);
		this.add(getPanelDisplay(), BorderLayout.CENTER);
		this.add(getPanelHelp(), BorderLayout.SOUTH);
		this.addComponentListener(this);
		this.addKeyListener(this);
	}

	private Panel getPanelReference() {
		if (panelReferences == null) {
			panelReferences = new Panel();

			references = new Label[] { new Label("1"), new Label("2"),
					new Label("3"), new Label("4"), new Label("5"),
					new Label("6"), new Label("7"), new Label("8"),
					new Label("9") };
			Panel left = new Panel();
			modeLabel = new Label(mode.toString());
			left.add(modeLabel);
			Panel right = new Panel();
			for (Label ref : references) {
				right.add(ref);
				ref.setBackground(Color.YELLOW);
				ref.setAlignment(Label.CENTER);
			}
			panelReferences.add(left);
			panelReferences.add(right);

		}
		return panelReferences;
	}

	private Panel getPanelHelp() {
		if (panelHelp == null) {
			panelHelp = new Panel();
			panelHelp.addKeyListener(this);
			Label l = new Label(
					"Type a number [1-9] to setup matrice, Clear All CTRL+C,New Sudoku CTRL+N, Save CTRL+S, Open CTRL+O, Resolve CTRL+R");
			panelHelp.add(l);
		}
		return panelHelp;
	}

	private Panel getPanelDisplay() {
		if (panelDisplay == null) {
			panelDisplay = new Panel();
			panelDisplay.addKeyListener(this);
			panelDisplay.setLayout(new GridBagLayout());
			Panel p = new Panel();
			p.setBackground(Color.BLACK);
			p.addKeyListener(this);
			panelDisplay.add(p, new GridBagConstraints(3, 0, 1, 11, 0.01, 0.01,
					GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			p = new Panel();
			p.addKeyListener(this);
			p.setBackground(Color.BLACK);
			panelDisplay.add(p, new GridBagConstraints(7, 0, 1, 11, 0.01, 0.01,
					GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			p = new Panel();
			p.addKeyListener(this);
			p.setBackground(Color.BLACK);
			panelDisplay.add(p, new GridBagConstraints(0, 3, 11, 1, 0.01, 0.01,
					GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			p = new Panel();
			p.addKeyListener(this);
			p.setBackground(Color.BLACK);
			panelDisplay.add(p, new GridBagConstraints(0, 7, 11, 1, 0.01, 0.01,
					GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			for (int row = 0; row < 9; row++) {
				for (int col = 0; col < 9; col++) {
					cells[row][col] = new CellField(row, col);
					cells[row][col].setName("Cell[" + row + "][" + col + "]");
					cells[row][col].setEditable(false);
					cells[row][col].addKeyListener(this);
					panelDisplay.add(cells[row][col], new GridBagConstraints(
							col + (col / 3), row + (row / 3), 1, 1, 1.0, 1.0,
							GridBagConstraints.PAGE_START,
							GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0,
							0));
					/*
					 * left.add(b, new GridBagConstraints(0, p++, 1, 1, 0.0,
					 * 0.0, GridBagConstraints.PAGE_START,
					 * GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
					 */
				}
			}
		}
		return panelDisplay;
	}

	public String getReferencedValue(int val) {
		return references[val - 1].getText();
	}

	public void show(Matrice m) {
		mat = m;
		int i = 0;
		for (char ref : m.getReference())
			references[i++].setText(ref == ' ' ? "" : "" + ref);
		int row = 0;
		for (Cell lines[] : m.getCells()) {
			int col = 0;
			for (Cell cell : lines) {
				if (cell.isSet()) {
					cells[row][col]
							.setText(getReferencedValue(cell.getValue()));
				} else {
					String show = null;
					for (int choice : cell.getChoices()) {
						if (show == null)
							show = "{" + getReferencedValue(choice);
						else
							show += " , " + getReferencedValue(choice);
					}
					if (show != null)
						show += "}";
					cells[row][col].setText(show);
				}
				col++;
			}
			row++;
		}
		String title;
		try {
			title = "Status : " + m.getStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			title = "Coherent : " + e.getLocalizedMessage();
		}
		setTitle(title);
	}

	// Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			kill();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		System.out.println((int) arg0.getKeyChar());
		System.out.println(arg0.getModifiers());
		if (((int) arg0.getModifiers() & 2) == 2) {
			// Control is pressed
			if (arg0.getKeyChar() == 15) {
				// CTRL + O
				open();
			}
			if (arg0.getKeyChar() == 4) {
				// CTRL + D
				duplicate();
			}
			if (arg0.getKeyChar() == 3) {
				// CTRL + C
				clear();
			}
			if (arg0.getKeyChar() == 14) {
				// CTRL + N
				initSudoku();
			}
			if (arg0.getKeyChar() == 24) {
				// CTRL+X
				saveExcel();
			}
			if (arg0.getKeyChar() == 19) {
				save();
			}
			if (arg0.getKeyChar() == 18) {
				Solver solve = new Solver(mat);
				solve.setSilent(false);
				try {
					solve.solve();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (arg0.getModifiers() == 8) {
			highlight(arg0.getKeyChar() - '0');
		} else if (arg0.getModifiers() == 0
				&& (arg0.getKeyChar() == '=' || arg0.getKeyChar() == 43)) {
			mode = Mode.SET_VALUE;
			modeLabel.setText(mode.toString());
		} else if (arg0.getModifiers() == 0 && arg0.getKeyChar() == '-') {
			mode = Mode.SET_OPTION;
			modeLabel.setText(mode.toString());
		} else if (arg0.getComponent() instanceof CellField) {
			CellField cell = (CellField) arg0.getComponent();
			int row = cell.getRow();
			int col = cell.getCol();
			if (arg0.getModifiers() == 0 && arg0.getKeyChar() == 127) {
				mat.unsetValue(row, col);
			} else if (arg0.getModifiers() == 0) {

				int value = validChar(arg0.getKeyChar());
				switch (mode) {
				case SET_VALUE:
					mat.setValue(row, col, value);
					break;
				case SET_OPTION:
					if (mat.getCells()[row][col].hasChoice(value)) {
						mat.removeChoice(value, mat.getCells()[row][col]);
					} else {
						mat.addChoice(value, mat.getCells()[row][col]);
					}
					break;
				}
				show(mat);
				arg0.getComponent().transferFocus();
			}
		}
	}

	private Vector<Cell> highlighted = null;
	private Color normalColor = null;
	private int previousValue = 0;

	private void highlight(int value) {
		if (value < 1 || value > 9)
			return;
		if (highlighted != null) {
			for (Cell cell : highlighted) {
				cells[cell.getRow()][cell.getCol()].setBackground(normalColor);
			}
		}
		if (value != previousValue) {
			highlighted = mat.getCells(value);
			for (Cell cell : highlighted) {
				normalColor = cells[cell.getRow()][cell.getCol()]
						.getBackground();
				cells[cell.getRow()][cell.getCol()].setBackground(Color.CYAN);
			}
			previousValue = value;
		} else {
			previousValue = 0;
		}
	}

	private String getReferenceString() {
		String ret = "";
		for (Label ref : references) {
			ret += ref.getText().length() == 0 ? " " : ref.getText().substring(
					0, 1);
		}
		return ret;
	}

	/**
	 * Accepts any char in reference (setting it up if unset ...) Only chars
	 * from 1-9 and A-Z are accepted in order to avoid messy chars on screen
	 * 
	 * @param c
	 * @return
	 */
	private int validChar(char c) {
		if ((c > '0' && c <= '9') || (c >= 'A' && c <= 'Z'))
			for (int i = 0; i < 9; i++) {
				Label ref = references[i];
				if (ref.getText().length() == 0) {
					ref.setText("" + c);
					mat.setReferenceString(getReferenceString());
					return i + 1;
				}
				if (ref.getText().charAt(0) == c)
					return i + 1;
			}
		return -1;
	}

	public Matrice getMatrice() {
		return mat;
	}

	public void kill() {
		setVisible(false);
		dispose();

	}

	public void save() {
		if (getMatrice().getReferenceString().equals("123456789")) {
			MatriceFile.write(getMatrice(), new File(""
					+ System.currentTimeMillis() + ".sudoku"));
		} else {
			MatriceFile.write(getMatrice(), new File(""
					+ System.currentTimeMillis() + ".wordoku"));
		}

	}

	public void saveExcel() {
		try {
			File excel = File.createTempFile("sudoku", ".xls", new File("."));
			ExcellFile.save(mat, excel);
			Runtime.getRuntime().exec(
					"rundll32 SHELL32.DLL,ShellExec_RunDLL "
							+ excel.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void showMessage(Strategies s, CellReference... cellReferences) {
		// TODO Auto-generated method stub
		Color back = null;
		for (CellReference cellRef : cellReferences) {
			back = cells[cellRef.getRow()][cellRef.getCol()].getBackground();
			cells[cellRef.getRow()][cellRef.getCol()]
					.setBackground(Color.GREEN);
		}
		System.out.println(s.toString());
		if (s == Strategies.X_WING_COL || s == Strategies.X_WING_ROW) {
			JOptionPane.showConfirmDialog(this, s.getDescription(), "sudoku",
					JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE);
		} else {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (CellReference cellRef : cellReferences)
			cells[cellRef.getRow()][cellRef.getCol()].setBackground(back);
	}

	public boolean confirmMessage(Strategies s, CellReference... cellReferences) {
		// TODO Auto-generated method stub
		int answer = JOptionPane.showConfirmDialog(this, s.toString(),
				"sudoku", JOptionPane.OK_CANCEL_OPTION);
		return answer == JOptionPane.OK_OPTION;
	}
}
