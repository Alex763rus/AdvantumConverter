package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsLentaSpbV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RsLentaSpbExcelGenerateServiceTest {

    private final RsLentaSpbExcelGenerateService service = new RsLentaSpbExcelGenerateService();

    private ConvertedListDataRsLentaSpbV2 repeatRow() {
        return ConvertedListDataRsLentaSpbV2.init()
                .setColumnAdata("A")
                .setColumnBdata(new Date())
                .setColumnCdata("C")
                .setColumnDdata("101")
                .setColumnEdata("E")
                .setColumnFdata("F")
                .setColumnGdata("G")
                .setColumnHdata("H")
                .setColumnIdata("I")
                .setColumnJdata("J")
                .setColumnKdata("K")
                .setColumnLdata(1)
                .setColumnMdata("M")
                .setColumnNdata(2)
                .setColumnOdata("O")
                .setColumnPdata(3)
                .setColumnQdata("Q")
                .setColumnRdata("R")
                .setTechCountRepeat(null)
                .setTechRepeats(List.of(
                        new ConvertedListDataRsLentaSpbV2.Repeat(1, "Гр1", "Склад A"),
                        new ConvertedListDataRsLentaSpbV2.Repeat(2, "Гр2", "Склад B")))
                .build();
    }

    private ConvertedListDataRsLentaSpbV2 countRow() {
        return ConvertedListDataRsLentaSpbV2.init()
                .setColumnBdata(new Date())
                .setColumnDdata("202")
                .setColumnLdata(1)
                .setColumnNdata(2)
                .setColumnPdata(3)
                .setTechCountRepeat(2)
                .setTechProductGroup("PG")
                .build();
    }

    @Test
    void createXlsxV2_success() {
        var list = ConvertedListV2.init()
                .setExcelListName("Orders")
                .setHeadersV2(List.of("h1", "h2", "h3"))
                .setExcelListContentV2(List.of(repeatRow(), countRow()))
                .build();
        var book = ConvertedBookV2.init().setBookName("RsLentaSpb").setBookV2(List.of(list)).build();
        assertThat(service.createXlsxV2(book)).isNotNull();
    }

    @Test
    void privateCellHelpers() {
        var list = ConvertedListV2.init()
                .setExcelListName("Orders")
                .setHeadersV2(List.of("h1", "h2", "h3"))
                .setExcelListContentV2(List.of(countRow()))
                .build();
        service.createXlsxV2(ConvertedBookV2.init().setBookName("RsLentaSpb").setBookV2(List.of(list)).build());
        ReflectionTestUtils.setField(service, "data", List.of(List.of("A", "15.01.2025", "C", "D", "1", "1.5")));
        var wb = (org.apache.poi.ss.usermodel.Workbook) ReflectionTestUtils.getField(service, "workbook");
        var row = wb.createSheet("extra").createRow(0);
        ReflectionTestUtils.invokeMethod(service, "createCellString", row, 0, 0);
        ReflectionTestUtils.invokeMethod(service, "createCellDate", row, 0, 1, "dd.MM.yyyy", ReflectionTestUtils.getField(service, "styleDateDot"));
        ReflectionTestUtils.invokeMethod(service, "createCellInt", row, 0, 4);
        ReflectionTestUtils.invokeMethod(service, "createCellDouble", row, 0, 5);
        ReflectionTestUtils.invokeMethod(service, "createGeneralCell", row, 9, 1.5);
        ReflectionTestUtils.invokeMethod(service, "createCell", row, 6, 1.5);
        assertThat(row.getCell(0).getStringCellValue()).isEqualTo("A");
    }

    @Test
    void createXlsxV2_badDataCast() {
        var list = ConvertedListV2.init()
                .setExcelListName("Orders")
                .setHeadersV2(List.of("h1", "h2", "h3"))
                .setExcelListContentV2(List.of(
                        com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2.init().build()))
                .build();
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.createXlsxV2(
                        ConvertedBookV2.init().setBookName("RsLentaSpb").setBookV2(List.of(list)).build()))
                .isInstanceOf(com.example.advantumconverter.exception.ExcelGenerationException.class);
    }
}
