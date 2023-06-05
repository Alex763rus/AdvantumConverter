package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.enums.State;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ConvertService {
    String getConverterName();

    String getConverterCommand();

    List<List<String>> getConvertedBook(XSSFWorkbook book);
    String getFileNamePrefix();
}
