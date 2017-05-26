package gee.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.apache.log4j.Logger;

public class CellPanelTest {
	private static final Logger LOG = Logger.getLogger(CellPanelTest.class);

	public static void main(String[] args) {
		new CellPanelTest();
	}

	Frame f;
	CellPanel cells[];

	public CellPanelTest() {
		cells = new CellPanel[] { new CellPanel(), new CellPanel(),
				new CellPanel(), new CellPanel(), new CellPanel(),
				new CellPanel(), new CellPanel(), new CellPanel(),
				new CellPanel() };
		f = new Frame("test") {
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				LOG.debug("painted");
			}

			@Override
			public void update(Graphics g) {
				// TODO Auto-generated method stub
				super.update(g);
				LOG.debug("updated");
			}

			@Override
			public void validate() {
				// TODO Auto-generated method stub
				super.validate();
				LOG.debug("validated");
			}

			@Override
			public void paintComponents(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponents(g);
				LOG.debug("Components painted");
			}
		};
		f.setLayout(new BorderLayout());
		for (CellPanel c : cells)
			f.add(c, BorderLayout.NORTH);
		f.setSize(200, 200);
		f.setLocation(1700, 50);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				LOG.debug("moved");

			}

			@Override
			public void componentResized(ComponentEvent e) {
				int w = f.getSize().width;
				LOG.debug(w);
				for (CellPanel c : cells) {
					c.setSize((w - 50) / 3);
				}
				f.validate();
				LOG.debug("resized");
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				LOG.debug("shown");
			}

		});
		f.setVisible(true);

	}

}
