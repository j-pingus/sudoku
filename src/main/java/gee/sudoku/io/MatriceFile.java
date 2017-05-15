package gee.sudoku.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import gee.sudoku.branchsolver.Sudoku;
import gee.sudoku.krn.Matrice;

/**
 * @author Even
 * @version $Revision: 1.2 $
 * @log $Log: MatriceFile.java,v $
 * @log Revision 1.2 2009/09/30 21:40:31 gev
 * @log news
 * @log
 * @log Revision 1.1 2009/07/08 11:28:58 gev
 * @log refactored
 * @log
 * @log Revision 1.1 2009/07/01 09:44:22 gev
 * @log Header comment added with CVS link
 * @log
 */
public class MatriceFile {
	private static final Logger LOG = Logger.getLogger(MatriceFile.class);

	public static void write(Matrice mat, File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			write(mat, out);
			out.close();
			LOG.info("file:" + file + " written");
		} catch (FileNotFoundException e) {
			LOG.error("can't produce file " + file, e);
		} catch (IOException e) {
			LOG.error("can't produce file " + file, e);
		}

	}

	public static void write(Matrice mat, OutputStream out) {
		try {
			DataOutputStream dos = new DataOutputStream(out);
			dos.write(mat.getValues());
			dos.writeUTF(mat.getReferenceString());
		} catch (IOException e) {
			LOG.error("can't write ", e);
		}
	}

	public static void write(Sudoku mat, OutputStream out) {
		try {
			DataOutputStream dos = new DataOutputStream(out);
			dos.write(mat.getCells());
		} catch (IOException e) {
			LOG.error("can't write ", e);
		}
	}

	public static Matrice read(File file) {
		Matrice mat = null;
		try {
			FileInputStream in = new FileInputStream(file);
			mat = read(in);
			in.close();
		} catch (FileNotFoundException e) {
			LOG.error("can't read " + file, e);
		} catch (IOException e) {
			LOG.error("can't read " + file, e);
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
			mat.init(buffer);
			if (dis.available() > 0)
				mat.setReferenceString(dis.readUTF());
		} catch (FileNotFoundException e) {
			LOG.error("can't read ", e);
		} catch (IOException e) {
			LOG.error("can't read ", e);
		}
		return mat;
	}

	public static void write(Sudoku mat, File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			write(mat, out);
			out.close();
			LOG.info("file:" + file + " written");
		} catch (FileNotFoundException e) {
			LOG.error("Can't write " + file, e);
		} catch (IOException e) {
			LOG.error("Can't write " + file, e);
		}

	}
}
