package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.exception.ExcelGenerationException;
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

import static com.example.advantumconverter.constant.Constant.ExcelType.BOOKER;
import static com.example.advantumconverter.constant.Constant.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;


@Component(BOOKER)
public class BookerExcelGenerateService implements ExcelGenerateService {
    private Workbook workbook;
    private List<List<String>> data;

    private CellStyle styleDateDot;
    private CellStyle styleDateTimeDot;
    private CellStyle styleInt;

    public InputFile createXlsx(List<List<String>> dataIn, String fileNamePrefix, String sheetName) {
        this.data = dataIn;
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        styleDateDot = getStyle(TEMPLATE_DATE_DOT);
        styleDateTimeDot = getStyle(TEMPLATE_DATE_TIME_DOT);
        styleInt = getStyle("0");
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
                    createCellInt(row, y, 1);
                    createCellInt(row, y, 2);
                }
            }
            val tmpFile = Files.createTempFile(fileNamePrefix, ".xlsx").toFile();
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
}
