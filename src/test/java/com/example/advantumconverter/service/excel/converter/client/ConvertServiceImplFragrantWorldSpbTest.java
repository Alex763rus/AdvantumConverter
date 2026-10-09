package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.addSheet;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplFragrantWorldSpbTest {

    private ConvertServiceImplFragrantWorldSpb converter;

    @BeforeEach
    void setUp() {
        converter = new ConvertServiceImplFragrantWorldSpb();
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

    private XSSFWorkbook workbook(List<String[]> addresses, List<String[]> domino) {
        var wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        utils.WorkbookBuilder.addSheet(wb, "Адреса магазинов", addresses);
        utils.WorkbookBuilder.addSheet(wb, "ДЛЯ ДОМИНО", domino);
        return wb;
    }

    private java.util.List<String[]> addresses() {
        return java.util.List.of(
                row(0, "header"),
                row(0, "SHOP1", 1, "ул. Ленина 1", 2, "СПб", 7, "8:00-19:00"),
                row(0, "SHOP2", 1, "ул. Мира 2", 2, "СПб", 7, "09:30-20:00"),
                row(0, ""));
    }

    private java.util.List<String[]> dominoHappy() {
        return java.util.List.of(
                row(0, "header"),
                row(0, "x", 2, "01.12.2025", 9, "SHOP1", 10, "REIS1", 13, "08:00", 14, "18:00",
                        15, "ООО Орг", 16, "иван иванов", 20, "А123 ВС", 23, "1,5", 28, "3"),
                row(0, "x", 2, "01.12.2025", 9, "SHOP1", 10, "REIS1", 13, "08:00", 14, "18:00",
                        15, "ООО Орг", 16, "дубль", 20, "А123 ВС", 23, "1,5", 28, "3"),
                row(0, "x", 2, "01.12.2025", 9, "SHOP2", 10, "REIS2_РЕФ", 13, "", 14, "",
                        15, "ООО Орг2", 16, "петр петров", 20, "В456", 23, "abc", 28, "4"),
                row(0, ""));
    }

    private java.util.List<String[]> domino(String point, String reis, String timeStart, String timeEnd, String tonnage) {
        return java.util.List.of(
                row(0, "header"),
                row(0, "x", 2, "01.12.2025", 9, point, 10, reis, 13, timeStart, 14, timeEnd,
                        15, "ООО Орг", 16, "иван", 20, "А123", 23, tonnage, 28, "1"),
                row(0, ""));
    }

    @Test
    void getConvertedBookV2_success() {
        var result = converter.getConvertedBookV2(workbook(addresses(), dominoHappy()));
        assertThat(result.getMessage()).isNotBlank();
        assertThat(result.getBookV2()).hasSize(1);
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).hasSize(4);
    }

    @Test
    void getConvertedBookV2_missingAddressSheet() {
        var wb = build("ДЛЯ ДОМИНО", dominoHappy());
        assertThatThrownBy(() -> converter.getConvertedBookV2(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_missingDominoSheet() {
        var wb = build("Адреса магазинов", addresses());
        assertThatThrownBy(() -> converter.getConvertedBookV2(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_addressNotFound() {
        assertThatThrownBy(() -> converter.getConvertedBookV2(workbook(addresses(), domino("UNKNOWN", "REIS1", "08:00", "18:00", "1"))))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_badTimeThrows() {
        var addr = java.util.List.of(
                row(0, "header"),
                row(0, "SHOP3", 1, "addr", 2, "city", 7, "bad-time-format-extra"),
                row(0, ""));
        assertThatThrownBy(() -> converter.getConvertedBookV2(workbook(addr, domino("SHOP3", "REIS1", "08:00", "18:00", "1"))))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getTime_variants() {
        assertThat((String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(converter, "getTime", "R", "", 0)).isEmpty();
        assertThat((String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(converter, "getTime", "R", "-", 0)).isEmpty();
        assertThat((String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(converter, "getTime", "R", "8:00-19:00", 0)).isEqualTo("8:00");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(converter, "getTime", "R", "08:00-19:00", 1)).isEqualTo("19:00");
        assertThat((String) org.springframework.test.util.ReflectionTestUtils.invokeMethod(converter, "getTime", "R", "abc", 0)).isEmpty();
    }

    @Test
    void getExcelType_isClient() {
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.CLIENT);
        assertThat(converter.isV2()).isTrue();
    }
}
