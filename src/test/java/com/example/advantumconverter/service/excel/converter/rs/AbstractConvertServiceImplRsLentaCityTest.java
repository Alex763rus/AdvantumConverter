package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import utils.WorkbookBuilder;

import java.util.Arrays;
import java.util.List;

import static com.example.advantumconverter.enums.ExcelType.RS_LENTA_SPB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AbstractConvertServiceImplRsLentaCityTest {

    private final ConvertServiceImplRsLentaSpb service = new ConvertServiceImplRsLentaSpb();

    @Test
    void getExcelType_andV2() {
        assertThat(service.getExcelType()).isEqualTo(RS_LENTA_SPB);
        assertThat(service.isV2()).isTrue();
    }

    @Test
    void getConvertedBookV2_missingSheet() {
        assertThatThrownBy(() -> service.getConvertedBookV2(new XSSFWorkbook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_missingSpWindowsSheet() {
        var book = WorkbookBuilder.build("Исходные данные заказов", List.of());
        assertThatThrownBy(() -> service.getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_missingSpParamsSheet() {
        var book = WorkbookBuilder.build("Исходные данные заказов", List.of());
        WorkbookBuilder.addSheet(book, "Сп-к ТТ-окна", List.of());
        assertThatThrownBy(() -> service.getConvertedBookV2(book))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBookV2_success() {
        String[] mainRow = new String[15];
        mainRow[5] = "YR1";
        mainRow[7] = "City";
        mainRow[8] = "Addr";
        mainRow[9] = "C";
        mainRow[13] = "тара";
        mainRow[14] = "PG";
        var book = WorkbookBuilder.build("Исходные данные заказов",
                Arrays.asList(null, null, null, mainRow));
        WorkbookBuilder.addSheet(book, "Сп-к ТТ-окна", Arrays.asList(null, null, null,
                WorkbookBuilder.row(3, "YR1", 5, "09:00-18:00", 6, "09:00-18:00", 7, "09:00-18:00")));
        WorkbookBuilder.addSheet(book, "СП -параметры ТК", Arrays.asList(null, null,
                WorkbookBuilder.row(0, "YR1", 2, "Паллета", 3, "20")));
        WorkbookBuilder.addSheet(book, "Свод", Arrays.asList(null,
                WorkbookBuilder.row(0, "YR1", 1, "АЛКО", 6, "09:00", 7, "10:00")));

        ConvertedBookV2 result = service.getConvertedBookV2(book);
        assertThat(result).isNotNull();
    }

    @Test
    void getTime_variants() {
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "", 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "-", 0)).isEmpty();
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "09:00-18:00", 0)).isEqualTo("09:00");
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "09:00-18:00", 1)).isEqualTo("18:00");
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "9:00-18:00", 0)).isEqualTo("9:00");
        assertThat((String) ReflectionTestUtils.invokeMethod(service, "getTime", "YR", "garbage", 0)).isEmpty();
    }
}
