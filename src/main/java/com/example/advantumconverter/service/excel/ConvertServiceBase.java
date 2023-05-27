package com.example.advantumconverter.service.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;


@Component
public class ConvertServiceBase {

    protected XSSFSheet sheet;

    protected int LAST_COLUMN_NUMBER;
    public static String SHEET_RESULT_NAME = "ИМПОРТ";
    @Autowired
    protected PrepareService prepareService;


//    public InputFile process(XSSFWorkbook book) {
//        return excelGenerateService.process(getConvertedBook(book), SHEET_RESULT_NAME);
//    }

}
