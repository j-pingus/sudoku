package gee.sudoku.ui;

import gee.sudoku.io.ExcellFile;
import gee.sudoku.io.MatriceFile;
import gee.sudoku.krn.*;
import gee.sudoku.solver.Solver;
import gee.sudoku.solver.Strategies;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SudokuApplication {
    private JFrame jFrame = null; // @jve:decl-index=0:visual-constraint="10,10"

    ; // @jve:decl-index=0:
    private JPanel jContentPane = null;

    ; // @jve:decl-index=0:
    private JMenuBar jJMenuBar = null;

    ; // @jve:decl-index=0:
    private JMenu fileMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JDialog aboutDialog = null;  //  @jve:decl-index=0:visual-constraint="618,310"
    private JPanel aboutContentPane = null;
    private JLabel aboutVersionLabel = null;
    private JMenuItem openMenuItem = null;
    private JToolBar jHighlight = null;
    private JMenu sudokuMenu = null;
    private JMenu Highlight = null;
    private Action undoAction = null;
    private Action redoAction = null;
    private JMenuItem undoMenuItem = null;
    private JMenuItem redoMenuItem = null;
    private JMenuItem saveExcelMenuItem = null;
    private JMenuItem solveMenuItem = null;
    private JTabbedPane matriceTabbedPane = null;
    private JTabbedSudoku jPanelMatrice = null;
    private JMenuItem newMenuItem = null;
    private JMenuItem highlightPairsMenuItem = null;
    private JMenuItem provideHintMenuItem = null;
    private JDialog hintDialog = null;  //  @jve:decl-index=0:visual-constraint="573,77"
    private JPanel hintContentPane1 = null;
    private JLabel descriptionLabel = null;
    private JLabel strategyNameLabel = null;
    private JLabel descriptionLabel1 = null;
    private JLabel valueLabel = null;
    private JLabel descriptionLabel2 = null;
    private JLabel referencesLabel = null;
    private JPanel hintButtonsPanel = null;
    private JButton applyButton = null;
    private JButton cancelButton = null;

    /**
     * Launches this application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SudokuApplication application = new SudokuApplication();
                application.getJFrame().setVisible(true);
            }
        });
    }

    /**
     * This method initializes jFrame
     *
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setName("sudoku");
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(545, 563);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Sudoku solver");
            getActiveTab().newMatrice();
        }
        return jFrame;
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJHighlight(), BorderLayout.EAST);
            jContentPane.add(getMatriceTabbedPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getSudokuMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.setMnemonic(KeyEvent.VK_F);
            fileMenu.add(getOpen());
            fileMenu.add(getSaveMenuItem());
            fileMenu.add(getSaveExcelMenuItem());
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.setMnemonic(KeyEvent.VK_H);
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
     * This method initializes aboutDialog
     *
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("About");
            aboutDialog.setSize(new Dimension(345, 159));
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

    /**
     * This method initializes aboutContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (aboutContentPane == null) {
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(new BorderLayout());
            aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
        }
        return aboutContentPane;
    }

    /**
     * This method initializes aboutVersionLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (aboutVersionLabel == null) {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel
                    .setText("Sudoku Application 0.1, \nhelps you solve your Sudoku's!");
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText("Save");
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Event.CTRL_MASK, true));
            saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getActiveTab().save();
                }
            });
        }
        return saveMenuItem;
    }

    /**
     * This method initializes Open
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpen() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText("Open");
            openMenuItem.setMnemonic('O');
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                    Event.CTRL_MASK, true));

            openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getActiveTab().open();
                }
            });
        }
        return openMenuItem;
    }

    /**
     * This method initializes jPanelMatrice
     *
     * @return gee.sudoku.ui.JPanelMatrice
     */
    private JPanelMatrice getJPanelMatrice() {
        if (jPanelMatrice == null) {
            jPanelMatrice = new JTabbedSudoku();
        }
        return jPanelMatrice;
    }

    /**
     * This method initializes jHighlight
     *
     * @return javax.swing.JToolBar
     */
    private JToolBar getJHighlight() {
        if (jHighlight == null) {
            jHighlight = new JToolBar();
            jHighlight.setFloatable(true);
            jHighlight.setOrientation(JToolBar.VERTICAL);
            for (int i = 1; i <= 9; i++) {
                HighlightAction action = new HighlightAction(i);
                JButton button = new JButton(action);
                button.setText("" + i);
                getHighlight().add(getHighLightMenu(action));
                jHighlight.add(button);
            }
        }
        return jHighlight;
    }

    /**
     * This method initializes sudokuMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getSudokuMenu() {
        if (sudokuMenu == null) {
            sudokuMenu = new JMenu();
            sudokuMenu.setText("Sudoku");
            sudokuMenu.setMnemonic(KeyEvent.VK_S);
            sudokuMenu.add(getHighlight());
            sudokuMenu.add(getUndoMenuItem());
            sudokuMenu.add(getRedoMenuItem());
            sudokuMenu.add(getSolveMenuItem());
            sudokuMenu.add(getNewMenuItem());
            sudokuMenu.add(getProvideHintMenuItem());
        }
        return sudokuMenu;
    }

    /**
     * This method initializes Highlight
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHighlight() {
        if (Highlight == null) {
            Highlight = new JMenu();
            Highlight.setText("highlight");
            Highlight.setMnemonic(KeyEvent.VK_H);
            Highlight.add(getHighlightPairsMenuItem());
        }
        return Highlight;
    }

    /**
     * This method initializes highLight1
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getHighLightMenu(HighlightAction action) {
        JMenuItem ret = new JMenuItem();
        ret.setAction(action);
        ret.setText("Highlight " + action.value);
        ret.setMnemonic(KeyEvent.VK_1 + action.value - 1);
        return ret;
    }

    /**
     * This method initializes undoMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getUndoMenuItem() {
        if (undoMenuItem == null) {
            undoMenuItem = new JMenuItem();
            undoMenuItem.setAction(getUndoAction());
            undoMenuItem.setText("Undo");
        }
        return undoMenuItem;
    }

    /**
     * This method initializes undoMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getRedoMenuItem() {
        if (redoMenuItem == null) {
            redoMenuItem = new JMenuItem();
            redoMenuItem.setAction(getRedoAction());
            redoMenuItem.setText("Redo");
        }
        return redoMenuItem;
    }

    /**
     * This method initializes saveExcelMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveExcelMenuItem() {
        if (saveExcelMenuItem == null) {
            saveExcelMenuItem = new JMenuItem();
            saveExcelMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            getActiveTab().saveExcel();
                        }
                    });
        }
        return saveExcelMenuItem;
    }

    /**
     * This method initializes solveMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSolveMenuItem() {
        if (solveMenuItem == null) {
            solveMenuItem = new JMenuItem();
            solveMenuItem.setAction(new SolveAction());
            solveMenuItem.setText("Solve");
        }
        return solveMenuItem;
    }

    /**
     * This method initializes matriceTabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getMatriceTabbedPane() {
        if (matriceTabbedPane == null) {
            matriceTabbedPane = new JTabbedPane();
            matriceTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
            matriceTabbedPane.addTab("Sudoku", null, getJPanelMatrice(), null);
        }
        return matriceTabbedPane;
    }

    /**
     * This method initializes newMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getNewMenuItem() {
        if (newMenuItem == null) {
            newMenuItem = new JMenuItem();
            newMenuItem.setText("New Matrice");
            newMenuItem.setMnemonic(KeyEvent.VK_N);
            newMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getActiveTab().newMatrice();
                }
            });
        }
        return newMenuItem;
    }

    /**
     * This method initializes highlightPairsMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getHighlightPairsMenuItem() {
        if (highlightPairsMenuItem == null) {
            highlightPairsMenuItem = new JMenuItem();
            highlightPairsMenuItem.setMnemonic(KeyEvent.VK_P);
            highlightPairsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_P, ActionEvent.ALT_MASK));
            highlightPairsMenuItem.setText("Pairs");
            highlightPairsMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            getActiveTab().hightlihtPairs();
                        }
                    });
        }
        return highlightPairsMenuItem;
    }

    /**
     * This method initializes provideHintMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getProvideHintMenuItem() {
        if (provideHintMenuItem == null) {
            provideHintMenuItem = new JMenuItem();
            provideHintMenuItem.setMnemonic(KeyEvent.VK_P);
            provideHintMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_H, ActionEvent.CTRL_MASK));
            provideHintMenuItem.setText("Hint");
            provideHintMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            getActiveTab().hint();
                        }
                    });
        }
        return provideHintMenuItem;
    }

    /**
     * This method initializes hintDialog
     *
     * @return javax.swing.JDialog
     */
    private JDialog getHintDialog() {
        if (hintDialog == null) {
            hintDialog = new JDialog(getJFrame());
            hintDialog.setSize(new Dimension(384, 192));
            hintDialog.setContentPane(getHintContentPane1());
            Point loc = getJFrame().getLocation();
            loc.translate(20, 20);
            hintDialog.setLocation(loc);
        }
        return hintDialog;
    }

    /**
     * This method initializes hintContentPane1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getHintContentPane1() {
        if (hintContentPane1 == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.weightx = 0.0D;
            gridBagConstraints6.gridwidth = 2;
            gridBagConstraints6.gridy = 3;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.gridy = 2;
            referencesLabel = new JLabel();
            referencesLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            referencesLabel.setText("... value");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.gridy = 2;
            descriptionLabel2 = new JLabel();
            descriptionLabel2.setText("references");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 1;
            valueLabel = new JLabel();
            valueLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            valueLabel.setText("... value");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.anchor = GridBagConstraints.EAST;
            gridBagConstraints11.gridy = 1;
            descriptionLabel1 = new JLabel();
            descriptionLabel1.setText("Impacted value");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 0;
            strategyNameLabel = new JLabel();
            strategyNameLabel.setText("... name");
            strategyNameLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.gridy = 0;
            descriptionLabel = new JLabel();
            descriptionLabel.setText("Proposed strategy");
            hintContentPane1 = new JPanel();
            hintContentPane1.setLayout(new GridBagLayout());
            hintContentPane1.add(descriptionLabel, gridBagConstraints);
            hintContentPane1.add(strategyNameLabel, gridBagConstraints1);
            hintContentPane1.add(descriptionLabel1, gridBagConstraints11);
            hintContentPane1.add(valueLabel, gridBagConstraints2);
            hintContentPane1.add(descriptionLabel2, gridBagConstraints3);
            hintContentPane1.add(referencesLabel, gridBagConstraints4);
            hintContentPane1.add(getHintButtonsPanel(), gridBagConstraints6);
        }
        return hintContentPane1;
    }

    /**
     * This method initializes hintButtonsPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getHintButtonsPanel() {
        if (hintButtonsPanel == null) {
            hintButtonsPanel = new JPanel();
            hintButtonsPanel.setLayout(new GridBagLayout());
            hintButtonsPanel.setBackground(Color.cyan);
            hintButtonsPanel.add(getApplyButton(), new GridBagConstraints());
            hintButtonsPanel.add(getCancelButton(), new GridBagConstraints());
        }
        return hintButtonsPanel;
    }

    /**
     * This method initializes applyButton
     *
     * @return javax.swing.JButton
     */
    private JButton getApplyButton() {
        if (applyButton == null) {
            applyButton = new JButton();
            applyButton.setText("Apply");
            applyButton.setMnemonic(KeyEvent.VK_UNDEFINED);
            applyButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //FIXME: find better way to perform the action
                    getActiveTab().redo();
                    getActiveTab().hint();
                }
            });
        }
        return applyButton;
    }

    /**
     * This method initializes cancelButton
     *
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getHintDialog().setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    public Action getUndoAction() {
        if (undoAction == null) {
            undoAction = new UndoAction();
        }
        return undoAction;
    }

    public Action getRedoAction() {
        if (redoAction == null) {
            redoAction = new RedoAction();
        }
        return redoAction;
    }

    public JTabbedSudoku getActiveTab() {
        Component c = getMatriceTabbedPane().getSelectedComponent();
        if (c != null) {
            return (JTabbedSudoku) c;
        }
        return null;
    }

    public void removeTab(JTabbedSudoku tab) {
        int i = 0;
        for (Component c : getMatriceTabbedPane().getComponents()) {
            if (c == tab) {
                getMatriceTabbedPane().removeTabAt(i);
            }
            i++;
        }

    }

    class UndoAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public UndoAction() {
            putValue(LONG_DESCRIPTION, "Undo last action");
            putValue(SHORT_DESCRIPTION, "Undo");
            putValue(ACTION_COMMAND_KEY, "undo");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            getActiveTab().undo();
        }
    }

    class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public RedoAction() {
            putValue(LONG_DESCRIPTION, "Redo last action");
            putValue(SHORT_DESCRIPTION, "Redo");
            putValue(ACTION_COMMAND_KEY, "redo");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                    Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            getActiveTab().redo();
        }
    }

    class SolveAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public SolveAction() {
            putValue(LONG_DESCRIPTION, "Find sudoku solution");
            putValue(SHORT_DESCRIPTION, "Resolve");
            putValue(ACTION_COMMAND_KEY, "Resolve");
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R,
                    Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        getActiveTab().solve();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });

        }
    }

    class HighlightAction extends AbstractAction {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        int value;

        public HighlightAction(int value) {
            this.value = value;
            putValue(LONG_DESCRIPTION, "Highligt value " + value
                    + " everywhere in the matrice");
            putValue(SHORT_DESCRIPTION, "show " + value + "'s");
            putValue(ACTION_COMMAND_KEY, "highlight_" + value);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_0
                    + value, ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            getActiveTab().highlight(value);
        }
    }

    class JTabbedSudoku extends JPanelMatrice implements SudokuPresenter,
            CellListener {
        /**

         */
        private static final long serialVersionUID = 1L;
        Matrice matrice;

        JTabbedSudoku() {
            setCellListener(this);
        }

        private void superSetValue(int row, int col, int value) {
            super.setValue(row, col, value);
        }

        private void superUnsetValue(int row, int col, int value) {
            super.unsetValue(row, col, value);
        }

        private void superSetHints(int row, int col, Integer... values) {
            super.setHints(row, col, values);
        }

        public void show(Matrice m) {
            matrice = m;

            int row = 0;
            init();
            for (Cell lines[] : matrice.getCells()) {
                int col = 0;
                for (Cell cell : lines) {
                    if (cell.isSet()) {
                        superSetValue(row, col, cell.getValue());
                    } else {
                        superSetHints(row, col, cell.getChoices());
                    }
                    col++;
                }
                row++;
            }
            String title;
            try {
                matrice.check();
                title = "Status : " + matrice.getStatus();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                title = "Coherent : " + e.getLocalizedMessage();
            }
            jFrame.setTitle(title);

        }

        public void showMessage(Strategies s, CellReference... cellReferences) {
            highlight(cellReferences);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            normallight(cellReferences);
        }

        public boolean confirmMessage(Strategies s,
                                      CellReference... cellReferences) {
            int answer = JOptionPane.showConfirmDialog(this, s.toString(),
                    "sudoku", JOptionPane.OK_CANCEL_OPTION);
            return answer == JOptionPane.OK_OPTION;
        }

        public SudokuPresenter duplicate() {
            JTabbedSudoku ret = new JTabbedSudoku();
            getMatriceTabbedPane().addTab("Sudoku", null, ret, null);
            return ret;
        }

        public void kill() {
            // TODO Auto-generated method stub
            removeTab(this);
        }

        public void setHint(int row, int col, int value) {
            // TODO Auto-generated method stub
            if (matrice != null) {
                matrice.addChoice(row, col, value);
                if (matrice.getHistory() != null)
                    matrice.getHistory().commit(Strategies.HUMAN);
            }
        }

        public void setValue(int row, int col, int value) {
            // TODO Auto-generated method stub
            if (matrice != null) {
                matrice.setValue(row, col, value);
                if (matrice.getHistory() != null)
                    matrice.getHistory().commit(Strategies.HUMAN);
                show(matrice);
            }
        }

        public void unsetValue(int row, int col, int value) {
            // TODO Auto-generated method stub
            if (matrice != null) {
                matrice.unsetValue(row, col, value);
                if (matrice.getHistory() != null)
                    matrice.getHistory().commit(Strategies.HUMAN);
                show(matrice);
            }
        }

        public void unsetHint(int row, int col, int value) {
            // TODO Auto-generated method stub
            if (matrice != null) {
                matrice.removeChoice(row, col, value);
                if (matrice.getHistory() != null)
                    matrice.getHistory().commit(Strategies.HUMAN);
            }

        }

        private void newMatrice() {
            matrice = new Matrice(9);
            matrice.setHistory(new MatriceHistory());
            show(matrice);
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
                Matrice mat = MatriceFile.read(file);
                matrice = mat;
                matrice.setHistory(new MatriceHistory());
                show(mat);
            }

        }

        public void solve() throws Exception {
            Solver solver = new Solver(matrice);
            solver.setGuessing(true);
            solver.solve();
        }

        public void redo() {
            matrice.getHistory().redo(matrice);
            show(matrice);
        }

        public void undo() {
            matrice.getHistory().undo(matrice);
            show(matrice);
        }

        public void save() {
            if (matrice.getReferenceString().equals("123456789")) {
                MatriceFile.write(matrice, new File(""
                        + System.currentTimeMillis() + ".sudoku"));
            } else {
                MatriceFile.write(matrice, new File(""
                        + System.currentTimeMillis() + ".wordoku"));
            }

        }

        public void saveExcel() {
            try {
                File excel = File.createTempFile("sudoku", ".xls",
                        new File("."));
                ExcellFile.save(matrice, excel);
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

        public void hightlihtPairs() {
            CellReference pairs[] = matrice.getPairs();
            highlight(pairs);
        }

        public void hint() {
            JDialog hintDialog = getHintDialog();
            try {
                MatriceAction action = new Solver(matrice).getNextAction();
                if (action != null) {
                    highlight(0);
                    for (MatriceZone zone : action.getHints()) {
                        for (Cell cell : zone.getCells()) {
                            if (!cell.isSet())
                                highlight(Color.GREEN, cell);
                        }
                    }
                    strategyNameLabel.setText(action.getStrategy().toString() + "\n" + action.getStrategy().getDescription());
                    valueLabel.setText(Arrays.toString(action.getHintValues()));
                    referencesLabel.setText(action.size() + " will be steps applied");
                    hintDialog.pack();
                    getHintDialog().setVisible(true);
                } else {
                    highlight(0);
                    getHintDialog().setVisible(false);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
