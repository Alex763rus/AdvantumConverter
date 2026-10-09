package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.service.database.DictionaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Converter.LEFT_FOR_A_FLIGHT;
import static com.example.advantumconverter.constant.Constant.Heap.DONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static utils.WorkbookBuilder.build;

@ExtendWith(MockitoExtension.class)
class ConvertServiceImplMetroTest {

    @Mock
    private DictionaryService dictionaryService;

    private ConvertServiceImplMetro converter;

    @BeforeEach
    void setUp() {
        converter = new ConvertServiceImplMetro();
        ReflectionTestUtils.setField(converter, "dictionaryService", dictionaryService);
        converter.init();
    }

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

    private XSSFWorkbook happyWorkbook() {
        return build("Sheet1", List.of(
                row(0, "header"),
                row(0, "01/12/25", 10, "занят"),
                row(0, "01/12/25", 10, "занят"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, LEFT_FOR_A_FLIGHT,
                        11, "01/12/25", 12, "A Z1", 13, "В456", 14, "иван иванов", 17, "10:00",
                        22, "12:00", 24, "01/12/25", 27, "T1"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, "занят",
                        11, "01/12/25", 12, "А123", 13, "В456", 14, "петр петров",
                        24, "01/12/25", 27, "T1"),
                row(0, ""),
                row(0, "")
        ));
    }

    private void stubHappyDictionaries() {
        lenient().when(dictionaryService.getMetroMinTemperature("T1")).thenReturn(5L);
        lenient().when(dictionaryService.getMetroMaxTemperature("T1")).thenReturn(15L);
        lenient().when(dictionaryService.getMetroDcAddressBrief(eq("TC1"), anyString())).thenReturn("DC address");
        lenient().when(dictionaryService.getMetroDcAddressName(eq("TC1"), anyString())).thenReturn("DC name");
        lenient().when(dictionaryService.getMetroTimeDictionary("12345")).thenReturn("10:00");
        lenient().when(dictionaryService.getMetroTimeStart(eq(12345L), any())).thenReturn("10:00");
        lenient().when(dictionaryService.getMetroTimeEnd(eq(12345L), any())).thenReturn("08:00");
    }

    @Test
    void getConvertedBookV2_success() {
        stubHappyDictionaries();
        var result = converter.getConvertedBookV2(happyWorkbook());
        assertThat(result.getMessage()).startsWith(DONE);
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).hasSize(3);
    }

    @Test
    void getConvertedBookV2_emptyTemperatureBranch() {
        stubHappyDictionaries();
        var workbook = build("Sheet1", List.of(
                row(0, "header"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, LEFT_FOR_A_FLIGHT,
                        11, "01/12/25", 14, "иван иванов", 17, "10:00", 22, "12:00", 24, "01/12/25", 27, ""),
                row(0, ""),
                row(0, "")
        ));
        var result = converter.getConvertedBookV2(workbook);
        assertThat(result.getMessage()).startsWith(DONE);
    }

    @Test
    void getConvertedBookV2_temperatureNotFound() {
        stubHappyDictionaries();
        when(dictionaryService.getMetroMinTemperature("T2")).thenReturn(null);
        var workbook = build("Sheet1", List.of(
                row(0, "header"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, LEFT_FOR_A_FLIGHT,
                        11, "01/12/25", 14, "иван иванов", 17, "10:00", 22, "12:00", 24, "01/12/25", 27, "T2"),
                row(0, ""),
                row(0, "")
        ));
        assertThatThrownBy(() -> converter.getConvertedBookV2(workbook))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_addressNotFound() {
        stubHappyDictionaries();
        when(dictionaryService.getMetroTimeDictionary("12345")).thenReturn(null);
        var workbook = build("Sheet1", List.of(
                row(0, "header"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, LEFT_FOR_A_FLIGHT,
                        11, "01/12/25", 14, "иван иванов", 17, "10:00", 22, "12:00", 24, "01/12/25", 27, "T1"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, "занят",
                        11, "01/12/25", 14, "петр петров", 24, "01/12/25", 27, "T1"),
                row(0, ""),
                row(0, "")
        ));
        assertThatThrownBy(() -> converter.getConvertedBookV2(workbook))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_timeStartNotFound() {
        stubHappyDictionaries();
        when(dictionaryService.getMetroTimeStart(eq(12345L), any())).thenReturn(null);
        var workbook = build("Sheet1", List.of(
                row(0, "header"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, LEFT_FOR_A_FLIGHT,
                        11, "01/12/25", 14, "иван иванов", 17, "10:00", 22, "12:00", 24, "01/12/25", 27, "T1"),
                row(0, "01/12/25", 1, "AB1234CD", 2, "12345", 4, "TC1", 10, "занят",
                        11, "01/12/25", 14, "петр петров", 24, "01/12/25", 27, "T1"),
                row(0, ""),
                row(0, "")
        ));
        assertThatThrownBy(() -> converter.getConvertedBookV2(workbook))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getIdAndType() {
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.CLIENT);
        assertThat(converter.isV2()).isTrue();
        assertThat(converter.getConverterName()).isNotBlank();
        assertThat(converter.getConverterCommand()).isNotBlank();
    }
}
