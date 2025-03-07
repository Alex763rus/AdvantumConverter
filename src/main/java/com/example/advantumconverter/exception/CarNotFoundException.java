package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.CAR_NOT_FOUND;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String message) {
        super(CAR_NOT_FOUND + message);
    }
}
