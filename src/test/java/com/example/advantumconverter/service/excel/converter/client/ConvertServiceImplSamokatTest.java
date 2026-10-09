package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplSamokatTest {

    private final ConvertServiceImplSamokat service = new ConvertServiceImplSamokat();

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

    private XSSFWorkbook workbook() {
        var wb = build("Лист1", List.of(
                row(0, "header"),
                row(0, "x", 1, "K1", 3, "C A R", 4, "FIO"),
                row(0, "x", 1, "K1", 3, "C A R", 4, "FIO"),
                row(0, "x", 1, "K2", 3, "C A R", 4, "FIO"),
                row(0, ""),
                row(0, "")));
        var sheet = wb.getSheetAt(0);
        for (int r = 1; r <= 3; r++) {
            sheet.getRow(r).getCell(0).setCellValue(new Date());
        }
        return wb;
    }

    @Test
    void getConvertedBook_success() {
        var result = service.getConvertedBook(workbook());
        // header + (2 + 1 + 2) data lines
        assertThat(result.getBook().get(0).getExcelListContent()).hasSize(6);
    }

    @Test
    void getConvertedBook_wrapsException() {
        var wb = build("Лист1", List.of(
                row(0, "header"),
                row(0, "bad", 1, "K1", 3, "CAR", 4, "FIO"),
                row(0, ""),
                row(0, "")));
        assertThatThrownBy(() -> service.getConvertedBook(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
    }
}
