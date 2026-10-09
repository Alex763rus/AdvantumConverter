package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.addSheet;

class BookerListServiceAshanTest {

    private final BookerListServiceAshan service = new BookerListServiceAshan();

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

    private XSSFWorkbook workbook(String sheet1, String sheet2) {
        var wb = new XSSFWorkbook();
        if (sheet1 != null) {
            addSheet(wb, "ashan1", List.of(
                    row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                    row(0, "x", 1, "cp", 2, "7706644017", 6, "A123", 9, "100", 16, "1", 17, "2", 18, "3", 19, "4", 20, "5"),
                    row(0, "x", 2, "0", 6, "B"),
                    row(0, "x", 2, "7810071482", 6, "C"),
                    row(0, "x", 2, "", 6, "D"),
                    row(0, "x", 2, "999", 6, ""),
                    row(0, "x", 2, "888", 6, "E", 9, ""),
                    row(0, ""),
                    row(0, "")));
        }
        if (sheet2 != null) {
            addSheet(wb, "ashan2", List.of(
                    row(0, "header"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                    row(0, "x", 6, "7810071482", 7, "CAR1"),
                    row(0, "x", 6, "7810071482", 7, "CAR1"),
                    row(0, "x", 6, "UNKNOWN", 7, "CAR2"),
                    row(0, "x", 6, "", 7, "CAR3"),
                    row(0, "x", 6, "некий текст", 7, "CAR4"),
                    row(0, ""),
                    row(0, "")));
        }
        return wb;
    }

    @Test
    void getConvertedList_success() {
        var result = service.getConvertedList(workbook("s1", "s2"), "Ashan");
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getInn()).isEqualTo("7717655878");
    }

    @Test
    void getConvertedList_missingSheet() {
        assertThatThrownBy(() -> service.getConvertedList(new XSSFWorkbook(), "Ashan"))
                .isInstanceOf(ExcelListNotFoundException.class);
    }

    @Test
    void getConvertedList_missingRow_convertError() {
        var wb = new XSSFWorkbook();
        addSheet(wb, "ashan1", List.<String[]>of(row(0, "only-header")));
        assertThatThrownBy(() -> service.getConvertedList(wb, "Ashan"))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedList_emptyGpsRow_skipped() {
        var wb = new XSSFWorkbook();
        String tsNoSetup = (String) ReflectionTestUtils.getField(service, "TS_NO_SETUP");
        addSheet(wb, "ashan1", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 6, ""), row(0, ""), row(0, "")));
        addSheet(wb, "ashan2", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 6, "", 7, "CAR"),
                row(0, "x", 6, tsNoSetup, 7, "CAR2"),
                row(0, ""), row(0, "")));

        assertThat(service.getConvertedList(wb, "Ashan")).isEmpty();
    }

    @Test
    void getConvertedList_secondSheetMissingRow_convertError() {
        var wb = new XSSFWorkbook();
        addSheet(wb, "ashan1", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 1, "cp", 2, "7706644017", 6, "A123", 9, "100"),
                row(0, ""), row(0, "")));
        addSheet(wb, "ashan2", List.<String[]>of(row(0, "only-header")));

        assertThatThrownBy(() -> service.getConvertedList(wb, "Ashan"))
                .isInstanceOf(ConvertProcessingException.class);
    }
}
