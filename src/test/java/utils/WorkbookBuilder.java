package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public final class WorkbookBuilder {

    private WorkbookBuilder() {
    }

    public static void addSheet(XSSFWorkbook workbook, String sheetName, List<String[]> rows) {
        Sheet sheet = workbook.createSheet(sheetName);
        fill(sheet, rows);
    }

    private static void fill(Sheet sheet, List<String[]> rows) {
        for (int r = 0; r < rows.size(); r++) {
            String[] values = rows.get(r);
            if (values == null) {
                continue;
            }
            Row row = sheet.createRow(r);
            for (int c = 0; c < values.length; c++) {
                if (values[c] == null) {
                    continue;
                }
                row.createCell(c).setCellValue(values[c]);
            }
        }
    }

    public static XSSFWorkbook build(String sheetName, List<String[]> rows) {
        var workbook = new XSSFWorkbook();
        addSheet(workbook, sheetName, rows);
        return workbook;
    }

    public static String[] row(int columns, int index, String value) {
        String[] values = new String[columns];
        values[index] = value;
        return values;
    }

    public static String[] row(Object... kv) {
        int max = 0;
        for (int i = 0; i < kv.length; i += 2) {
            max = Math.max(max, (Integer) kv[i]);
        }
        String[] arr = new String[max + 1];
        java.util.Arrays.fill(arr, "");
        for (int i = 0; i < kv.length; i += 2) {
            arr[(Integer) kv[i]] = (String) kv[i + 1];
        }
        return arr;
    }
}
