package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.DICTIONARY_ERROR;

public class DictionaryException extends RuntimeException {
    public DictionaryException(String message) {
        super(DICTIONARY_ERROR + message);
    }
}
