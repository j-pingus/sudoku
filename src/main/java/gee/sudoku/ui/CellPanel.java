package gee.sudoku.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

public class CellPanel extends Panel {
	private static final Logger LOG = Logger.getLogger(CellPanel.class);
	int size = 50;
	int top = 13;
	int left = 4;

	public CellPanel() {
		// TODO Auto-generated constructor stub
		setSize(size);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, size - 1, size - 1);
		g.setColor(Color.RED);
		char digits[] = new char[] { '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };
		int digit = 0;
		g.drawChars(digits, digit++, 1, left, top);
		g.drawChars(digits, digit++, 1, left, (top + size - 5) / 2);
		g.drawChars(digits, digit++, 1, left, size - 5);
		g.drawChars(digits, digit++, 1, (left + size - 14) / 2, top);
		g.drawChars(digits, digit++, 1, (left + size - 14) / 2,
				(top + size - 5) / 2);
		g.drawChars(digits, digit++, 1, (left + size - 14) / 2, size - 5);
		g.drawChars(digits, digit++, 1, size - 14, top);
		g.drawChars(digits, digit++, 1, size - 14, (top + size - 5) / 2);
		g.drawChars(digits, digit++, 1, size - 14, size - 5);
		LOG.debug("painted - cell");
	}

	public void setSize(int size) {
		this.size = size;
		setPreferredSize(new Dimension(size, size));
		setMinimumSize(new Dimension(size, size));
		repaint();
	}

}
