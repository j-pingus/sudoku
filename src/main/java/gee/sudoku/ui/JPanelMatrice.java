package gee.sudoku.ui;


import gee.sudoku.krn.CellReference;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class JPanelMatrice extends JPanel{
	private static final long serialVersionUID = 1L;
	private JPanelCell panelCells[][] = new JPanelCell[9][9];
	int lastHighlightedValue = -1;

	public void setCellListener(CellListener cellListener) {
		for (JPanelCell cells[] : panelCells)
			for (JPanelCell cell : cells)
				cell.setCellListener(cellListener);
	}

	/**
	 * This is the default constructor
	 */
	public JPanelMatrice() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(589, 412);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(153, 153, 255));
		for (int row = 0; row < panelCells.length; row++) {
			for (int col = 0; col < panelCells[row].length; col++) {
				GridBagConstraints gridBagConstraint = new GridBagConstraints();
				gridBagConstraint.gridx = col;
				gridBagConstraint.gridy = row;
				int top = 1, left = 1, right = 0, bottom = 0;
				if (col % 3 == 2)
					right = 1;
				if (row % 3 == 2)
					bottom = 1;
				gridBagConstraint.insets = new Insets(top, left, bottom, right);
				gridBagConstraint.weightx = 0.11111D;
				gridBagConstraint.weighty = 0.11111D;
				gridBagConstraint.fill = GridBagConstraints.BOTH;
				panelCells[row][col] = new JPanelCell(row, col);
				this.add(panelCells[row][col], gridBagConstraint);
			}

		}
	}

	public void setValue(int row, int col, int value) {
		panelCells[row][col].setValue(value, false);
		if(panelCells[row][col].contains(lastHighlightedValue)){
			panelCells[row][col].highlight(lastHighlightedValue);
		}else{
			panelCells[row][col].normallight();
		}
	}

	public void unsetValue(int row, int col, int value) {
		panelCells[row][col].unsetValue(value, false);
		if(panelCells[row][col].contains(lastHighlightedValue)){
			panelCells[row][col].highlight(lastHighlightedValue);
		}else{
			panelCells[row][col].normallight();
		}
	}

	public void setHints(int row, int col, Integer... values) {
		panelCells[row][col].setHints(false,values);
		if(panelCells[row][col].contains(lastHighlightedValue)){
			panelCells[row][col].highlight(lastHighlightedValue);
		}else{
			panelCells[row][col].normallight();
		}
	}
	public void highlight(int value) {
		if(value==lastHighlightedValue)value=-value;
		for (JPanelCell cells[] : panelCells) {
			for (JPanelCell cell : cells) {
				if (cell.contains(value)) {
					cell.highlight(value);
				} else {
					cell.normallight();
				}
			}
		}
		lastHighlightedValue=value;
	}
	public void highlight(CellReference... cellReferences){
		for(CellReference ref:cellReferences){
			panelCells[ref.getRow()][ref.getCol()].highlight();
		}
	}
	public void highlight(Color color,CellReference... cellReferences){
		for(CellReference ref:cellReferences){
			panelCells[ref.getRow()][ref.getCol()].highlight(color);
		}
	}

	public void normallight(CellReference... cellReferences){
		for(CellReference ref:cellReferences){
			panelCells[ref.getRow()][ref.getCol()].normallight();
		}
	}

	public void init() {
		for (JPanelCell cells[] : panelCells) {
			for (JPanelCell cell : cells) {
				cell.init();
			}
		}
	}
} // @jve:decl-index=0:visual-constraint="10,33"
