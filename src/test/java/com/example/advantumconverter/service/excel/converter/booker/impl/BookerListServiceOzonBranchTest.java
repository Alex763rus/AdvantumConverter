package com.example.advantumconverter.service.excel.converter.booker.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import utils.WorkbookBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookerListServiceOzonBranchTest {

    private final BookerListServiceOzon service = new BookerListServiceOzon();

    private org.apache.poi.xssf.usermodel.XSSFSheet checkedSheet() {
        var workbook = WorkbookBuilder.build("Sheet1", List.<String[]>of(
                new String[]{"h", "h", "7706644017", "", "", "", "", "", "", "", ""}));
        return workbook.getSheetAt(0);
    }

    @Test
    void privateInnAndRateHelpers() {
        ReflectionTestUtils.setField(service, "sheet", checkedSheet());

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getInn", 0)).isEqualTo("7706644017");
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "prepareInn", 0)).isEqualTo("7717655878");

        assertThat((Double) ReflectionTestUtils.invokeMethod(service, "prepareRate", 0, 9))
                .isEqualTo(500.0);
    }

    @Test
    void prepareRate_returnsRateWhenPresent() {
        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("s");
        var row = sheet.createRow(0);
        row.createCell(2).setCellValue("123");
        row.createCell(9).setCellValue(321.0);
        ReflectionTestUtils.setField(service, "sheet", sheet);

        assertThat((Double) ReflectionTestUtils.invokeMethod(service, "prepareRate", 0, 9))
                .isEqualTo(321.0);
    }
}
