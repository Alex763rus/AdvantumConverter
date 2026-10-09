package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsLentaV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RsExcelGenerateServiceTest {

    private final RsExcelGenerateService service = new RsExcelGenerateService();

    private ConvertedListDataRsLentaV2 full() {
        return ConvertedListDataRsLentaV2.init()
                .setColumnAdata("A")
                .setColumnBdata(3)
                .setColumnCdata("C")
                .setColumnDdata("D")
                .setColumnEdata(new Date())
                .setColumnFdata(new Date())
                .setColumnGdata("G")
                .setColumnHdata("H")
                .setColumnIdata(1)
                .setColumnJdata(1.5)
                .setColumnKdata(2.5)
                .setColumnLdata("L")
                .setColumnMdata("M")
                .setColumnNdata("N")
                .build();
    }

    private ConvertedListDataRsLentaV2 empty() {
        return ConvertedListDataRsLentaV2.init().build();
    }

    private ConvertedBookV2 book() {
        var list = ConvertedListV2.init()
                .setExcelListName("Orders")
                .setHeadersV2(List.of("h1", "h2", "h3"))
                .setExcelListContentV2(List.of(full(), empty()))
                .build();
        return ConvertedBookV2.init().setBookName("RsLenta").setBookV2(List.of(list)).build();
    }

    @Test
    void createXlsxV2_success() {
        assertThat(service.createXlsxV2(book())).isNotNull();
    }

    @Test
    void privateCellHelpers() {
        service.createXlsxV2(book());
        ReflectionTestUtils.setField(service, "data", List.of(List.of("A", "15.01.2025", "C", "D", "1", "1.5")));
        var wb = (org.apache.poi.ss.usermodel.Workbook) ReflectionTestUtils.getField(service, "workbook");
        var row = wb.createSheet("extra").createRow(0);
        ReflectionTestUtils.invokeMethod(service, "createCellString", row, 0, 0);
        ReflectionTestUtils.invokeMethod(service, "createCellDate", row, 0, 1, "dd.MM.yyyy", ReflectionTestUtils.getField(service, "styleDateDot"));
        ReflectionTestUtils.invokeMethod(service, "createCellInt", row, 0, 4);
        ReflectionTestUtils.invokeMethod(service, "createCellDouble", row, 0, 5);
        ReflectionTestUtils.invokeMethod(service, "createGeneralCell", row, 6, 1.5);
        ReflectionTestUtils.invokeMethod(service, "createCell", row, 7, 1.5);
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
        var book = ConvertedBookV2.init().setBookName("RsLenta").setBookV2(List.of(list)).build();
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.createXlsxV2(book))
                .isInstanceOf(com.example.advantumconverter.exception.ExcelGenerationException.class);
    }
}
