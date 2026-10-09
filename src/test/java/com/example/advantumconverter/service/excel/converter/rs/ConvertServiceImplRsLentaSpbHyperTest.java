package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.addSheet;
import static utils.WorkbookBuilder.build;

class ConvertServiceImplRsLentaSpbHyperTest {

    private final ConvertServiceImplRsLentaSpbHyper service = new ConvertServiceImplRsLentaSpbHyper();

    private static String[] sparseRow(int len, Object... kv) {
        String[] arr = new String[len];
        for (int i = 0; i < kv.length; i += 2) {
            arr[(Integer) kv[i]] = (String) kv[i + 1];
        }
        return arr;
    }

    private static void setFormula(XSSFWorkbook wb, String sheetName, int rowIdx, int col, String formula) {
        var sheet = wb.getSheet(sheetName);
        Row r = sheet.getRow(rowIdx);
        if (r == null) {
            r = sheet.createRow(rowIdx);
        }
        var c = r.getCell(col);
        if (c == null) {
            c = r.createCell(col);
        }
        c.setCellFormula(formula);
    }

    @Test
    void prepareDateStartEnd_time1AfterTime2_addsDay() {
        var result = ReflectionTestUtils.invokeMethod(service, "prepareDateStartEnd",
                "01.12.2025", "09:00", "08:00", LocalTime.of(18, 0), 0);

        assertThat(result).isInstanceOf(Pair.class);
        assertThat((Pair<?, ?>) result).isNotNull();
    }

    @Test
    void getConvertedBookV2_missingSvodSheet_throws() {
        var wb = build("Исходные данные заказов", List.of());

        assertThatThrownBy(() -> service.getConvertedBookV2(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_formulaCells_warningsOnly() {
        var wb = new XSSFWorkbook();
        var mainRows = new ArrayList<String[]>();
        mainRows.add(null);
        mainRows.add(null);
        mainRows.add(null);
        mainRows.add(sparseRow(16, 2, "123", 4, "1"));
        mainRows.add(sparseRow(16, 2, "124", 4, "1"));
        addSheet(wb, "Исходные данные заказов", mainRows);

        var svodRows = new ArrayList<String[]>();
        svodRows.add(null);
        svodRows.add(sparseRow(12, 0, "999", 10, "1"));
        svodRows.add(sparseRow(12, 0, "124", 8, "bad", 9, "time", 10, "5", 11, "0"));
        addSheet(wb, "Сводная", svodRows);

        setFormula(wb, "Исходные данные заказов", 3, 4, "1+1");
        setFormula(wb, "Сводная", 1, 10, "1+1");

        var result = service.getConvertedBookV2(wb);

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).contains("предупреждени");
    }
}
