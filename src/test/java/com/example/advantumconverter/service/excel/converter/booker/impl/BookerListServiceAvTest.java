package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class BookerListServiceAvTest {

    private final BookerListServiceAv service = new BookerListServiceAv();

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
        var wb = build("av", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 1, "A1", 2, "cp1", 3, "7706644017"),
                row(0, "x", 1, "A1", 2, "cp1", 3, "7706644017"),
                row(0, "x", 1, "", 3, "111"),
                row(0, "x", 1, "0", 3, "111"),
                row(0, "x", 1, "B", 3, ""),
                row(0, "x", 1, "C", 3, "0"),
                row(0, "x", 1, "D", 2, "cp2", 3, "999"),
                row(0, ""),
                row(0, "")));
        var result = service.getConvertedList(wb, "av");
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getInn()).isEqualTo("7717655878");
        assertThat(result.get(1).getInn()).isEqualTo("999");
    }

    @Test
    void getConvertedList_missingSheet() {
        assertThatThrownBy(() -> service.getConvertedList(new XSSFWorkbook(), "av"))
                .isInstanceOf(ExcelListNotFoundException.class);
    }

    @Test
    void getConvertedList_missingStartRow_convertError() {
        var wb = build("av", List.<String[]>of(row(0, "only-header")));

        assertThatThrownBy(() -> service.getConvertedList(wb, "av"))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }
}
