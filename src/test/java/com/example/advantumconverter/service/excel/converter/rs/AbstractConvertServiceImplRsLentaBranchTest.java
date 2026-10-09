package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.exception.ConvertProcessingException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static utils.WorkbookBuilder.build;
import static utils.WorkbookBuilder.row;

class AbstractConvertServiceImplRsLentaBranchTest {

    static class TestRsLenta extends AbstractConvertServiceImplRsLenta {
        @Override
        String calculatePointType(String pointOperationType, Integer pointTypeReturnMassa) {
            return "D";
        }
    }

    private final TestRsLenta service = new TestRsLenta();

    @Test
    void metadata_fromAbstractBase() {
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.isV2()).isTrue();
        assertThat(service.getExcelType()).isNotNull();
    }

    @Test
    void getConvertedBookV2_noSheets_throws() {
        assertThatThrownBy(() -> service.getConvertedBookV2(new XSSFWorkbook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_missingSecondSheet_throws() {
        var wb = build("Main", List.<String[]>of(
                row(0, "R1", 3, "01.12.2025", 10, "G1")));

        assertThatThrownBy(() -> service.getConvertedBookV2(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }
}
