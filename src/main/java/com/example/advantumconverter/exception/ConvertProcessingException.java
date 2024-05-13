package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exception.CONVERT_PROCESSING_ERROR;

public class ConvertProcessingException extends RuntimeException {
    public ConvertProcessingException(String message) {
        super(CONVERT_PROCESSING_ERROR + message);
    }

    public static ConvertProcessingException of(String format, Object... args) {
        return new ConvertProcessingException(String.format(format, args));
    }
}
