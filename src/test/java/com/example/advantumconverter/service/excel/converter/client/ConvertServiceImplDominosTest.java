package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.service.database.DictionaryService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplDominosTest {

    private final ConvertServiceImplDominos service = new ConvertServiceImplDominos();
    private final DictionaryService dictionaryService = mock(DictionaryService.class);

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

    private List<String[]> sheet() {
        return List.of(
                row(0, "header"),
                row(0, "01/15/25"),
                row(0, "x", 3, "K1", 4, "addr", 5, "10:00", 9, "CAR1", 12, "FIO_B", 13, "FIO_A"),
                row(0, "10:00", 3, "K1", 4, "addr", 5, "11:00", 9, "CAR1", 11, "V", 12, "FIO_B", 13, "FIO_A"),
                row(0, "x", 3, "K2", 9, "CAR1"),
                row(0, ""),
                row(0, ""));
    }

    @Test
    void getConvertedBook_success() {
        ReflectionTestUtils.setField(service, "dictionaryService", dictionaryService);
        var car = new Car();
        car.setTonnage(10);
        car.setPallet(20);
        when(dictionaryService.getCarOrElseThrow("CAR1")).thenReturn(car);
        var result = service.getConvertedBook(build("Лист1", sheet()));
        // header + 2 data lines
        assertThat(result.getBook().get(0).getExcelListContent()).hasSize(3);
    }

    @Test
    void getConvertedBook_wrapsException() {
        ReflectionTestUtils.setField(service, "dictionaryService", dictionaryService);
        when(dictionaryService.getCarOrElseThrow("CAR1")).thenThrow(new RuntimeException("no car"));
        assertThatThrownBy(() -> service.getConvertedBook(build("Лист1", sheet())))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
    }
}
