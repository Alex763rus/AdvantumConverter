package com.example.advantumconverter.exception;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message, Throwable cause) {
        super(cause);
    }
}
