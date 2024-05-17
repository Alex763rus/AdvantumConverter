package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.ConvertedList;
import com.example.advantumconverter.service.database.DictionaryService;
import lombok.val;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;


@Component
public class ConvertServiceBase {

    @Autowired
    protected DictionaryService dictionaryService;

    protected XSSFSheet sheet;

    protected int LAST_COLUMN_NUMBER;
    protected int LAST_ROW;

    protected XSSFSheet getExcelList(XSSFWorkbook book, String listName) {
        return Optional.ofNullable(book.getSheet(listName))
                .orElseThrow(() -> new ExcelListNotFoundException(listName));
    }

    protected String getCellValue(int row, int col) {
        if (sheet.getRow(row) == null) {
            return EMPTY;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return EMPTY;
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }

    protected String getCellWindowValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return EMPTY;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return EMPTY;
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected List<List<String>> getDataFromSheet(XSSFWorkbook book, String sheetName, final int colStart, final int countColumns) {
        val data = new ArrayList<List<String>>();
        val windowSheet = book.getSheet(sheetName);
        val colEnd = colStart + countColumns;
        for (int row = 0; ; row++) {
            val cellValue = getCellWindowValue(windowSheet, row, 0);
            val nextValue = getCellWindowValue(windowSheet, row + 1, 0);
            if (cellValue.equals(EMPTY) && nextValue.equals(EMPTY)) {
                --row;
                break;
            }
            val line = new ArrayList<String>();
            for (int column = colStart; column < colEnd; ++column) {
                line.add(getCellWindowValue(windowSheet, row, column));
            }
            data.add(line);
        }
        return data;
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

    protected int getLastRow(int startRow) {
        int i = startRow;
        for (; ; i++) {
            if (getCellValue(i, 0).equals(EMPTY)) {
                if (getCellValue(i + 1, 0).equals(EMPTY)) {
                    break;
                }
            }
        }
        return i - 1;
    }

    private final int MAX_ROW = 10000;

    protected int getStartRow(String startRowText) {
        for (int i = 0; i < MAX_ROW; i++) {
            if (getCellValue(i, 0).equals(startRowText)) {
                return i + 1;
            }
        }
        return 0;
    }

    protected String getCurrentDate(String format) throws ParseException {
        return convertDateFormat(new Date(), format);
    }

    protected Long getLongValue(int row, int col) {
        val cellValue = getCellValue(row, col);
        return cellValue.equals(EMPTY) ? null : Long.parseLong(cellValue);
    }

    protected Integer getIntegerValue(int row, int col) {
        return convertToIntegerOrNull(getCellValue(row, col));
    }

    private Integer convertToIntegerOrNull(String value) {
        try {
            if (value.equals(EMPTY)) {
                return null;
            }
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return null;
        }
    }

    protected Double getDoubleValue(int row, int col) {
        val cellValue = getCellValue(row, col);
        return cellValue.equals(EMPTY) ? null : Double.parseDouble(cellValue);
    }

    protected ConvertedBook createDefaultBook(String bookName,
                                              String excelListName
            , List<List<String>> excelListContent, String message) {
        return createConvertedBook(bookName, List.of(createConvertedList(excelListName, excelListContent)), message);
    }

    protected ConvertedBook createConvertedBook(String bookName, List<ConvertedList> book, String message) {
        return ConvertedBook.init()
                .setBookName(bookName)
                .setBook(book)
                .setMessage(message)
                .build();
    }

    protected ConvertedList createConvertedList(String excelListName, List<List<String>> excelListContent) {
        return ConvertedList.init()
                .setExcelListName(excelListName)
                .setExcelListContent(excelListContent)
                .build();
    }


    //    private String getValueOrDefault(int row, int slippage, int col) {
//        row = row + slippage;
//        if (row < START_ROW || row > LAST_ROW) {
//            return EMPTY;
//        }
//        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
//            return EMPTY;
//        }
//        return getCellValue(sheet.getRow(row).getCell(col));
//    }
}
