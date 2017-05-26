package gee.sudoku.io;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gee.sudoku.krn.Matrice;

public class ExcellFileTest {
	private static final Logger LOG = Logger.getLogger(ExcellFileTest.class);
	Matrice mat;
	File excel;

	@Before
	public void setUp() throws Exception {
		mat = MatriceFile
				.read(getClass().getResourceAsStream("../sample.sudoku"));
		excel = new File("target", "sample.xls");
	}

	@Test
	public void testSave() {
		try {
			ExcellFile.save(mat, excel);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOG.error("can't save", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

}
