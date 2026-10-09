package com.example.advantumconverter.service.database;

import com.example.advantumconverter.model.jpa.ozon.OzonDictionary;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

class DictionaryServiceBranchTest {

    private DictionaryService service() {
        return mock(DictionaryService.class, CALLS_REAL_METHODS);
    }

    @Test
    void getBestDictionary_returnsWindow() {
        var service = service();
        var dictionary = new OzonDictionary();
        dictionary.setStockBrief("S");
        dictionary.setStockInTime("10:00");
        dictionary.setStockOutTime("14:00");
        ReflectionTestUtils.setField(service, "ozonDictionaries", Set.of(dictionary));

        assertThat(service.getBestDictionary("S", 12)).isEqualTo(dictionary);
    }

    @Test
    void getHour_parsesAndThrows() {
        var service = service();
        assertThat((Integer) ReflectionTestUtils.invokeMethod(service, "getHour", "12:00")).isEqualTo(12);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(service, "getHour", "not-a-time"))
                .isInstanceOf(RuntimeException.class);
    }
}
