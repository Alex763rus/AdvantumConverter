package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.exception.ExcelGenerationException;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.ConvertedList;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import lombok.val;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.ExcelType.BOOKER;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;


@Component(BOOKER)
public class BookerExcelGenerateService implements ExcelGenerateService {

    private CellStyle styleInt;

    private void createXlsxList(XSSFSheet sheet, List<List<String>> data) {
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
                    createCellString(data, row, y, 0);
                    createCellInt(data, row, y, 1);
                    createCellInt(data, row, y, 2);
                }
            }
        } catch (Exception e) {
            throw new ExcelGenerationException("Строка:" + y + ", " + "Столбец:" + x + ". " + e.getMessage());
        }
    }

    @Override
    public InputFile createXlsx(ConvertedBook convertedBook) {
        val workbook = new XSSFWorkbook();
        styleInt = getStyle(workbook, "0");
        try {
            for (ConvertedList book : convertedBook.getBook()) {
                val sheet = workbook.createSheet(book.getExcelListName());
                val data = book.getExcelListContent();
                createXlsxList(sheet, data);
            }
            val tmpFile = Files.createTempFile(convertedBook.getBookName(), ".xlsx").toFile();
            workbook.write(new FileOutputStream(tmpFile));
            workbook.close();
            return new InputFile(tmpFile);
        } catch (Exception e) {
            throw new ExcelGenerationException("Ошибка во время формирования файла:" + e.getMessage());
        }
    }

    @Override
    public InputFile createXlsxV2(ConvertedBookV2 convertedBook) {
        throw new RuntimeException("Не реализовано. Требуется реализация");
    }


    private CellStyle getStyle(Workbook workbook, String format) {
        val style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(format));
        return style;
    }

    private void createCellInt(List<List<String>> data, Row row, int y, int x) {
        val cell = row.createCell(x);
        cell.setCellStyle(styleInt);
        if (!data.get(y).get(x).equals(EMPTY)) {
            cell.setCellValue(Integer.parseInt(data.get(y).get(x)));
        }
    }

    private void createCellString(List<List<String>> data, Row row, int y, int x) {
        row.createCell(x).setCellValue(data.get(y).get(x));
    }

}
