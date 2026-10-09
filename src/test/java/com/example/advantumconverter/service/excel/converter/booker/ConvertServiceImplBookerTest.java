package com.example.advantumconverter.service.excel.converter.booker;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_ASHAN;
import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_AV;
import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_METRO;
import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_X5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConvertServiceImplBookerTest {

    private ConvertServiceImplBooker converter;
    private BookerListService service;
    private final Map<String, BookerListService> map = new HashMap<>();

    @BeforeEach
    void setUp() {
        converter = new ConvertServiceImplBooker();
        service = mock(BookerListService.class);
        for (String key : List.of(BOOKER_X5, BOOKER_ASHAN, BOOKER_AV, BOOKER_METRO)) {
            map.put(key, service);
        }
        ReflectionTestUtils.setField(converter, "bookerListServiceMap", map);
    }

    private BookerInputData input(String client, String inn, String car, double rate, int rnic, int ozon) {
        return BookerInputData.init()
                .setClient(client).setCounterparty("cp").setInn(inn).setCarNumber(car).setRate(rate)
                .setRnic(rnic).setX5(0).setAshan(0).setMetro(0).setOzon(ozon).setAv(0)
                .setBilla(0).setMagnit(0).setLoginet(0).setOboz(0).setRezident(0).setVerniy(0).setAtmc(0)
                .build();
    }

    private List<BookerInputData> data() {
        return List.of(
                input("_REF", "111", "A", 100, 0, 0),
                input("B", "111", "A", 50, 0, 0),
                input("B", "222", "B", 100, 2, 0),
                input("X5", "222", "B", 500, 0, 0),
                input("A", "222", "C", 300, 1, 0),
                input("X5", "333", "D", 400, 0, 0),
                input("B", "333", "D", 700, 0, 0),
                input("B", "444", "E", 0, 0, 50));
    }

    @Test
    void getConvertedBook_success() {
        when(service.getConvertedList(any(), eq(BOOKER_X5))).thenReturn(data());
        when(service.getConvertedList(any(), eq(BOOKER_ASHAN))).thenReturn(List.of());
        when(service.getConvertedList(any(), eq(BOOKER_AV))).thenReturn(List.of());
        when(service.getConvertedList(any(), eq(BOOKER_METRO))).thenReturn(List.of());

        ConvertedBook result = converter.getConvertedBook(new XSSFWorkbook());
        assertThat(result.getMessage()).isNotBlank();
        assertThat(result.getBook()).hasSize(5);
    }

    @Test
    void getConvertedBook_listNotFound() {
        when(service.getConvertedList(any(), eq(BOOKER_X5))).thenReturn(List.of());
        when(service.getConvertedList(any(), eq(BOOKER_ASHAN))).thenReturn(List.of());
        when(service.getConvertedList(any(), eq(BOOKER_AV)))
                .thenThrow(new ExcelListNotFoundException("AV"));
        when(service.getConvertedList(any(), eq(BOOKER_METRO))).thenReturn(List.of());

        ConvertedBook result = converter.getConvertedBook(new XSSFWorkbook());
        assertThat(result.getMessage()).isNotBlank();
    }

    @Test
    void fillAndGetId() throws Exception {
        var wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("s");
        Row row = sheet.createRow(1);
        row.createCell(9).setCellValue("12/25/25");
        row.createCell(10).setCellValue("10:30");
        row.createCell(12).setCellValue("12/25/25");
        row.createCell(13).setCellValue("11:30");
        ReflectionTestUtils.setField(converter, "sheet", sheet);

        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillS", true, 1)).isNotBlank();
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillS", false, 1)).isNotBlank();
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillT", true, 1)).isNotBlank();
        assertThat((String) ReflectionTestUtils.invokeMethod(converter, "fillT", false, 1)).isNotBlank();

        assertThat(converter.getConverterName()).isNotBlank();
        assertThat(converter.getConverterCommand()).isNotBlank();
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.BOOKER);
        assertThat(converter.isV2()).isFalse();
    }
}
