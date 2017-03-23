package gee.sudoku.io;

import gee.sudoku.krn.Matrice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
public class ExcellFileTest {
    Matrice mat;
    File excel;

    @Before
    protected void setUp() throws Exception {
        mat = MatriceFile.read(getClass().getResourceAsStream(
                "../sample.sudoku"));
        excel = new File("target",
                "sample.xls");
    }

    @Test
    public void testSave() {
        try {
            ExcellFile.save(mat, excel);
            Assert.assertTrue(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
    }

}
