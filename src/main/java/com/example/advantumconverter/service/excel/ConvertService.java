package com.example.advantumconverter.service.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ConvertService {
    List<List<String>> getConvertedBook(XSSFWorkbook book);
}
