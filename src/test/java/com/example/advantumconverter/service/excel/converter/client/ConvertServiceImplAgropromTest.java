package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplAgropromTest {

    private final ConvertServiceImplAgroprom service = new ConvertServiceImplAgroprom();

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

    private List<String[]> sheet(String timeStart) {
        return List.of(
                row(0, "header"),
                row(0, "A1", 2, "B1", 3, "C1", 5, "5v", 6, "6v", 8, "8v", 9, "01/15/25", 10, timeStart, 12, "01/15/25", 13, "14:00"),
                row(0, ""),
                row(0, ""));
    }

    @Test
    void getConvertedBook_success() {
        var result = service.getConvertedBook(build("Лист1", sheet("10:00")));
        // header + 2 repeats
        assertThat(result.getBook().get(0).getExcelListContent()).hasSize(3);
    }

    @Test
    void getConvertedBook_wrapsException() {
        assertThatThrownBy(() -> service.getConvertedBook(build("Лист1", sheet("bad"))))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
    }
}
