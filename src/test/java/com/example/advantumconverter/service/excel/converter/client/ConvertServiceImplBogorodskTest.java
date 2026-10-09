package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplBogorodskTest {

    private final ConvertServiceImplBogorodsk service = new ConvertServiceImplBogorodsk();

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

    private org.apache.poi.xssf.usermodel.XSSFWorkbook workbook(String date) {
        var wb = build("Лист1", List.of(
                row(0, "header"),
                row(0, "x", 1, "A1", 2, date, 4, "4v", 5, "5v", 13, "U1", 21, "U2", 23, "U3"),
                row(0, ""),
                row(0, "")));
        var sheet = wb.getSheetAt(0);
        for (int col : new int[]{3, 20, 22}) {
            sheet.getRow(1).getCell(col).setCellValue(new Date());
        }
        return wb;
    }

    @Test
    void getConvertedBook_success() {
        var result = service.getConvertedBook(workbook("01/15/25"));
        assertThat(result.getBook().get(0).getExcelListContent()).hasSize(4);
    }

    @Test
    void getConvertedBook_wrapsException() {
        assertThatThrownBy(() -> service.getConvertedBook(workbook("bad")))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void basics() {
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
    }
}
