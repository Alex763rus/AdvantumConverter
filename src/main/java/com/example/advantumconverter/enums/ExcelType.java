package com.example.advantumconverter.enums;

public enum ExcelType {

    CLIENT("Client"),
    BOOKER("Booker"),
    RS("Rs"),
    RS_INNER_LENTA("RsInnerLenta");

    private String excelType;

    ExcelType(String excelType) {
        this.excelType = excelType;
    }

    public String getExcelType() {
        return excelType;
    }

}
