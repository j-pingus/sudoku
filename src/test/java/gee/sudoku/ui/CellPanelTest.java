package gee.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CellPanelTest {
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
		f = new Frame("test"){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				System.out.println("painted");
			}
			@Override
			public void update(Graphics g) {
				// TODO Auto-generated method stub
				super.update(g);
				System.out.println("updated");
			}
			@Override
			public void validate() {
				// TODO Auto-generated method stub
				super.validate();
				System.out.println("validated");
			}
			@Override
			public void paintComponents(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponents(g);
				System.out.println("Components painted");
			}
		};
		f.setLayout(new BorderLayout());
		for (CellPanel c : cells)
			f.add(c,BorderLayout.NORTH);
		f.setSize(200, 200);
		f.setLocation(1700, 50);
		f.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		          System.exit(0);
		        }
		      });
		f.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("moved");

			}

			public void componentResized(ComponentEvent e) {
				int w = f.getSize().width;
				System.out.println(w);
				for (CellPanel c : cells){
					c.setSize((w-50) / 3);
				}
				f.validate();
				System.out.println("resized");
			}

			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				System.out.println("shown");
			}

		});
		f.setVisible(true);

	}

}
