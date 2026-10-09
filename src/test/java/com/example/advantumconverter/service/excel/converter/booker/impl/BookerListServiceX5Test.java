package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class BookerListServiceX5Test {

    private final BookerListServiceX5 service = new BookerListServiceX5();

    private static String[] row(Object... kv) {
        int max = 0;
        for (int i = 0; i < kv.length; i += 2) {
            max = Math.max(max, (Integer) kv[i]);
        }
        String[] arr = new String[max + 1];
        Arrays.fill(arr, "");
        for (int i = 0; i < kv.length; i += 2) {
            arr[(Integer) kv[i]] = (String) kv[i + 1];
        }
        return arr;
    }

    @Test
    void getConvertedList_success() {
        var wb = build("X5", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 1, "cp", 2, "7706644017", 6, "A1", 9, "100", 10, "3", 11, "1", 17, "2", 18, "3", 19, "4", 20, "5", 21, "6"),
                row(0, "x", 1, "ТЕСТ", 2, "111", 6, "A2"),
                row(0, "x", 1, "cp", 2, "", 6, "A3"),
                row(0, "x", 1, "cp", 2, "0", 6, "A4"),
                row(0, "x", 1, "cp", 2, "111", 6, ""),
                row(0, "x", 1, "cp", 2, "111", 6, "0"),
                row(0, "x", 1, "cp", 2, "111", 6, "A5", 9, "", 10, ""),
                row(0, ""),
                row(0, "")));
        var result = service.getConvertedList(wb, "X5");
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getInn()).isEqualTo("7717655878");
        assertThat(result.get(1).getRate()).isEqualTo(500.0);
        assertThat(result.get(1).getRnic()).isZero();
    }

    @Test
    void getConvertedList_missingSheet() {
        assertThatThrownBy(() -> service.getConvertedList(new XSSFWorkbook(), "X5"))
                .isInstanceOf(ExcelListNotFoundException.class);
    }

    @Test
    void getConvertedList_missingStartRow_convertError() {
        var wb = build("X5", List.<String[]>of(row(0, "only-header")));

        assertThatThrownBy(() -> service.getConvertedList(wb, "X5"))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }
}
