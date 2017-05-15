package gee.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

public class SudokuPanelTest extends JFrame implements KeyListener {
	private static final Logger LOG = Logger.getLogger(SudokuPanelTest.class);

	private final JPanel contentPane;
	private final SudokuPanel sudokuPanel;
	JLabel lblSolutions;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SudokuPanelTest frame = new SudokuPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					LOG.error("Error", e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SudokuPanelTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 514);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(224, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		sudokuPanel = new SudokuPanel();
		contentPane.add(sudokuPanel, BorderLayout.CENTER);
		addKeyListener(sudokuPanel);

		lblSolutions = new JLabel("Solutions:");
		// lblSolutions.setFont(new Font("Arial Black", Font.BOLD, 22));
		contentPane.add(lblSolutions, BorderLayout.SOUTH);
		addKeyListener(this);
	}

	private double countSolutions(int sudoku[]) {
		int choices[] = new int[81];
		for (int i = 0; i < 81; i++) {
			choices[i] = alloptions;
		}
		for (int i = 0; i < 81; i++) {
			if (sudoku[i] != 0) {
				choices[i] = masks[sudoku[i] - 1];
				removeOption(choices, i, sudoku[i] - 1);
			}
		}
		solutions = 0D;
		search(0, choices);
		return solutions;
	}

	private void search(int pos, int[] choices) {
		if (pos == 81 || solutions > 1000) {
			solutions++;
			return;
		}
		for (int i = 0; i < 9; i++) {
			if ((choices[pos] & masks[i]) > 0) {
				int[] choices2 = new int[81];
				System.arraycopy(choices, 0, choices2, 0, 81);
				if (removeOption(choices2, pos, i)) {
					search(pos + 1, choices2);
				}
			}
		}
	}

	int masks[] = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
	int notmasks[] = new int[] { ~1, ~2, ~4, ~8, ~16, ~32, ~64, ~128, ~256 };
	int alloptions = 1 | 2 | 4 | 8 | 16 | 32 | 64 | 128 | 256;
	double solutions = 0D;

	private boolean removeOption(int[] choices, int pos, int val) {
		int row = pos / 9;
		int col = pos % 9;
		int zoneCol = (col / 3) * 3;
		int zoneRow = (row / 3) * 3;
		for (int i = 0; i < 9; i++) {
			int posRow = (row * 9) + i;
			if (posRow != pos && (choices[posRow] &= notmasks[val]) == 0) {
				return false;
			}
			int posCol = (i * 9) + col;
			if (posCol != posRow && posCol != pos
					&& (choices[posCol] &= notmasks[val]) == 0) {
				return false;
			}
			int cZone = (i / 3) + zoneCol;
			int rZone = (i % 3) + zoneRow;
			if (cZone != col && rZone != row) {
				int posZone = (rZone * 9) + cZone;
				if (posZone != pos
						&& (choices[posZone] &= notmasks[val]) == 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		double solutions = countSolutions(sudokuPanel.getSudoku());
		if (solutions > 1000D) {
			lblSolutions.setText("Solutions : >1000");
		} else {
			lblSolutions.setText("Solutions : " + solutions);
		}

	}

}
