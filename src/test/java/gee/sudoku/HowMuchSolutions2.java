package gee.sudoku;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class HowMuchSolutions2 {
	private static final Logger LOG = Logger.getLogger(HowMuchSolutions2.class);
	int masks[] = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
	int notmasks[] = new int[] { ~1, ~2, ~4, ~8, ~16, ~32, ~64, ~128, ~256 };
	int values[];
	double solutions = 0D;
	JFrame jFrame;
	JPanel jContentPane;
	JLabel jLabel[] = new JLabel[9];

	HowMuchSolutions2() {
		values = new int[81];
		for (int i = 0; i < 81; i++) {
			values[i] = -1;
		}

	}

	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			for (int row = 0; row < 9; row++) {
				GridBagConstraints gridBagConstraint = new GridBagConstraints();
				gridBagConstraint.gridx = 0;
				gridBagConstraint.gridy = row;
				gridBagConstraint.insets = new Insets(2, 2, 2, 2);
				gridBagConstraint.weightx = 1D;
				gridBagConstraint.weighty = 0.11111D;
				gridBagConstraint.fill = GridBagConstraints.BOTH;

				jContentPane.add(getJLabel(row), gridBagConstraint);
			}
		}
		return jContentPane;
	}

	private JLabel getJLabel(int row) {
		if (jLabel[row] == null) {
			jLabel[row] = new JLabel("row " + (row + 1) + ":");
		}
		return jLabel[row];
	}

	public static void main(String[] args) {
		final HowMuchSolutions2 sols = new HowMuchSolutions2();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				sols.getJFrame().setVisible(true);
			}
		});
		sols.search();
	}

	protected Component getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setName("sudoku search Solution");
			jFrame.setSize(545, 563);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Sudoku search solutions");
		}
		return jFrame;
	}

	private void search() {
		int choices[] = new int[81];
		for (int i = 0; i < 81; i++) {
			choices[i] = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256;
		}
		search(0, choices);
		LOG.info("Solution : " + solutions);
	}

	private void search(int pos, int[] choices) {
		if (pos == 81) {
			solutions++;
			if (solutions == 1 || solutions % 100000 == 0) {
				LOG.info("Solution : " + solutions);
				print();
			}
			return;
		}
		for (int i = 0; i < 9; i++) {
			if ((choices[pos] & masks[i]) > 0) {
				if (pos == 35 || pos == 36) {
					LOG.info(String.format("%d:%d\n", pos, i + 1));
				}
				int[] choices2 = removeOption(choices, pos, i);
				if (choices2 == null)
					return;
				values[pos] = i;
				String text = getJLabel(pos / 9).getText();
				if (text.length() > 100)
					text = text.substring(0, 10);
				getJLabel(pos / 9).setText(text + ".");
				search(pos + 1, choices2);

			}
		}
	}

	private void print() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 81; i++) {
			sb.append("" + values[i] + 1);
			if (i % 9 == 8)
				sb.append("\n");
		}
		sb.append("---------\n");
		LOG.info(sb);

	}

	private int[] removeOption(int[] choices, int pos, int val) {
		LOG.debug("Pos :" + pos);
		int ret[] = new int[81];
		System.arraycopy(choices, 0, ret, 0, 81);
		int row = pos / 9;
		int col = pos % 9;
		int zoneCol = (col / 3) * 3;
		int zoneRow = (row / 3) * 3;
		for (int i = 0; i < 9; i++) {
			int posRow = (row * 9) + i;
			int posCol = (i * 9) + col;
			ret[posRow] = ret[posRow] & notmasks[val];
			if (posCol != posRow) {
				ret[posCol] = ret[posCol] & notmasks[val];
			}

			int cZone = (i / 3) + zoneCol;
			int rZone = (i % 3) + zoneRow;
			//
			if (cZone != col && rZone != row) {
				int posZone = (rZone * 9) + cZone;
				LOG.debug(" ... " + posZone);
				ret[posZone] = ret[posZone] & notmasks[val];
			}
		}
		return ret;
	}
}
