package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

public interface ExcelGenerateService {

    @Deprecated
    default InputFile createXlsx(ConvertedBook convertedBook){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default InputFile createXlsxV2(ConvertedBookV2 convertedBook){
        throw new UnsupportedOperationException("Need implementation");
    }
}
