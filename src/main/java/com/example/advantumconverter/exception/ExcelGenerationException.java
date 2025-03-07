package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_GENERATION_ERROR;

public class ExcelGenerationException extends RuntimeException {
    public ExcelGenerationException(String message) {
        super(EXCEL_GENERATION_ERROR + message);
    }
}
