package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.ConvertedList;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookerExcelGenerateServiceTest {

    private final BookerExcelGenerateService service = new BookerExcelGenerateService();

    @Test
    void createXlsx_success() {
        var header = new ArrayList<>(List.of("c0", "c1", "c2"));
        var data = new ArrayList<>(List.of("A", "1", "2"));
        var emptyInt = new ArrayList<>(List.of("B", "", ""));
        var list = ConvertedList.init()
                .setExcelListName("Sheet1")
                .setExcelListContent(List.of(header, data, emptyInt))
                .build();
        var book = ConvertedBook.init().setBookName("Booker").setBook(List.of(list)).build();
        assertThat(service.createXlsx(book)).isNotNull();
    }

    @Test
    void createXlsxV2_notImplemented() {
        assertThatThrownBy(() -> service.createXlsxV2(ConvertedBookV2.init().build()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createXlsx_badIntegerValue() {
        var header = new ArrayList<>(List.of("c0", "c1", "c2"));
        var badData = new ArrayList<>(List.of("B", "not-a-number", ""));
        var list = ConvertedList.init()
                .setExcelListName("Sheet1")
                .setExcelListContent(List.of(header, badData))
                .build();
        var book = ConvertedBook.init().setBookName("Booker").setBook(List.of(list)).build();
        assertThatThrownBy(() -> service.createXlsx(book))
                .isInstanceOf(com.example.advantumconverter.exception.ExcelGenerationException.class);
    }
}
