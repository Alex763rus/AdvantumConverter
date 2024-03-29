package com.example.advantumconverter.service.excel.converter.booker;

import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface BookerListService {

    List<BookerInputData> getConvertedList(XSSFWorkbook book, String listName);
}
