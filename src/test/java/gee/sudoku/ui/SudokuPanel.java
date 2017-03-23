package gee.sudoku.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SudokuPanel extends JPanel implements KeyListener {
	private int pos = 0;
	JLabel lblZone[];

	/**
	 * Create the panel.
	 */
	public SudokuPanel() {
		lblZone = new JLabel[81];
		setLayout(new GridLayout(3, 3, 5, 5));
		JPanel panel[] = new JPanel[9];
		for (int i = 0; i < panel.length; i++) {
			panel[i] = new JPanel();
			panel[i].setBackground(Color.WHITE);
			panel[i].setBorder(new BevelBorder(BevelBorder.LOWERED));
			panel[i].setLayout(new GridLayout(3, 3, 0, 0));
			add(panel[i]);
		}
		for (int i = 0; i < lblZone.length; i++) {
			lblZone[i] = new JLabel("");
			lblZone[i].setBackground(new Color(224, 255, 255));
			lblZone[i].setFont(new Font("Arial Black", Font.BOLD, 22));
			lblZone[i].setHorizontalAlignment(JLabel.CENTER);
			lblZone[i].setVerticalAlignment(JLabel.CENTER);
			lblZone[i].setBorder(new BevelBorder(BevelBorder.RAISED));
			int x = i % 9 / 3;
			int y = i / 27;
			int panelPos = x + y * 3;
			panel[panelPos].add(lblZone[i]);
		}
		moveEditPosition(0);
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		System.out.println("Key released " + arg0);
		if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveEditPosition(1);
		}
		if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			moveEditPosition(-1);
		}
		if (arg0.getKeyCode() == KeyEvent.VK_UP) {
			moveEditPosition(-9);
		}
		if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			moveEditPosition(9);
		}

	}

	private void moveEditPosition(int delta) {
		lblZone[pos].setBackground(new Color(224, 255, 255));
		pos = (pos + delta + 81) % lblZone.length;
		lblZone[pos].setBackground(Color.GREEN);
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println("Key typed " + arg0);
		if (arg0.getKeyChar() >= '1' && arg0.getKeyChar() <= '9') {
			lblZone[pos].setText("" + arg0.getKeyChar());
			moveEditPosition(1);
		}
		if (arg0.getKeyChar() == '0') {
			lblZone[pos].setText("");
			moveEditPosition(1);

		}
	}

	public int[] getSudoku() {
		int ret[] = new int[lblZone.length];
		int i = 0;
		for (JLabel lbl : lblZone) {
			if (lbl.getText().length() == 0) {
				ret[i++] = 0;
			} else {
				ret[i++] = lbl.getText().charAt(0) - '0';
			}
		}
		return ret;
	}
}
