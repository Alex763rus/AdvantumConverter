package com.example.advantumconverter.service.excel;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.Date;


@Component
public class ConvertServiceBase {

    protected XSSFSheet sheet;

    protected int LAST_COLUMN_NUMBER;
    public static String SHEET_RESULT_NAME = "ИМПОРТ";
    @Autowired
    protected PrepareService prepareService;

    protected String getCellValue(int row, int col) {
        if (sheet.getRow(row) == null) {
            return "";
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }

    protected Date getCellDate(int row, int col) {
        if (sheet.getRow(row) == null) {
            return null;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return null;
        }
        return sheet.getRow(row).getCell(col).getDateCellValue();
    }
}
