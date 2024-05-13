package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ConvertService {
    String getConverterName();

    String getConverterCommand();

    ConvertedBook getConvertedBook(XSSFWorkbook book, String fileNamePrefix);

    String getFileNamePrefix();

    String getExcelType();
}
