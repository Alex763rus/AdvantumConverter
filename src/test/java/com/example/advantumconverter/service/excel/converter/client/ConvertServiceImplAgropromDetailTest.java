package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplAgropromDetailTest {

    private final ConvertServiceImplAgropromDetail service = new ConvertServiceImplAgropromDetail();

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

    private List<String[]> sheet(String firstDateTime) {
        return List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 1, "A1", 4, "B1", 6, "C1", 11, "D1", 12, "E1", 15, firstDate(), 18, "15.01.2025 12:00", 20, "1", 30, "F1", 31, "G1"),
                row(0, "y", 0, "y", 1, "A2", 4, "B2", 6, "C2", 11, "D2", 12, "E2", 15, "15.01.2025 11:00", 18, "15.01.2025 13:00", 20, "2", 30, "F2", 31, "G2"),
                row(0, ""),
                row(0, ""));
    }

    private static String firstDate() {
        return "15.01.2025 10:00";
    }

    @Test
    void getConvertedBook_success() {
        var result = service.getConvertedBook(build("Лист1", sheet("15.01.2025 10:00")));
        assertThat(result.getBook().get(0).getExcelListContent()).hasSize(3);
    }

    @Test
    void getConvertedBook_wrapsException() {
        assertThatThrownBy(() -> service.getConvertedBook(build("Лист1", List.of(
                row(0, "h"), row(0, "h"), row(0, "h"), row(0, "h"),
                row(0, "x", 15, "bad"),
                row(0, ""),
                row(0, "")))))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
    }
}
