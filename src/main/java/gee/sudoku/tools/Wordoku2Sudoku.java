package gee.sudoku.tools;

import gee.sudoku.krn.Matrice;
import gee.sudoku.io.MatriceFile;

import java.io.File;
import java.util.Scanner;

public class Wordoku2Sudoku {
	public static void main(String[] args) {
		// 1242076531734.wordoku
		System.out.print("Please enter a valid path for wordoku file:");
		Scanner scan = new Scanner(System.in);
		String wordokuFileName = scan.next();
		File wordoku = new File(wordokuFileName);
		File sudoku = new File(wordokuFileName.replace(".wordoku", ".sudoku"));
		if (wordoku.exists() && wordoku.canRead() && sudoku.getAbsoluteFile().getParentFile().canWrite()
				&& !sudoku.exists()) {
			Matrice mat =MatriceFile.read(wordoku);
			mat.setReferenceString("123456789");
			MatriceFile.write(mat, sudoku);
			System.out.println(sudoku+" written");
		}else{
			System.out.println("Check that "+wordoku+" exists and "+sudoku+" doesn't");
		}

	}
}
