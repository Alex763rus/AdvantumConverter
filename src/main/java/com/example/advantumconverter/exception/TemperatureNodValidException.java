package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exception.TEMPERATURE_NOT_VALID;

public class TemperatureNodValidException extends RuntimeException {
    public TemperatureNodValidException(String rowNumber) {
        super(TEMPERATURE_NOT_VALID + rowNumber);
    }
}
