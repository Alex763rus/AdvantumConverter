package com.example.advantumconverter.service.excel;

import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelGenerateService {

    public InputFile process(List<List<String>> data, String sheetName) {
        File tmpFile;
        try {
            tmpFile = Files.createTempFile("exel", ".xls").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        val book = new HSSFWorkbook();
        val sheet = book.createSheet(sheetName);
        for (int y = 0; y < data.size(); y++) {
            Row row = sheet.createRow(y);
            for (int x = 0; x < data.get(y).size(); x++) {
                row.createCell(x).setCellValue(data.get(y).get(x));
            }
        }
        for (int x = 0; x < data.get(0).size(); x++) {
            sheet.autoSizeColumn(x);
        }

        try {
            book.write(new FileOutputStream(tmpFile));
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new InputFile(tmpFile);
    }

}
