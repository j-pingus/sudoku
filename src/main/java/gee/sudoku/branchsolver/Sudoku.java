package gee.sudoku.branchsolver;

public class Sudoku {
    byte cells[] = new byte[81];

    public Sudoku() {
        cells = new byte[81];
        for (int i = 0; i < 81; i++) {
            cells[i] = 0;
        }
    }

    public Sudoku(byte... init) {
        this();
        for (int i = 0; i < cells.length && i < 81; i++) {
            this.cells[i] = init[i];
        }
    }

    public Sudoku(Options c) {
        this();
        for (int i = 0; i < 81; i++) {
            this.cells[i] = c.value(i);
        }
    }

    public byte[] getCells() {
        return cells;
    }

    public String getFileName() {
        StringBuilder sb = new StringBuilder();
        for (byte b : cells) {
            sb.append(b);
        }
        sb.append(".sudoku");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 81; i++) {
            if (cells[i] > 0) {
                sb.append(cells[i]);
            } else {
                sb.append(' ');
            }
            if (i % 9 == 8) {
                sb.append('\n');
            } else if (i % 3 == 2) {
                sb.append("|");
            } else {
                sb.append(' ');
            }
            if (i < 55 && i % 27 == 26) {
                sb.append("-----+-----+-----\n");
            }
        }
        return sb.toString();
    }

    public int score() {
        int ret = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] > 0)
                ret += i;
        }
        return ret;
    }

    public Sudoku mirrorH() {
        byte dest[] = new byte[cells.length];
        for (int i = 0; i < cells.length; i++) {
            dest[i + (((i % 9) - 4) * -2)] = cells[i];
        }
        return new Sudoku(dest);
    }

    public Sudoku flip() {
        //=80-((MOD(B12;9)*9)+MOD(ROUNDDOWN(B12/9;0);9))
        byte dest[] = new byte[cells.length];
        for (int i = 0; i < cells.length; i++) {
            dest[(((i % 9) * 9) + ((i / 9) % 9))] = cells[i];
        }
        return new Sudoku(dest);
    }

    public Sudoku mirrorV() {
        byte dest[] = new byte[cells.length];
        for (int i = 0; i < cells.length; i++) {
            dest[i + (((i / 9) - 4) * -18)] = cells[i];
        }
        return new Sudoku(dest);

    }

    public Sudoku invert() {
        byte dest[] = new byte[cells.length];
        for (int i = 0; i < cells.length; i++) {
            dest[80 - i] = cells[i];
        }
        return new Sudoku(dest);
    }
}
