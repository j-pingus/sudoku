package gee.xls;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public abstract class PoiConditionalFormatting {
    public PoiConditionalFormatting() {
    }

    public abstract boolean applies(int var1, int var2, Object var3);

    public abstract HSSFCellStyle getStyle();

    public static PoiConditionalFormatting getBooleanCondition(final boolean value, final HSSFCellStyle style) {
        return new PoiConditionalFormatting() {
            public boolean applies(int row, int column, Object valuex) {
                return valuex instanceof Boolean && ((Boolean)valuex).booleanValue() == value;
            }

            public HSSFCellStyle getStyle() {
                return style;
            }
        };
    }

    public static PoiConditionalFormatting getStringCondition(final String value, final HSSFCellStyle style) {
        return new PoiConditionalFormatting() {
            public boolean applies(int row, int column, Object valuex) {
                return valuex instanceof String && value.equals(valuex);
            }

            public HSSFCellStyle getStyle() {
                return style;
            }
        };
    }

    public static PoiConditionalFormatting getNoCondition(final HSSFCellStyle style) {
        return new PoiConditionalFormatting() {
            public boolean applies(int row, int column, Object value) {
                return true;
            }

            public HSSFCellStyle getStyle() {
                return style;
            }
        };
    }
}
