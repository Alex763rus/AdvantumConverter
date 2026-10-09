package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.exception.ExcelValidationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConvertServiceBaseTest {

    private final ConvertServiceBase base = new ConvertServiceBase();
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @BeforeEach
    void setUp() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("List");
        base.sheet = sheet;
    }

    private void setCell(int row, int col, String value) {
        var r = sheet.getRow(row) != null ? sheet.getRow(row) : sheet.createRow(row);
        r.createCell(col).setCellValue(value);
    }

    private void setDateCell(int row, int col, Date value) {
        var r = sheet.getRow(row) != null ? sheet.getRow(row) : sheet.createRow(row);
        var cell = r.createCell(col);
        var style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd.MM.yyyy"));
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    private void setFormula(int row, int col, String formula) {
        var r = sheet.getRow(row) != null ? sheet.getRow(row) : sheet.createRow(row);
        r.createCell(col).setCellFormula(formula);
    }

    @Test
    void getCellValue_nullRowAndNullCell() {
        assertThat(base.getCellValue(sheet, 5, 5)).isEmpty();
        sheet.createRow(0);
        assertThat(base.getCellValue(sheet, 0, 5)).isEmpty();
    }

    @Test
    void getCellValue_plain() {
        setCell(0, 0, "hello");
        assertThat(base.getCellValue(sheet, 0, 0)).isEqualTo("hello");
        assertThat(base.getCellValue(0, 0)).isEqualTo("hello");
        assertThat(base.getCellValueOrError(sheet, 0, 0)).isEqualTo("hello");
    }

    @Test
    void getCellValue_formulaTypes() {
        setFormula(0, 0, "1+2");
        assertThat(base.getCellValue(sheet, 0, 0)).isEqualTo("3.0");

        setFormula(0, 1, "\"abc\"");
        assertThat(base.getCellValue(sheet, 0, 1)).isEqualTo("abc");

        setFormula(0, 2, "1=1");
        assertThat(base.getCellValue(sheet, 0, 2)).isEqualTo("true");

        setFormula(0, 3, "1/0");
        assertThat(base.getCellValue(sheet, 0, 3)).isEqualTo("ERROR");
    }

    @Test
    void getCellValueOrError_formulaThrows() {
        setFormula(0, 0, "1+2");
        assertThatThrownBy(() -> base.getCellValueOrError(sheet, 0, 0))
                .isInstanceOf(ExcelValidationException.class);
    }

    @Test
    void getCellDate_variants() {
        assertThat(base.getCellDate(sheet, 5, 5)).isNull();
        sheet.createRow(0);
        assertThat(base.getCellDate(sheet, 0, 5)).isNull();
        setDateCell(1, 0, new Date(0L));
        assertThat(base.getCellDate(sheet, 1, 0)).isNotNull();
        base.sheet = sheet;
        assertThat(base.getCellDate(1, 0)).isNotNull();
    }

    @Test
    void getLastRow_and_getStartRow() {
        setCell(0, 0, "a");
        setCell(1, 0, "b");
        setCell(2, 0, "c");
        assertThat(base.getLastRow(0)).isEqualTo(2);
        assertThat(base.getStartRow("b")).isEqualTo(2);
        assertThat(base.getStartRow("zzz")).isZero();
    }

    @Test
    void integerAndDoubleValues() {
        setCell(0, 0, "7");
        setCell(0, 1, "");
        setCell(0, 2, "abc");
        setCell(0, 3, "1,5");
        assertThat(base.getIntegerValue(0, 0)).isEqualTo(7);
        assertThat(base.getIntegerValue(0, 1)).isNull();
        assertThat(base.getIntegerValue(0, 2)).isNull();
        assertThat(base.getIntegerValue(0, 1, 9)).isEqualTo(9);
        assertThat(base.getIntegerValue(sheet, 0, 0, 9)).isEqualTo(7);
        assertThat(base.getIntegerValueOrErrorIfFormula(sheet, 0, 0, 9)).isEqualTo(7);
        assertThat(base.getIntegerValueOrErrorIfFormula(sheet, 0, 1, 9)).isEqualTo(9);
        assertThat(base.convertToIntegerOrNull("")).isNull();
        assertThat(base.convertToIntegerOrNull("x")).isNull();
        assertThat(base.convertToIntegerOrNull("12")).isEqualTo(12);
        assertThat(base.getDoubleValue(sheet, 0, 3)).isEqualTo(1.5);
        assertThat(base.getDoubleValue(sheet, 0, 1)).isNull();
        assertThat(base.getDoubleValue(0, 3)).isEqualTo(1.5);
    }

    @Test
    void integerValueOrErrorIfFormula_formulaThrows() {
        setFormula(0, 0, "1+2");
        assertThatThrownBy(() -> base.getIntegerValueOrErrorIfFormula(sheet, 0, 0, 9))
                .isInstanceOf(ExcelValidationException.class);
    }

    @Test
    void currentDateAndConvertDate() throws Exception {
        assertThat(base.getCurrentDate("dd.MM.yyyy")).isNotEmpty();
        setCell(0, 0, "15.01.2025");
        assertThat(base.convertDate(0, 0, List.of("dd.MM.yyyy"))).isPresent();
        assertThat(base.convertDate(0, 1, List.of("dd.MM.yyyy"))).isEmpty();
    }

    @Test
    void getExcelList() {
        assertThat(base.getExcelList(workbook, "List")).isEqualTo(sheet);
        assertThatThrownBy(() -> base.getExcelList(workbook, "missing"))
                .isInstanceOf(ExcelListNotFoundException.class);
    }

    @Test
    void createDefaultBooks() {
        var v1 = base.createDefaultBook("book", "list", List.of(List.of("a")), "msg");
        assertThat(v1.getBookName()).isEqualTo("book");
        assertThat(v1.getBook()).hasSize(1);

        var v2 = base.createDefaultBookV2(List.of(), List.of(), "name");
        assertThat(v2.getBookV2()).hasSize(1);
        assertThat(v2.getMessage()).isEqualTo(com.example.advantumconverter.constant.Constant.Heap.DONE);

        var v2warn = base.createDefaultBookV2(List.of(), List.of("warn1", "warn1"), "name",
                List.of("h"), "export");
        assertThat(v2warn.getMessage()).contains("warn1");
    }
}
