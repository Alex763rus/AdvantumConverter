package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.jpa.ozon.OzonDictionary;
import com.example.advantumconverter.service.database.DictionaryService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.advantumconverter.constant.Constant.Converter.COMPANY_DEAL_AUTO_TRANS_INCORRECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static utils.WorkbookBuilder.build;

@ExtendWith(MockitoExtension.class)
class ConvertServiceImplOzonTest {

    @Mock
    private DictionaryService dictionaryService;

    private ConvertServiceImplOzon converter;

    @BeforeEach
    void setUp() {
        converter = new ConvertServiceImplOzon();
        ReflectionTestUtils.setField(converter, "dictionaryService", dictionaryService);
        converter.init();
    }

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

    private OzonDictionary dictionary(String in, String out) {
        var d = new OzonDictionary();
        d.setStockInTime(in);
        d.setStockOutTime(out);
        return d;
    }

    private void stubHappy() {
        lenient().when(dictionaryService.getOzonTransitTime(anyString(), anyString())).thenReturn("00:30:00");
        lenient().when(dictionaryService.getOzonTonnageTime(any())).thenReturn(Optional.of("01:00:00"));
        lenient().when(dictionaryService.getOzonLoadUnloadTime(anyString())).thenReturn(Optional.of(10));
        lenient().when(dictionaryService.getBestDictionary(anyString(), anyInt())).thenReturn(dictionary("10:00", "00:00"));
    }

    private XSSFWorkbook workbook(String org2, String col37Row1, String carRow2) {
        return build("Sheet1", List.of(
                row(0, "header"),
                row(0, "KEY1", 1, "25-01-01 08:00:00", 3, "ООО Орг", 10, "1,5", 11, "12", 12, "30", 13, "20",
                        14, "склад", 22, "A", 23, "B", 30, "А123ВС", 32, "999", 37, col37Row1, 38, "2"),
                row(0, "KEY1", 1, "25-01-01 09:00:00", 3, org2, 10, "2", 11, "5", 12, "10", 13, "40",
                        14, "склад", 22, "A", 23, "C", 30, carRow2, 32, "888", 37, "0", 38, "1"),
                row(0, ""),
                row(0, "")));
    }

    @Test
    void getConvertedBook_success_fullBranches() {
        stubHappy();
        var result = converter.getConvertedBook(workbook("ООО Орг", "0", "X99"));
        assertThat(result.getMessage()).isNotBlank();
        assertThat(result.getBook()).isNotEmpty();
        assertThat(result.getBook().get(0).getExcelListContent()).isNotEmpty();
    }

    @Test
    void getConvertedBook_success_dictionaryPaths() {
        stubHappy();
        var result = converter.getConvertedBook(workbook(COMPANY_DEAL_AUTO_TRANS_INCORRECT, "1", "А123ВС"));
        assertThat(result.getMessage()).isNotBlank();
    }

    @Test
    void getConvertedBook_transitTimeNotFound() {
        stubHappy();
        when(dictionaryService.getOzonTransitTime(anyString(), anyString())).thenReturn(null);
        assertThatThrownBy(() -> converter.getConvertedBook(workbook("ООО Орг", "0", "X99")))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBook_tonnageTimeNotFound() {
        stubHappy();
        when(dictionaryService.getOzonTonnageTime(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> converter.getConvertedBook(workbook("ООО Орг", "0", "X99")))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBook_loadUnloadTimeNotFound() {
        stubHappy();
        when(dictionaryService.getOzonLoadUnloadTime(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> converter.getConvertedBook(workbook("ООО Орг", "0", "X99")))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getConvertedBook_bestDictionaryNull_fallbackTime() {
        stubHappy();
        when(dictionaryService.getBestDictionary(anyString(), anyInt())).thenReturn(null);

        var result = converter.getConvertedBook(workbook("ООО Орг", "1", "X99"));

        assertThat(result.getMessage()).isNotBlank();
    }

    @Test
    void getConvertedBook_badRowOrder_convertError() {
        stubHappy();
        var wb = build("Sheet1", List.of(
                row(0, "header"),
                row(0, "KEY1", 1, "25-01-01 08:00:00", 10, "1", 38, "abc"),
                row(0, ""),
                row(0, "")));

        assertThatThrownBy(() -> converter.getConvertedBook(wb))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void getIdAndType() {
        assertThat(converter.getConverterName()).isNotBlank();
        assertThat(converter.getConverterCommand()).isNotBlank();
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.CLIENT);
        assertThat(converter.isV2()).isFalse();
    }
}
