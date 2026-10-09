package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplKznFatOilTest {

    private final ConvertServiceImplKznFatOil service = new ConvertServiceImplKznFatOil();

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

    private List<String[]> sheet(String firstDate) {
        return List.of(
                row(0, "header"),
                row(0, "x", 1, firstDate, 5, "addr1", 8, "01/15/25 10:00", 9, "01/15/25 12:00", 12, "U1", 14, "V1", 15, "A 1", 16, "AE1", 17, "B 2"),
                row(0, "x", 1, "15.01.2025", 5, "addr2", 8, "01/15/25 13:00", 9, "01/15/25 14:00", 12, "U2", 14, "V2", 15, "C 3", 16, "AE2", 17, "D 4"),
                row(0, "y", 1, "15.01.2025", 5, "addr3", 8, "01/15/25 15:00", 9, "01/15/25 16:00", 12, "U3", 14, "V3", 15, "E 5", 16, "AE3", 17, "F 6"),
                row(0, ""),
                row(0, ""));
    }

    @Test
    void getConvertedBookV2_success() {
        var result = service.getConvertedBookV2(build("Лист1", sheet("15.01.2025")));
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).hasSize(3);
    }

    @Test
    void getConvertedBookV2_wrapsException() {
        assertThatThrownBy(() -> service.getConvertedBookV2(build("Лист1", sheet("bad"))))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.isV2()).isTrue();
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
    }
}
