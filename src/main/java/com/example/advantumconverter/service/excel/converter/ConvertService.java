package com.example.advantumconverter.service.excel.converter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ConvertService {
    String getConverterName();

    String getConverterCommand();

    List<List<String>> getConvertedBook(XSSFWorkbook book);
    String getFileNamePrefix();
}
