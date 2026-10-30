package com.example.advantumconverter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExcelType {

    CLIENT("Client"),
    BOOKER("Booker"),
    RS("Rs"),
    RS_LENTA_SPB("RsLentaSpb"),
    RS_LENTA_SPB_NEW("RsLentaSpbNew");

    private final String excelType;

}
