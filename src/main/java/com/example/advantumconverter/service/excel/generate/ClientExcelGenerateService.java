package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.exception.ExcelGenerationException;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import lombok.val;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;


@Component(CLIENT)
public class ClientExcelGenerateService implements ExcelGenerateService {
    private Workbook workbook;
    private List<List<String>> data;

    private CellStyle styleDateDot;
    private CellStyle styleDateTimeDot;
    private CellStyle styleInt;
    private CellStyle styleDouble;

    public InputFile createXlsx(ConvertedBook convertedBook) {
        val book = convertedBook.getBook().get(0);
        this.data = book.getExcelListContent();
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(book.getExcelListName());
        styleDateDot = getStyle(TEMPLATE_DATE_DOT);
        styleDateTimeDot = getStyle(TEMPLATE_DATE_TIME_DOT);
        styleInt = getStyle("0");
        styleDouble = getStyle("0,000000");
        int y = 0;
        int x = 0;
        try {
            for (; y < data.size(); y++) {
                val row = sheet.createRow(y);
                if (y == 0) {
                    for (x = 0; x < data.get(y).size(); x++) {
                        val cell = row.createCell(x);
                        cell.setCellValue(data.get(y).get(x));
                    }
                } else {
                    createCellString(row, y, 0);
                    createCellDate(row, y, 1, TEMPLATE_DATE_DOT, styleDateDot);
                    createCellString(row, y, 2);
                    createCellString(row, y, 3);
                    createCellInt(row, y, 4);
                    createCellString(row, y, 5);
                    createCellString(row, y, 6);
                    createCellString(row, y, 7);
                    createCellInt(row, y, 8);
                    createCellInt(row, y, 9);
                    createCellInt(row, y, 10);
                    createCellInt(row, y, 11);
                    createCellInt(row, y, 12);
                    createCellInt(row, y, 13);
                    createCellInt(row, y, 14);
                    createCellInt(row, y, 15);
                    createCellInt(row, y, 16);
                    createCellInt(row, y, 17);
                    createCellDate(row, y, 18, TEMPLATE_DATE_TIME_DOT, styleDateTimeDot);
                    createCellDate(row, y, 19, TEMPLATE_DATE_TIME_DOT, styleDateTimeDot);
                    createCellString(row, y, 20);
                    createCellString(row, y, 21);
                    createCellString(row, y, 22);
                    createCellInt(row, y, 23);
                    createCellDouble(row, y, 24);
                    createCellDouble(row, y, 25);
                    createCellString(row, y, 26);
                    createCellString(row, y, 27);
                    createCellString(row, y, 28);
                    createCellString(row, y, 29);
                    createCellString(row, y, 30);
                    createCellInt(row, y, 31);
                    createCellString(row, y, 32);
                    createCellString(row, y, 33);
                }
            }
            val tmpFile = Files.createTempFile(convertedBook.getBookName(), ".xlsx").toFile();
            workbook.write(new FileOutputStream(tmpFile));
            workbook.close();
            return new InputFile(tmpFile);
        } catch (Exception e) {
            throw new ExcelGenerationException("Строка:" + y + ", " + "Столбец:" + x + ". " + e.getMessage());
        }
    }

    private CellStyle getStyle(String format) {
        val style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(format));
        return style;
    }

    private void createCellInt(Row row, int y, int x) {
        val cell = row.createCell(x);
        cell.setCellStyle(styleInt);
        if (!data.get(y).get(x).equals(EMPTY)) {
            cell.setCellValue(Integer.parseInt(data.get(y).get(x)));
        }
    }

    private void createCellString(Row row, int y, int x) {
        row.createCell(x).setCellValue(data.get(y).get(x));
    }

    private void createCellDate(Row row, int y, int x, String dateFormat, CellStyle cellStyle) throws ParseException {
        val cell1 = row.createCell(x);
        cell1.setCellValue(convertDateFormat(data.get(y).get(x), dateFormat));
        cell1.setCellStyle(cellStyle);

    }

    private void createCellDouble(Row row, int y, int x) throws ParseException {
        val cell = row.createCell(x);
        cell.setCellStyle(styleDouble);
        if (!data.get(y).get(x).equals(EMPTY)) {
            cell.setCellValue(Double.parseDouble(data.get(y).get(x)));
        }
    }
}
