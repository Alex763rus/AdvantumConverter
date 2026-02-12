package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.exception.ExcelGenerationException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsLentaSpbV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import lombok.val;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.tgcommons.constant.Constant;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.ExcelType.RS_LENTA_SPB;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;


@Component(RS_LENTA_SPB)
public class RsLentaSpbExcelGenerateService implements ExcelGenerateService {
    private Workbook workbook;
    private List<List<String>> data;
    private CellStyle styleDateDot;
    private CellStyle styleDateTimeDot;
    private CellStyle styleInt;
    private CellStyle styleDouble;
    private CellStyle styleGeneral;
    private CellStyle styleTime;

    private void createCellString(Row row, int y, int x) {
        var value = data.get(y).size() > x ? data.get(y).get(x) : EMPTY;
        row.createCell(x).setCellValue(value);
    }

    private void createCellDate(Row row, int y, int x, String dateFormat, CellStyle cellStyle) throws ParseException {
        val cell1 = row.createCell(x);
        cell1.setCellValue(convertDateFormat(data.get(y).get(x), dateFormat));
        cell1.setCellStyle(cellStyle);

    }

    private void createCellInt(Row row, int y, int x) {
        val cell = row.createCell(x);
        cell.setCellStyle(styleInt);
        if (!data.get(y).get(x).equals(Constant.TextConstants.EMPTY)) {
            cell.setCellValue(Integer.parseInt(data.get(y).get(x)));
        }
    }

    private void createCellDouble(Row row, int y, int x) throws ParseException {
        val cell = row.createCell(x);
        cell.setCellStyle(styleDouble);
        if (!data.get(y).get(x).equals(Constant.TextConstants.EMPTY)) {
            cell.setCellValue(Double.parseDouble(data.get(y).get(x)));
        }
    }

    @Override
    @LogExecutionTime(value = "Генерация файла результата v2  lenta", unit = LogExecutionTime.TimeUnit.SECONDS)
    public InputFile createXlsxV2(ConvertedBookV2 convertedBook) {
        val book = convertedBook.getBookV2().get(0);
        List<ConvertedListDataV2> dataV2 = book.getExcelListContentV2();
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(book.getExcelListName());
        styleDateDot = getStyle(TEMPLATE_DATE_DOT);
        styleDateTimeDot = getStyle(TEMPLATE_DATE_TIME_DOT);
        styleInt = getStyle("0");
        styleDouble = getStyle("0,000000");
        styleGeneral = getStyle("general");
        styleTime = getStyle(TEMPLATE_TIME_SECOND);
        int y = 0;
        int x = 0;
        int dataCounter = 0;
        try {
            Row row = sheet.createRow(y);
            for (x = 0; x < book.getHeadersV2().size(); x++) {
                val cell = row.createCell(x);
                cell.setCellValue(book.getHeadersV2().get(x));
            }

            for (ConvertedListDataV2 convertedListDataV2 : dataV2) {
                ConvertedListDataRsLentaSpbV2 rowData = (ConvertedListDataRsLentaSpbV2) convertedListDataV2;

                for (int repeat = 0; repeat < rowData.getTechCountRepeat(); ++repeat) {
                    row = sheet.createRow(y + 1);
                    createCell(row, 0, convertDateFormat(rowData.getColumnBdata(), TEMPLATE_DATE) + "_"
                            + rowData.getColumnDdata() + "_" + (repeat + 1));
                    createCell(row, 1, styleDateDot, rowData.getColumnBdata());
                    createCell(row, 2, rowData.getColumnCdata());
                    createCell(row, 3, rowData.getColumnDdata());
                    createCell(row, 4, rowData.getColumnEdata());
                    createCell(row, 5, rowData.getColumnFdata());
                    createCell(row, 6, rowData.getColumnGdata());
                    createCell(row, 7, rowData.getColumnHdata());
                    createCell(row, 8, rowData.getColumnIdata());
                    createCell(row, 9, rowData.getColumnJdata());
                    createCell(row, 10, rowData.getColumnKdata());
                    createCell(row, 11, rowData.getColumnLdata());
                    createCell(row, 12, rowData.getColumnMdata());
                    createCell(row, 13, rowData.getColumnNdata());
                    createCell(row, 14, rowData.getColumnOdata());
                    createCell(row, 15, rowData.getColumnPdata());
                    createCell(row, 16, rowData.getColumnRdata());
                    createCell(row, 17, rowData.getColumnSdata());
                    y = y + 1;
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

    private void createCell(Row row, int x, Integer value) {
        val cell = row.createCell(x);
        cell.setCellStyle(styleInt);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    private void createCell(Row row, int x, String value) {
        row.createCell(x).setCellValue(value != null ? value : EMPTY);
    }

    private void createCell(Row row, int x, CellStyle cellStyle, Date value) throws ParseException {
        val cell1 = row.createCell(x);
        cell1.setCellStyle(cellStyle);
        if (value != null) {
            cell1.setCellValue(value);
        }
    }

    private void createGeneralCell(Row row, int x, Double value) throws ParseException {
        val cell = row.createCell(x);
        cell.setCellStyle(styleGeneral);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    private void createCell(Row row, int x, Double value) throws ParseException {
        val cell = row.createCell(x);
        cell.setCellStyle(styleDouble);
        if (value != null) {
            cell.setCellValue(value);
        }
    }
}
