package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

public interface ExcelGenerateService {

    InputFile createXlsx(ConvertedBook convertedBook);
}
