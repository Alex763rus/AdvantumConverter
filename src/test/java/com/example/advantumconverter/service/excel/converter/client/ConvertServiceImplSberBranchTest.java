package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.SberAddressNotFoundException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.jpa.sber.SberAddressDictionary;
import com.example.advantumconverter.service.database.DictionaryService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import utils.WorkbookBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConvertServiceImplSberBranchTest {

    private ConvertServiceImplSber service(DictionaryService dictionaryService) {
        var service = new ConvertServiceImplSber(new CrmConfigProperties());
        ReflectionTestUtils.setField(service, "dictionaryService", dictionaryService);
        return service;
    }

    private XSSFWorkbook addressBook() {
        return WorkbookBuilder.build("Sheet1", List.of(
                new String[]{"address"},
                new String[]{"a"},
                new String[]{"x", "x"}));
    }

    @Test
    void emptyCity_throwsSberAddressNotFound() {
        var dictionary = mock(DictionaryService.class);
        when(dictionary.getSberCity(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service(dictionary).getConvertedBookV2(addressBook()))
                .isInstanceOf(SberAddressNotFoundException.class);
    }

    @Test
    void unexpectedError_wrappedInConvertProcessingException() {
        var dictionary = mock(DictionaryService.class);
        var address = new SberAddressDictionary();
        address.setCity("Москва");
        when(dictionary.getSberCity(any())).thenReturn(Optional.of(address));
        assertThatThrownBy(() -> service(dictionary).getConvertedBookV2(addressBook()))
                .isInstanceOf(ConvertProcessingException.class);
    }

    @Test
    void privateHelpers() {
        var dictionary = mock(DictionaryService.class);
        var service = service(dictionary);

        assertThat((String) ReflectionTestUtils.invokeMethod(service, "prepareFio", new Object[]{null})).isEqualTo("");

        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("s");
        var row = sheet.createRow(0);
        row.createCell(0).setCellValue("noComma");
        row.createCell(1).setCellValue("10,20");
        row.createCell(16).setCellValue(new Date());
        ReflectionTestUtils.setField(service, "sheet", sheet);

        assertThat((Date) ReflectionTestUtils.invokeMethod(service, "getDateFromFile", 0)).isNotNull();
        assertThat((Integer) ReflectionTestUtils.invokeMethod(service, "getTemperage2", 0, 0, 0)).isNull();
        assertThat((Integer) ReflectionTestUtils.invokeMethod(service, "getTemperage2", 0, 1, 1)).isEqualTo(20);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(service, "getTemperage", 0, 0, 0))
                .isInstanceOf(TemperatureNodValidException.class);
    }
}
