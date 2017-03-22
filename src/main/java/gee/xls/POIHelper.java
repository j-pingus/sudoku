package gee.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class POIHelper {
	short lastColor = 63;
	private static Logger log = Logger.getLogger(POIHelper.class);

	public POIHelper() {
	}

	public static HSSFWorkbook open(File excel) throws Exception {
		FileInputStream myxls = null;
		HSSFWorkbook wb = null;

		try {
			myxls = new FileInputStream(excel);
			wb = new HSSFWorkbook(myxls);
			log.info("Excel loaded " + excel);
		} finally {
			if(myxls != null) {
				myxls.close();
			}

		}

		return wb;
	}

	public static HSSFWorkbook open(InputStream excel) throws Exception {
		HSSFWorkbook wb = null;

			wb = new HSSFWorkbook(excel);
			log.info("Excel loaded " + excel);

		return wb;
	}

	public static void save(HSSFWorkbook wb, File excel) throws Exception {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(excel);
			wb.write(out);
			log.info("Workbook saved " + excel);
		} finally {
			if(out != null) {
				out.close();
			}

		}

	}

	public HSSFColor getColor(HSSFWorkbook wb, int red, int green, int blue) throws Exception {
		HSSFColor c = wb.getCustomPalette().findColor((byte)red, (byte)green, (byte)blue);
		if(c == null) {
			if(--this.lastColor <= 16) {
				throw new Exception("Exhausted the number of custom colors, consider using less colors...");
			}

			wb.getCustomPalette().setColorAtIndex(this.lastColor, (byte)red, (byte)green, (byte)blue);
		}

		return wb.getCustomPalette().findColor((byte)red, (byte)green, (byte)blue);
	}

	public static void setMargins(HSSFSheet sheet, double header, double footer, double left, double right, double top, double bottom) {
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		sheet.setMargin((short)0, left);
		sheet.setMargin((short)1, right);
		sheet.setMargin((short)2, top);
		sheet.setMargin((short)3, bottom);
		printSetup.setFooterMargin(footer);
		printSetup.setHeaderMargin(header);
	}

	public static int addRow(HSSFSheet sheet, Object... values) {
		return addStyledRow(sheet, (PoiConditionalFormatting[])null, values);
	}

	/** @deprecated */
	public static int addRow(HSSFSheet sheet, HSSFCellStyle style, Object... values) {
		return addStyledRow(sheet, style == null?null:new PoiConditionalFormatting[]{PoiConditionalFormatting.getNoCondition(style)}, values);
	}

	public static int addStyledRow(HSSFSheet sheet, PoiConditionalFormatting[] formattings, Object... values) {
		int rownum = sheet.getLastRowNum() + 1;
		HSSFRow row = sheet.getRow(rownum - 1);
		if(row == null) {
			row = sheet.createRow(rownum - 1);
		} else {
			row = sheet.createRow(rownum);
		}

		short column = 0;
		Object[] arr$ = values;
		int len$ = values.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			Object value = arr$[i$];
			HSSFCell cell = row.createCell(column++);
			if(formattings != null) {
				PoiConditionalFormatting[] arr$1 = formattings;
				int len$1 = formattings.length;

				for(int i$1 = 0; i$1 < len$1; ++i$1) {
					PoiConditionalFormatting f = arr$1[i$1];
					if(f.applies(row.getRowNum(), column, value)) {
						cell.setCellStyle(f.getStyle());
					}
				}
			}

			if(value instanceof String) {
				cell.setCellValue(new HSSFRichTextString((String)value));
			}

			if(value instanceof Integer) {
				cell.setCellValue((double)((Integer)value).intValue());
			}

			if(value instanceof Boolean) {
				cell.setCellValue(((Boolean)value).booleanValue());
			}

			if(value instanceof Double) {
				cell.setCellValue(((Double)value).doubleValue());
			}

			if(value instanceof Date) {
				cell.setCellValue((Date)value);
			}

			if(value instanceof Long) {
				cell.setCellValue(((Long)value).doubleValue());
			}
		}

		return rownum;
	}

	/** @deprecated */
	public static int addRow(HSSFSheet sheet, HSSFCellStyle style, HSSFCellStyle styleFaulty, Object... values) {
		return addStyledRow(sheet, new PoiConditionalFormatting[]{PoiConditionalFormatting.getBooleanCondition(true, style), PoiConditionalFormatting.getBooleanCondition(false, styleFaulty)}, values);
	}

	public static void setColumnWidth(HSSFSheet sheet, int colnum, int pixels) {
		sheet.setColumnWidth((short)colnum, (short)((int)((double)((float)pixels) * 36.71D)));
	}

	public static void addHelperSheets(HSSFWorkbook wb) {
		HSSFSheet s = wb.createSheet("Color scheme");

		short color;
		HSSFRow r;
		for(color = 0; color < 10; ++color) {
			r = s.createRow(color);

			for(short cs = 0; cs < 10; ++cs) {
				HSSFFont c = wb.createFont();
				c.setFontHeightInPoints((short)10);
				c.setColor(color);
				HSSFCellStyle cs1 = wb.createCellStyle();
				cs1.setFont(c);
				cs1.setFillForegroundColor(cs);
				cs1.setFillPattern((short)1);
				HSSFCell c1 = r.createCell(cs);
				c1.setCellValue("f: " + color + " b: " + cs);
				c1.setCellStyle(cs1);
			}
		}

		for(color = 10; color < 64; ++color) {
			r = s.createRow(color);
			HSSFCellStyle var8 = wb.createCellStyle();
			var8.setFillForegroundColor(color);
			var8.setFillPattern((short)1);
			HSSFCell var9 = r.createCell((short)0);
			var9.setCellValue("color " + color);
			var9 = r.createCell((short)1);
			var9.setCellValue(" sample ");
			var9.setCellStyle(var8);
		}

	}
}
