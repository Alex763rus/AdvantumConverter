package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.addSheet;

class BookerListServiceMetroTest {

    private final BookerListServiceMetro service = new BookerListServiceMetro();

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

    private XSSFWorkbook workbook() {
        var wb = new XSSFWorkbook();
        addSheet(wb, "metro1", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 2, "ДЖИИКСО ЛОДЖИСТИКС", 3, "C1", 5, "OWNER1", 7, "777"),
                row(0, "x", 2, "ДЖИИКСО ЛОДЖИСТИКС", 3, "C1", 5, "OWNER1", 7, "777"),
                row(0, "x", 2, "OTHER", 3, "C2", 5, "own", 6, "888"),
                row(0, "x", 2, "OTHER", 3, "", 6, "888"),
                row(0, "x", 2, "OTHER", 3, "0", 6, "888"),
                row(0, "x", 2, "OTHER", 3, "C3", 6, ""),
                row(0, "x", 2, "OTHER", 3, "C4", 6, "0"),
                row(0, ""),
                row(0, "")));
        addSheet(wb, "metro2", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 2, "ФМ ВОСТОК", 3, "C5", 5, "ФМ ВОСТОК", 6, "999"),
                row(0, ""),
                row(0, "")));
        return wb;
    }

    @Test
    void getConvertedList_success() {
        var result = service.getConvertedList(workbook(), "metro");
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getInn()).isEqualTo("777");
        assertThat(result.get(2).getInn()).isEqualTo("999");
    }

    @Test
    void getConvertedList_missingSheet() {
        assertThatThrownBy(() -> service.getConvertedList(new XSSFWorkbook(), "metro"))
                .isInstanceOf(ExcelListNotFoundException.class);
    }

    @Test
    void convert_missingStartRow_convertError() {
        var wb = new XSSFWorkbook();
        addSheet(wb, "metro1", List.<String[]>of(row(0, "only-header")));

        assertThatThrownBy(() -> service.convert(wb, "metro1"))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }
}
