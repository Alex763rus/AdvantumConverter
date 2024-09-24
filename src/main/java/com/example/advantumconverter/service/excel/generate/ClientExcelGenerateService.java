package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.exception.ExcelGenerationException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
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
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.ExcelType.CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.TEMPLATE_DATE_DOT;
import static org.example.tgcommons.utils.DateConverterUtils.TEMPLATE_DATE_TIME_DOT;


@Component(CLIENT)
public class ClientExcelGenerateService implements ExcelGenerateService {
    private Workbook workbook;
    private List<ConvertedListDataV2> dataV2;

    private CellStyle styleDateDot;
    private CellStyle styleDateTimeDot;
    private CellStyle styleInt;
    private CellStyle styleDouble;

    @Override
    public InputFile createXlsxV2(ConvertedBookV2 convertedBook) {
        val book = convertedBook.getBookV2().get(0);
        this.dataV2 = book.getExcelListContentV2();
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(book.getExcelListName());
        styleDateDot = getStyle(TEMPLATE_DATE_DOT);
        styleDateTimeDot = getStyle(TEMPLATE_DATE_TIME_DOT);
        styleInt = getStyle("0");
        styleDouble = getStyle("0,000000");
        int y = 0;
        int x = 0;
        try {
            Row row = sheet.createRow(y);
            for (x = 0; x < book.getHeadersV2().size(); x++) {
                val cell = row.createCell(x);
                cell.setCellValue(book.getHeadersV2().get(x));
            }
            for (; y < dataV2.size(); y++) {
                row = sheet.createRow(y + 1);
                createCell(row, 0, dataV2.get(y).getColumnAdata());
                createCell(row, 1, styleDateDot, dataV2.get(y).getColumnBdata());
                createCell(row, 2, dataV2.get(y).getColumnCdata());
                createCell(row, 3, dataV2.get(y).getColumnDdata());
                createCell(row, 4, dataV2.get(y).getColumnEdata());
                createCell(row, 5, dataV2.get(y).getColumnFdata());
                createCell(row, 6, dataV2.get(y).getColumnGdata());
                createCell(row, 7, dataV2.get(y).getColumnHdata());
                createCell(row, 8, dataV2.get(y).getColumnIdata());
                createCell(row, 9, dataV2.get(y).getColumnJdata());
                createCell(row, 10, dataV2.get(y).getColumnKdata());
                createCell(row, 11, dataV2.get(y).getColumnLdata());
                createCell(row, 12, dataV2.get(y).getColumnMdata());
                createCell(row, 13, dataV2.get(y).getColumnNdata());
                createCell(row, 14, dataV2.get(y).getColumnOdata());
                createCell(row, 15, dataV2.get(y).getColumnPdata());
                createCell(row, 16, dataV2.get(y).getColumnQdata());
                createCell(row, 17, dataV2.get(y).getColumnRdata());
                createCell(row, 18, styleDateTimeDot, dataV2.get(y).getColumnSdata());
                createCell(row, 19, styleDateTimeDot, dataV2.get(y).getColumnTdata());
                createCell(row, 20, dataV2.get(y).getColumnUdata());
                createCell(row, 21, dataV2.get(y).getColumnVdata());
                createCell(row, 22, dataV2.get(y).getColumnWdata());
                createCell(row, 23, dataV2.get(y).getColumnXdata());
                createCell(row, 24, dataV2.get(y).getColumnYdata());
                createCell(row, 25, dataV2.get(y).getColumnZdata());
                createCell(row, 26, dataV2.get(y).getColumnAaData());
                createCell(row, 27, dataV2.get(y).getColumnAbData());
                createCell(row, 28, dataV2.get(y).getColumnAcData());
                createCell(row, 29, dataV2.get(y).getColumnAdData());
                createCell(row, 30, dataV2.get(y).getColumnAeData());
                createCell(row, 31, dataV2.get(y).getColumnAfData());
                createCell(row, 32, dataV2.get(y).getColumnAgData());
                createCell(row, 33, dataV2.get(y).getColumnAhData());
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

    private void createCell(Row row, int x, Double value) throws ParseException {
        val cell = row.createCell(x);
        cell.setCellStyle(styleDouble);
        if (value != null) {
            cell.setCellValue(value);
        }
    }
}
