package gee.sudoku.ui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingConstants;

public class JPanelCell extends JPanel {
	Color normal = new Color(153, 153, 255); // @jve:decl-index=0:
	Color disabled = new Color(227, 227, 255); // @jve:decl-index=0:
	Color highlighted = new Color(255, 255, 51); // @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JLabel hints[] = new JLabel[9];
	private int row, col;
	private int value = -1;
	CellListener cellListener;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public JPanelCell() {
		this(1, 1);
	}

	/**
	 * This is the default constructor
	 */
	public JPanelCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		normal = new Color(153, 153, 255); // @jve:decl-index=0:
		disabled = new Color(227, 227, 255); // @jve:decl-index=0:
		highlighted = new Color(255, 255, 51); // @jve:decl-index=0:
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(100, 100));
		this.setSize(100, 100);
		for (int i = 0; i < hints.length; i++) {
			GridBagConstraints gridBagConstraint = new GridBagConstraints();
			gridBagConstraint.gridx = i % 3;
			gridBagConstraint.weightx = 0.33D;
			gridBagConstraint.weighty = 0.33D;
			gridBagConstraint.gridy = i / 3;
			gridBagConstraint.fill = GridBagConstraints.BOTH;
			JLabel hint = new JLabel();
			hint.setText("" + (i + 1));
			hint.setHorizontalAlignment(SwingConstants.CENTER);
			hint.setHorizontalTextPosition(SwingConstants.CENTER);
			hint.addMouseListener(new HintListener(i + 1));
			hint.setForeground(normal);
			hints[i] = hint;
			this.add(hint, gridBagConstraint);
		}
	}

	void setValue(int value, boolean propagate) {
		if (this.value == -1) {
			for (JLabel hint : hints)
				hint.setForeground(Color.white);
			hints[4].setForeground(Color.BLUE);
			hints[4].setText("" + value);
			hints[4].setFont(new Font("Dialog", Font.BOLD, 18));
			this.value = value;
			highlight(lastHighlightedValue);
			if (cellListener != null && propagate)
				cellListener.setValue(row, col, value);
		}
	}
	void unsetValue(int value, boolean propagate) {
		if (this.value != -1) {
			for (JLabel hint : hints)
				hint.setForeground(Color.BLUE);
			int setValue = new Integer(hints[4].getText());
			hints[4].setForeground(Color.BLUE);
			hints[4].setText("5");
			hints[4].setFont(new Font("Dialog", Font.BOLD, 12));
			System.out.println("unset : "+setValue);
			this.value = -1;
			highlight(lastHighlightedValue);
			if (cellListener != null && propagate)
				cellListener.unsetValue(row, col, setValue);
		}
	}

	public void init() {
		this.value = -1;
		for (JLabel hint : hints) {
			hint.setForeground(normal);
			hint.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		hints[4].setText("5");

	}

	int lastHighlightedValue = -1;

	void highlight(int value) {
		if (contains(value)) {
			highlight();
		} else {
			normallight();
		}
		lastHighlightedValue = value;
	}

	void highlight() {
		highlight(highlighted);
	}

	void highlight(Color highlighted) {
		setBackground(highlighted);
		if (highlighted == this.highlighted)
			for (JLabel hint : hints) {
				if (hint.getForeground() == Color.WHITE) {
					hint.setForeground(highlighted);
				}
			}
	}

	void normallight() {
		setBackground(Color.WHITE);
		for (JLabel hint : hints) {
			if (hint.getForeground() == highlighted) {
				hint.setForeground(Color.WHITE);
			}
		}

	}

	void toggleHint(int value, boolean propagate) {
		if (this.value == -1) {
			Color c = hints[value - 1].getForeground();
			// System.out.println(c);
			if (c == normal) {
				hints[value - 1].setForeground(disabled);
				if (cellListener != null && propagate)
					cellListener.unsetHint(row, col, value);
			} else {
				hints[value - 1].setForeground(normal);
				if (cellListener != null && propagate)
					cellListener.setHint(row, col, value);
			}
			highlight(lastHighlightedValue);
		}
	}

	void setHints(boolean propagate, Integer... values) {
		for (JLabel hint : hints)
			hint.setForeground(disabled);
		for (int value : values)
			toggleHint(value, propagate);
	}

	class HintListener implements MouseListener {
		int value;

		HintListener(int value) {
			this.value = value;
		}

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.printf("%d value with %s\n", value, e);
			if(e.getClickCount()==2){
				unsetValue(value,true);
			}
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				toggleHint(value, true);
				break;
			case MouseEvent.BUTTON3:
				setValue(value, true);
				break;
			default:
				// Ignore
			}
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public void setCellListener(CellListener cellListener) {
		this.cellListener = cellListener;
	}

	public boolean contains(int value) {
		if (value < 1 || value > 9)
			return false;
		if (this.value == value)
			return true;
		if (hints[value - 1].getForeground() == normal)
			return true;
		return false;
	}
}
