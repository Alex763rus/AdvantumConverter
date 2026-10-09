package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookerListServiceOzonTest {

    private final BookerListServiceOzon service = new BookerListServiceOzon();

    @Test
    void getConvertedList_notImplemented() {
        assertThatThrownBy(() -> service.getConvertedList(new XSSFWorkbook(), "Ozon"))
                .isInstanceOf(ConvertProcessingException.class);
    }
}
