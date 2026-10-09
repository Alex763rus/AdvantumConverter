package com.example.advantumconverter.service.database;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.support.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSiel.SIEL_COMPANY_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DictionaryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private DictionaryService dictionaryService;

    @Test
    void reloadDictionary_doesNotThrow() {
        dictionaryService.reloadDictionary();
    }

    @Test
    void lookups_returnEmptyForUnknownKeys() {
        assertThatThrownBy(() -> dictionaryService.getCarOrElseThrow("no-such-car"))
                .isInstanceOf(CarNotFoundException.class);

        var fallback = new Car();
        assertThat(dictionaryService.getCarOrElse("no-such-car", fallback)).isSameAs(fallback);

        assertThat(dictionaryService.getCarNumber("x")).isEmpty();
        assertThat(dictionaryService.getLentaCar("x")).isEmpty();
        assertThat(dictionaryService.getSielPoint("x")).isEmpty();
        assertThat(dictionaryService.getSielMilkPoint("x")).isEmpty();
        assertThat(dictionaryService.getSparWindows("x")).isEmpty();
        assertThat(dictionaryService.getLentaDictionaries(0L)).isNull();

        assertThat(dictionaryService.getSielCarrierName("no-such-car")).isEqualTo(SIEL_COMPANY_NAME);
        assertThat(dictionaryService.getBestDictionary("no-such-stock", 10)).isNull();
        assertThat(dictionaryService.getOzonTonnageTime(0L)).isEmpty();
        assertThat(dictionaryService.getOzonTransitTime("a", "b")).isNull();
        assertThat(dictionaryService.getOzonLoadUnloadTime("a")).isEmpty();

        assertThat(dictionaryService.getTsCityBrief("x")).isNull();
        assertThat(dictionaryService.getMetroMinTemperature("x")).isNull();
        assertThat(dictionaryService.getMetroMaxTemperature("x")).isNull();
        assertThat(dictionaryService.getMetroTimeStart(0L, Set.of("x"))).isNull();
        assertThat(dictionaryService.getMetroTimeEnd(0L, Set.of("x"))).isNull();
        assertThat(dictionaryService.getSberCity("x")).isEmpty();
        assertThat(dictionaryService.getMetroTimeDictionary("x")).isNull();
        assertThat(dictionaryService.getMetroDcAddressName("x", "def")).isEqualTo("def");
        assertThat(dictionaryService.getMetroDcAddressBrief("x", "def")).isEqualTo("def");
    }
}
