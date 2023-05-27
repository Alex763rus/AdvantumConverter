package com.example.advantumconverter.service.excel;

import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ConvertService {

    @Autowired
    private PrepareService prepareService;

    @Autowired
    private ExcelGenerateService excelGenerateService;

    @Autowired
    private ConvertServiceFile1 convertServiceFile1;
    String sheetName = "ИМПОРТ";

    public InputFile process(XSSFWorkbook book) {
        prepareService.process(book);
        return excelGenerateService.process(convertServiceFile1.process(book), sheetName);
    }
}
