package gee.sudoku.io;

import java.io.File;

import gee.xls.POIHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import gee.sudoku.krn.Cell;
import gee.sudoku.krn.Matrice;
import gee.sudoku.krn.MatriceZone;

public class ExcellFile {
	public static void save(Matrice mat, File excel) throws Exception {
		HSSFWorkbook wb = POIHelper.open(ExcellFile.class
				.getResourceAsStream("../Sudoku.xls"));

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFFont fontBlue = wb.createFont();
		fontBlue.setColor(HSSFColor.BLUE.index);
		fontBlue.setFontHeightInPoints((short) 11);
		HSSFCellStyle styleValue = wb.createCellStyle();
		styleValue.setFont(fontBlue);
		styleValue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleValue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		short rowNum = 0;
		for (MatriceZone rowZone : mat.getRows()) {
			short colNum = 0;
			for (Cell cell : rowZone.getCells()) {
				for (short rowXls = (short) ((rowNum * 3) + 3); rowXls < (rowNum * 3) + 6; rowXls++) {
					HSSFRow row = sheet.getRow(rowXls);
					for (short colXls = (short) ((colNum * 3) + 1); colXls < (colNum * 3) + 4; colXls++) {
						HSSFCell cellXls = row.getCell(colXls);
						if (cell.isSet()) {
							if (colXls == (colNum * 3) + 2
									&& rowXls == (rowNum * 3) + 4) {
								cellXls.setCellValue(new HSSFRichTextString(""
										+ cell.getValue()));
								cellXls.setCellStyle(styleValue);
							} else {
								cellXls
										.setCellValue(new HSSFRichTextString(""));
							}
						} else {
							Double val = cellXls.getNumericCellValue();
							if (!cell.hasChoice(val.intValue())) {
								cellXls
										.setCellValue(new HSSFRichTextString(""));
							}
						}
					}
				}
				colNum++;
			}
			rowNum++;
		}
		POIHelper.save(wb, excel);

	}
}
