package com.example.advantumconverter.service.rest.out.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CrmException extends RuntimeException  {

    private final String message;
    private final HttpStatusCode httpStatusCode;

    public CrmException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
