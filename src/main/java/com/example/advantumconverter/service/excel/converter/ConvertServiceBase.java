package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;


@Component
public class ConvertServiceBase {

    @Autowired
    protected CrmConfigProperties crmConfigProperties;

    @Autowired
    protected DictionaryService dictionaryService;

    protected XSSFSheet sheet;
    protected int LAST_COLUMN_NUMBER;
    protected int LAST_ROW;
    private static final int MAX_ROW = 10000;
    protected static final String REGEX_NUMBER = "\\D+";
    protected static final String REGEX_NUMBER_AND_SIMBOL = "[^a-zA-Zа-яА-ЯёЁ0-9]";

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
        while (!(EMPTY.equals(getCellValue(i, 0)) && EMPTY.equals(getCellValue(i + 1, 0)))) {
            i = i + 1;
        }
        return i - 1;
    }

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

    protected Integer getIntegerValue(int row, int col, int defaultValue) {
        return Optional.ofNullable(convertToIntegerOrNull(getCellValue(row, col)))
                .orElse(defaultValue);
    }

    protected Integer convertToIntegerOrNull(String value) {
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
        val cellValue = getCellValue(row, col).replace(",", ".");
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

    protected Optional<Date> convertDate(int row, int col, List<String> templates) {
        var cellValue = getCellValue(row, col);
        for (String template : templates) {
            try {
                return Optional.of(convertDateFormat(cellValue, template));
            } catch (Exception ex) {
            }
        }
        return Optional.empty();
    }
}
