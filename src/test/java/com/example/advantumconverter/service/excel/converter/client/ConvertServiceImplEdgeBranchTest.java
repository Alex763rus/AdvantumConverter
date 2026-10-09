package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.DictionaryException;
import com.example.advantumconverter.service.database.DictionaryService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import utils.WorkbookBuilder;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConvertServiceImplEdgeBranchTest {

    private static void cell(XSSFSheet sheet, int r, int c, String value) {
        var row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        row.createCell(c).setCellValue(value);
    }

    private static XSSFSheet emptySheet() {
        return new XSSFWorkbook().createSheet();
    }

    private void setRowLimits(Object converter) {
        ReflectionTestUtils.setField(converter, "LAST_ROW", 10);
        ReflectionTestUtils.setField(converter, "LAST_COLUMN_NUMBER", 20);
        ReflectionTestUtils.setField(converter, "sheet", emptySheet());
    }

    @Test
    void cofix_helpers() {
        var converter = new ConvertServiceImplCofix();
        setRowLimits(converter);

        assertThat((Integer) ReflectionTestUtils.invokeMethod(converter, "fillX", 2)).isEqualTo(1);
        assertThat((Integer) ReflectionTestUtils.invokeMethod(converter, "fillX", 4)).isEqualTo(2);
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getFioTrack", 3, 12)).isEqualTo("0");
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getValueOrDefault", 1, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getValueOrDefault", 2, 0, 99)).isEmpty();
    }

    @Test
    void dominos_helpers() {
        var converter = new ConvertServiceImplDominos();
        setRowLimits(converter);

        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillX", 2)).isEqualTo("1");
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillX", 4)).isEqualTo("2");
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getFioTrack", 3, 12)).isEqualTo("0");
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getValueOrDefault", 1, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "getValueOrDefault", 2, 0, 99)).isEmpty();
    }

    @Test
    void artFruit_helpers() {
        var converter = new ConvertServiceImplArtFruit();
        ReflectionTestUtils.setField(converter, "sheet", emptySheet());

        assertThat((Date) ReflectionTestUtils.invokeMethod(converter, "fillS", 4, new Date())).isNull();
        assertThat((Date) ReflectionTestUtils.invokeMethod(converter, "fillT", 4)).isNull();
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(converter, "getDateFromFile", 4))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void artFruit_getConvertedBookV2_emptyWorkbook_throws() {
        assertThatThrownBy(() -> new ConvertServiceImplArtFruit().getConvertedBookV2(new XSSFWorkbook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void fragrantWorldMsk_fillInteger_invalid() {
        var converter = new ConvertServiceImplFragrantWorldMsk();
        assertThat((Integer) ReflectionTestUtils.invokeMethod(converter, "fillInteger", "abc")).isEqualTo(0);
    }

    @Test
    void fragrantWorldMsk_missingOrders_throws() {
        assertThatThrownBy(() -> new ConvertServiceImplFragrantWorldMsk().getConvertedBookV2(new XSSFWorkbook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void fragrantWorldMsk_missingDominoSheet_throws() {
        var book = new XSSFWorkbook();
        WorkbookBuilder.addSheet(book, "Orders", List.of());
        assertThatThrownBy(() -> new ConvertServiceImplFragrantWorldMsk().getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void fragrantWorldMsk_ordersMissingShop_throws() {
        var book = new XSSFWorkbook();
        WorkbookBuilder.addSheet(book, "Orders", List.of());
        WorkbookBuilder.addSheet(book, "ДЛЯ ДОМИНО", List.<String[]>of(new String[]{}));
        var domino = book.getSheet("ДЛЯ ДОМИНО");
        cell(domino, 1, 2, "01.01.2023");
        cell(domino, 1, 7, "1");
        cell(domino, 1, 10, "R");
        cell(domino, 1, 15, "org");
        cell(domino, 1, 16, "fio");
        cell(domino, 1, 20, "car");
        cell(domino, 1, 23, "1");
        cell(domino, 1, 28, "1");

        assertThatThrownBy(() -> new ConvertServiceImplFragrantWorldMsk().getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void metro_fillT_and_fillM_dictionaryMissing_throws() {
        var converter = new ConvertServiceImplMetro();
        var dict = mock(DictionaryService.class);
        ReflectionTestUtils.setField(converter, "dictionaryService", dict);
        when(dict.getMetroTimeEnd(any(), any())).thenReturn(null);
        when(dict.getMetroMaxTemperature(anyString())).thenReturn(null);

        var sheet = new XSSFWorkbook().createSheet();
        cell(sheet, 4, 2, "123");
        cell(sheet, 4, 24, "01/01/24");
        cell(sheet, 4, 27, "5");
        ReflectionTestUtils.setField(converter, "sheet", sheet);

        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(converter, "fillT", false, 4))
                .isInstanceOf(DictionaryException.class);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(converter, "fillM", 4))
                .isInstanceOf(DictionaryException.class);
    }

    private XSSFWorkbook manualOnly(boolean withDelivery, boolean withIncome) {
        var book = new XSSFWorkbook();
        var manual = book.createSheet("manual");
        cell(manual, 5, 4, "YR1");
        if (withDelivery) {
            cell(manual, 5, 1, "01.01.2024");
        }
        if (withIncome) {
            cell(manual, 5, 19, "01.01.2024 10:00");
        }
        return book;
    }

    @Test
    void metroPrimary_noManualSheet() {
        assertThatThrownBy(() -> new ConvertServiceImplMetroPrimary().getConvertedBookV2(new XSSFWorkbook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void metroPrimary_noWindowsSheet() {
        var book = manualOnly(true, true);
        assertThatThrownBy(() -> new ConvertServiceImplMetroPrimary().getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void metroPrimary_windowsEmpty() {
        var book = manualOnly(true, true);
        book.createSheet("Лист1");
        assertThat(new ConvertServiceImplMetroPrimary().getConvertedBookV2(book)).isNotNull();
    }

    @Test
    void metroPrimary_prepareDataError() {
        var book = manualOnly(false, true);
        var list = book.createSheet("Лист1");
        cell(list, 5, 2, "YR1");
        cell(list, 5, 7, "01.01.2024 10:00");
        cell(list, 5, 8, "01.01.2024 11:00");
        assertThatThrownBy(() -> new ConvertServiceImplMetroPrimary().getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }
}
