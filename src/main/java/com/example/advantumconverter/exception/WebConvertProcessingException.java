package com.example.advantumconverter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.advantumconverter.constant.Constant.Exceptions.CONVERT_PROCESSING_ERROR;

@Getter
public class WebConvertProcessingException extends RuntimeException {
    private HttpStatus httpStatus;

    public WebConvertProcessingException(HttpStatus httpStatus, String message) {
        super(CONVERT_PROCESSING_ERROR + message);
        this.httpStatus = httpStatus;
    }

}
