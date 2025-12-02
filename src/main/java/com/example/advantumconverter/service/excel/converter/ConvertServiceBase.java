package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.config.properties.ConverterProperties;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ResultCode;
import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.ConvertedList;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.database.DictionaryService;
import lombok.val;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ResultCode.OK;
import static com.example.advantumconverter.enums.ResultCode.WARNING;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;


@Component
public class ConvertServiceBase {

    @Autowired
    protected ConverterProperties converterProperties;

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

    protected String getCellValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return EMPTY;
        }
        var cell = sheet.getRow(row).getCell(col);
        if (cell == null) {
            return EMPTY;
        }
        return getCellValue(cell);
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

    protected String getFormulaCellValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return EMPTY;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return EMPTY;
        }
        var xssfCell = sheet.getRow(row).getCell(col);

        DataFormatter formatter = new DataFormatter();
        // Если ячейка содержит формулу, вычисляем ее значение
        if (xssfCell != null && xssfCell.getCellType() == CELL_TYPE_FORMULA) {
            Workbook workbook = xssfCell.getSheet().getWorkbook();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Вычисляем значение формулы
            CellValue cellValue = evaluator.evaluate(xssfCell);

            int i = 0;
            // Форматируем вычисленное значение
            return switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC -> String.valueOf(cellValue.getNumberValue());
                case Cell.CELL_TYPE_STRING -> cellValue.getStringValue();
                case Cell.CELL_TYPE_BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                case Cell.CELL_TYPE_ERROR -> "ERROR";
                default -> "";
            };
        } else {
            // Для обычных ячеек используем стандартный форматтер
            return formatter.formatCellValue(xssfCell);
        }
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        // Если ячейка содержит формулу, вычисляем ее значение
        if (xssfCell != null && xssfCell.getCellType() == CELL_TYPE_FORMULA) {
            Workbook workbook = xssfCell.getSheet().getWorkbook();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Вычисляем значение формулы
            CellValue cellValue = evaluator.evaluate(xssfCell);

            int i = 0;
            // Форматируем вычисленное значение
            return switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC -> String.valueOf(cellValue.getNumberValue());
                case Cell.CELL_TYPE_STRING -> cellValue.getStringValue();
                case Cell.CELL_TYPE_BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                case Cell.CELL_TYPE_ERROR -> "ERROR";
                default -> "";
            };
        } else {
            // Для обычных ячеек используем стандартный форматтер
            return formatter.formatCellValue(xssfCell);
        }
    }

    protected Date getCellDate(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return null;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return null;
        }
        return sheet.getRow(row).getCell(col).getDateCellValue();
    }

    protected Date getCellDate(int row, int col) {
        return getCellDate(sheet, row, col);
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

    protected Integer getIntegerValue(XSSFSheet sheet, int row, int col, int defaultValue) {
        return Optional.ofNullable(convertToIntegerOrNull(getCellValue(sheet, row, col)))
                .orElse(defaultValue);
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

    protected Double getDoubleValue(XSSFSheet sheet, int row, int col) {
        val cellValue = getCellValue(sheet, row, col).replace(",", ".");
        return cellValue.equals(EMPTY) ? null : Double.parseDouble(cellValue);
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

    protected ConvertedBookV2 createDefaultBookV2(List<ConvertedListDataV2> data, List<String> warnings, String listName, List<String> header,
                                                  String excelListName) {

        String message = DONE;
        ResultCode resultCode = OK;
        if (!CollectionUtils.isEmpty(warnings)) {
            message = message + "Возникшие предупреждения: " + NEW_LINE +
                    warnings.stream().distinct().collect(Collectors.joining(EMPTY));
            resultCode = WARNING;
        }
        return ConvertedBookV2.init()
                .setBookV2(List.of(
                        ConvertedListV2.init()
                                .setHeadersV2(header)
                                .setExcelListName(excelListName)
                                .setExcelListContentV2(data)
                                .build()
                ))
                .setMessage(message)
                .setBookName(listName + UNDERSCORE)
                .setResultCode(resultCode)
                .build();
    }

    protected ConvertedBookV2 createDefaultBookV2(List<ConvertedListDataV2> data, List<String> warnings, String listName) {
        return createDefaultBookV2(data, warnings, listName, Header.headersOutputClientV2, EXPORT);
    }
}
