package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.model.jpa.lenta.LentaCar;
import com.example.advantumconverter.model.jpa.lenta.LentaDictionary;
import com.example.advantumconverter.service.database.DictionaryService;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplLenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.WorkbookBuilder.build;
import static utils.WorkbookBuilder.row;

class ConvertServiceImplLentaBranchTest {

    private final DictionaryService dictionaryService = mock(DictionaryService.class);
    private final ConvertServiceImplLenta converter = new ConvertServiceImplLenta();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(converter, "dictionaryService", dictionaryService);

        var car = new Car();
        car.setCarName("CAR12345");
        car.setTonnage(5000);
        car.setTemperatureMin(-20);
        car.setTemperatureMax(-10);

        var lentaCar = new LentaCar();
        lentaCar.setCarNumber("AA222");
        lentaCar.setTonnage(3000);

        var dictionary = new LentaDictionary();
        dictionary.setRegion("REG");
        dictionary.setTimeShop("20:00");
        dictionary.setTimeStock("10:00");
        dictionary.setAddressName("ADDR");

        when(dictionaryService.getLentaDictionaries(anyLong())).thenReturn(dictionary);
        when(dictionaryService.getCarOrElse(eq("CAR12345"), any())).thenReturn(car);
        when(dictionaryService.getCarOrElseThrow("CAR12345")).thenReturn(car);
        when(dictionaryService.getLentaCar("AA111")).thenReturn(Optional.empty());
        when(dictionaryService.getLentaCar("AA222")).thenReturn(Optional.of(lentaCar));
        when(dictionaryService.getLentaCar("AA333")).thenReturn(Optional.empty());
        when(dictionaryService.getCarNumber(anyString())).thenReturn(Optional.empty());
        when(dictionaryService.getTsCityBrief("AA111")).thenReturn(null);
        when(dictionaryService.getTsCityBrief("AA222")).thenReturn("CityBrief");
    }

    @Test
    void getConvertedBookV2_dictionaryBranches() {
        var workbook = build("Sheet1", List.<String[]>of(
                row(0, "ТК/РЦ"),
                row(0, "123-1", 5, "A", 7, "CAR12345", 9, "AA111", 13, "ЛЕНТА", 14, "15.01.2025 10:00"),
                row(0, "124-2", 5, "B", 7, "CAR12345", 9, "AA222", 13, "ЛЕНТА", 14, "16.01.2025 10:00"),
                row(0, "125-3", 5, "B", 7, "1,5", 9, "AA333", 13, "ЛЕНТА", 14, "17.01.2025 10:00")
        ));

        var result = converter.getConvertedBookV2(workbook);

        assertThat(result).isNotNull();
        assertThat(result.getBookV2().get(0).getExcelListContentV2()).hasSize(3);
    }

    @Test
    void simpleGetters() {
        var crmConfig = mock(com.example.advantumconverter.config.properties.CrmConfigProperties.class);
        ReflectionTestUtils.setField(converter, "crmConfigProperties", crmConfig);
        assertThat(converter.getExcelType()).isEqualTo(com.example.advantumconverter.enums.ExcelType.CLIENT);
        assertThat(converter.getConverterName()).isNotEmpty();
        assertThat(converter.getConverterCommand()).isNotEmpty();
        assertThat(converter.isV2()).isTrue();
        assertThat(converter.getCrmCreds()).isNull();
    }

    @Test
    void getConvertedBookV2_nullDictionaryBranches() {
        when(dictionaryService.getLentaDictionaries(anyLong())).thenReturn(null);
        var workbook = build("Sheet1", List.<String[]>of(
                row(0, "ТК/РЦ"),
                row(0, "123-1", 5, "A", 7, "CAR12345", 9, "AA111", 13, "ЛЕНТА", 14, "15.01.2025 10:00"),
                row(0, "124-2", 5, "B", 7, "CAR12345", 9, "AA222", 13, "ЛЕНТА", 14, "16.01.2025 10:00")
        ));
        var result = converter.getConvertedBookV2(workbook);
        assertThat(result).isNotNull();
    }

    @Test
    void getConvertedBookV2_invalidCodeRow() {
        var workbook = build("Sheet1", List.<String[]>of(
                row(0, "ТК/РЦ"),
                row(0, "123-1", 5, "A", 7, "CAR12345", 9, "AA111", 13, "ЛЕНТА", 14, "15.01.2025 10:00"),
                row(0, "abc", 5, "B", 7, "CAR12345", 9, "AA222", 13, "ЛЕНТА", 14, "16.01.2025 10:00")
        ));
        var result = converter.getConvertedBookV2(workbook);
        assertThat(result.getMessage()).contains("некорректный код");
    }

    @Test
    void getConvertedBookV2_dateParseException() {
        var workbook = build("Sheet1", List.<String[]>of(
                row(0, "ТК/РЦ"),
                row(0, "123-1", 5, "A", 7, "CAR12345", 9, "AA111", 13, "ЛЕНТА", 14, "not-a-date")
        ));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> converter.getConvertedBookV2(workbook))
                .isInstanceOf(com.example.advantumconverter.exception.ConvertProcessingException.class);
    }
}
