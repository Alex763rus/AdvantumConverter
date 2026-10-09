package com.example.advantumconverter.service.excel.converter.rs;

import org.junit.jupiter.api.Test;

import static com.example.advantumconverter.constant.Constant.Converter.LOAD_THE_GOODS;
import static com.example.advantumconverter.constant.Constant.Converter.RETURN_CONTAINERS;
import static com.example.advantumconverter.constant.Constant.Converter.UNLOAD_THE_GOODS;
import static org.assertj.core.api.Assertions.assertThat;

class ConvertServiceImplRsLentaYrTest {

    private final ConvertServiceImplRsLentaYr service = new ConvertServiceImplRsLentaYr();

    @Test
    void calculatePointType() {
        assertThat(service.calculatePointType(RETURN_CONTAINERS, 0)).isEqualTo("D");
        assertThat(service.calculatePointType(LOAD_THE_GOODS, 0)).isEqualTo("P");
        assertThat(service.calculatePointType(UNLOAD_THE_GOODS, 1)).isEqualTo("PD");
        assertThat(service.calculatePointType(UNLOAD_THE_GOODS, 0)).isEqualTo("D");
        assertThat(service.calculatePointType("unknown", null)).isEmpty();
    }

    @Test
    void metadata() {
        assertThat(service.getConverterName()).isNotBlank();
        assertThat(service.getConverterCommand()).isNotBlank();
        assertThat(service.getExcelType()).isNotNull();
        assertThat(service.isV2()).isTrue();
    }
}
