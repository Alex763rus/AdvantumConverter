package com.example.advantumconverter.exception;

import static com.example.advantumconverter.constant.Constant.Exceptions.SBER_ADDRESS_NOT_FOUND;

public class SberAddressNotFoundException extends RuntimeException {
    public SberAddressNotFoundException() {
        super(SBER_ADDRESS_NOT_FOUND);
    }
}
