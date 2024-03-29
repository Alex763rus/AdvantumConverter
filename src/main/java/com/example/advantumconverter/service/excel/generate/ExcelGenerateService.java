package com.example.advantumconverter.service.excel.generate;

import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

public interface ExcelGenerateService {

    InputFile createXlsx(List<List<String>> dataIn, String fileNamePrefix, String sheetName);
}
