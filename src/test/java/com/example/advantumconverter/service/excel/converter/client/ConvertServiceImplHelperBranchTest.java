package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertServiceImplHelperBranchTest {

    private org.apache.poi.xssf.usermodel.XSSFSheet sheetWithRow(int rowIndex) {
        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("s");
        sheet.createRow(rowIndex);
        return sheet;
    }

    @Test
    void nika_fillZAndFillY_noSpace_returnsZero() {
        var service = new ConvertServiceImplNika(new CrmConfigProperties());
        ReflectionTestUtils.setField(service, "sheet", new XSSFWorkbook().createSheet("s"));
        assertThat((Double) ReflectionTestUtils.invokeMethod(service, "fillZ", 0, true)).isEqualTo(0.0);
        assertThat((Double) ReflectionTestUtils.invokeMethod(service, "fillY", 0, true)).isEqualTo(0.0);
    }

    @Test
    void siel_getConvertedBookV2_badWorkbook_convertError() {
        var service = new ConvertServiceImplSiel();
        var wb = new XSSFWorkbook();
        wb.createSheet("s");

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getConvertedBookV2(wb))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }

    @Test
    void lenta_getValueOrDefault_bounds() {
        var service = new ConvertServiceImplLenta();
        ReflectionTestUtils.setField(service, "START_ROW", 1);
        ReflectionTestUtils.setField(service, "LAST_ROW", 5);
        ReflectionTestUtils.setField(service, "LAST_COLUMN_NUMBER", 10);
        ReflectionTestUtils.setField(service, "sheet", new XSSFWorkbook().createSheet("s"));

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 0, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 1, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 3, 0, 99)).isEmpty();
    }

    @Test
    void nika_getConvertedBookV2_badWorkbook_convertError() {
        var service = new ConvertServiceImplNika(new CrmConfigProperties());
        var wb = utils.WorkbookBuilder.build("Sheet1",
                java.util.List.<String[]>of(utils.WorkbookBuilder.row(0, "only-header")));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getConvertedBookV2(wb))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }

    @Test
    void samokat_getValueOrDefault_bounds() {
        var service = new ConvertServiceImplSamokat();
        ReflectionTestUtils.setField(service, "LAST_ROW", 5);
        ReflectionTestUtils.setField(service, "LAST_COLUMN_NUMBER", 0);
        ReflectionTestUtils.setField(service, "sheet", new XSSFWorkbook().createSheet("s"));

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 0, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 1, 0, 5)).isEmpty();
    }

    @Test
    void kznFatOil_getValueOrDefault_bounds() {
        var service = new ConvertServiceImplKznFatOil();
        ReflectionTestUtils.setField(service, "START_ROW", 1);
        ReflectionTestUtils.setField(service, "LAST_ROW", 5);
        ReflectionTestUtils.setField(service, "LAST_COLUMN_NUMBER", 0);
        ReflectionTestUtils.setField(service, "sheet", new XSSFWorkbook().createSheet("s"));

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 0, 0, 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getValueOrDefault", 1, 0, 5)).isEmpty();
    }

    @Test
    void metro_calcTonnage() {
        var service = new ConvertServiceImplMetro();
        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("s");
        var row = sheet.createRow(0);
        row.createCell(10).setCellValue("1,5");
        ReflectionTestUtils.setField(service, "sheet", sheet);

        assertThat((Integer) ReflectionTestUtils.invokeMethod(service, "calcTonnage", 0)).isEqualTo(1500);
    }
}
