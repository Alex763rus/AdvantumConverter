package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_VALIDATION_ERROR;

public class ExcelValidationException extends RuntimeException {
    public ExcelValidationException(int row, int col) {
        super(String.format(EXCEL_VALIDATION_ERROR, row, col));
    }
}
