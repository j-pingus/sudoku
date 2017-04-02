package gee.sudoku.io;

import gee.sudoku.krn.Matrice;
import gee.sudoku.ui.SudokuFrame;

import java.io.*;

/**
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: MatriceFile.java,v $
 * @log Revision 1.2  2009/09/30 21:40:31  gev
 * @log news
 * @log
 * @log Revision 1.1  2009/07/08 11:28:58  gev
 * @log refactored
 * @log
 * @log Revision 1.1  2009/07/01 09:44:22  gev
 * @log Header comment added with CVS link
 * @log
 */
public class MatriceFile {
    public static void write(Matrice mat, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            write(mat, out);
            out.close();
            System.out.println("file:" + file + " written");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void write(Matrice mat, OutputStream out) {

        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.write(mat.getValues());
            dos.writeUTF(mat.getReferenceString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Matrice read(File file) {
        Matrice mat = null;
        try {
            FileInputStream in = new FileInputStream(file);
            mat = read(in);
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mat;
    }

    public static Matrice read(InputStream in) {
        byte buffer[] = new byte[81];
        int size = 9;
        Matrice mat = new Matrice(size);
        try {
            DataInputStream dis = new DataInputStream(in);
            dis.read(buffer);
            int values[][] = new int[size][size];
            int b = 0;
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    values[i][j] = (int) buffer[b++];
            mat.init(values);
            if (dis.available() > 0)
                mat.setReferenceString(dis.readUTF());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mat;
    }

    public static void main(String[] args) {
        SudokuFrame frame = new SudokuFrame();
        frame.setMatrice(read(new File("1240170504437.sudoku")));
        frame.showMatrice();
        frame.setVisible(true);
    }

    public Matrice getSudokuSample() {
        Matrice mat = read(this.getClass().getResourceAsStream("sample.sudoku"));
        return mat;
    }

    public Matrice getWordokuSample() {
        Matrice mat = read(this.getClass().getResourceAsStream("sample.wordoku"));
        return mat;
    }
}
