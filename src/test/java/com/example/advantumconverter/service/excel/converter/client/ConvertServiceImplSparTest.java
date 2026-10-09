package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.model.jpa.spar.SparWindows;
import com.example.advantumconverter.service.database.DictionaryService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.WorkbookBuilder.build;

@ExtendWith(MockitoExtension.class)
class ConvertServiceImplSparTest {

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private CrmConfigProperties crmConfigProperties;

    private ConvertServiceImplSpar converter;

    @BeforeEach
    void setUp() {
        converter = new ConvertServiceImplSpar(crmConfigProperties);
        ReflectionTestUtils.setField(converter, "dictionaryService", dictionaryService);
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

    private SparWindows windows(String start, String end) {
        var w = new SparWindows();
        w.setTimeStart(start);
        w.setTimeEnd(end);
        return w;
    }

    private XSSFWorkbook happyWorkbook() {
        return build("Sheet1", java.util.List.of(
                row(0, "header"),
                row(0, "01.12.2025", 1, "baza", 4, "COMPANY1", 5, "1.5", 6, "2.5", 8, "А123", 9, "77", 10, "иван",
                        11, "REIS1", 12, "addr1", 13, "06:00", 14, "20:00"),
                row(0, "01.12.2025", 1, "baza", 4, "COMPANY1", 5, "1.5", 6, "2.5", 8, "А123", 9, "77", 10, "иван",
                        11, "REIS1", 12, "addr2", 13, "06:00", 14, "20:00"),
                row(0, "01.12.2025", 1, "baza", 4, "COMPANY2", 5, "3", 6, "4", 8, "В456", 9, "50", 10, "петр",
                        11, "REIS2", 12, "addr3", 13, "07:00", 14, "21:00"),
                row(0, "01.12.2025", 1, "baza", 4, "COMPANY3", 5, "5", 6, "6", 8, "С789", 9, "99", 10, "сидор",
                        11, "REIS3", 12, "addr4", 13, "#NULL!", 14, "#NULL!"),
                row(0, "01.12.2025", 1, "baza", 4, "COMPANY3", 5, "5", 6, "6", 8, "D111", 9, "99", 10, "федор",
                        11, "REIS4", 12, "addr5", 13, "23:00", 14, "01:00"),
                row(0, "01.12.2025", 8, "", 9, "77", 11, "REIS5"),
                row(0, ""),
                row(0, "")));
    }

    private void stubWindows() {
        lenient().when(dictionaryService.getSparWindows("COMPANY1")).thenReturn(java.util.Optional.of(windows("05:00", "19:00")));
        lenient().when(dictionaryService.getSparWindows(anyString())).thenReturn(java.util.Optional.empty());
    }

    @Test
    void getConvertedBookV2_success() {
        stubWindows();
        var result = converter.getConvertedBookV2(happyWorkbook());
        assertThat(result.getMessage()).isNotBlank();
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).hasSize(9);
    }

    @Test
    void getConvertedBookV2_allRowsInvalid_warningOnly() {
        var wb = build("Sheet1", java.util.List.of(
                row(0, "header"),
                row(0, "01.12.2025", 8, "", 9, "", 11, ""),
                row(0, ""),
                row(0, "")));
        var result = converter.getConvertedBookV2(wb);
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).isEmpty();
    }

    @Test
    void getCrmCreds_returnsConfigured() {
        when(crmConfigProperties.getSpar()).thenReturn(org.mockito.Mockito.mock(com.example.advantumconverter.config.properties.CrmConfigProperties.CrmCreds.class));
        assertThat(converter.getCrmCreds()).isNotNull();
    }

    @Test
    void getIdAndType() {
        assertThat(converter.getConverterName()).isNotBlank();
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.CLIENT);
        assertThat(converter.isV2()).isTrue();
    }

    @Test
    void getConvertedBookV2_windowsPresent() {
        when(dictionaryService.getSparWindows("COMPANY1"))
                .thenReturn(java.util.Optional.of(windows("05:00", "19:00")));
        var result = converter.getConvertedBookV2(happyWorkbook());
        assertThat(result).isNotNull();
    }

    @Test
    void getConvertedBookV2_badTime_parseError() {
        var wb = build("Sheet1", java.util.List.of(
                row(0, "header"),
                row(0, "01.12.2025", 4, "C", 8, "А123", 9, "77", 11, "REIS1", 12, "addr1", 13, "06:00", 14, "20:00"),
                row(0, "01.12.2025", 4, "C", 8, "А123", 9, "77", 11, "REIS1", 12, "addr2", 13, "garbage", 14, "20:00"),
                row(0, ""),
                row(0, "")));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> converter.getConvertedBookV2(wb))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }
}
