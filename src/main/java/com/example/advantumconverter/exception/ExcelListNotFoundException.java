package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LIST_NOT_FOUND_WARNING;

public class ExcelListNotFoundException extends RuntimeException {
    public ExcelListNotFoundException(String message) {
        super(String.format(EXCEL_LIST_NOT_FOUND_WARNING, message));
    }
}
