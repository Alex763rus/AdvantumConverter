package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.support.AbstractConverterIntegrationTest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterOverridesTest extends AbstractConverterIntegrationTest {

    @Autowired
    private List<ConvertService> converters;

    @Test
    void allConverters_overrideMetadata() {
        assertThat(converters).isNotEmpty();
        for (ConvertService converter : converters) {
            assertThat(converter.getConverterName()).isNotNull();
            assertThat(converter.getConverterCommand()).isNotNull();
            assertThat(converter.getExcelType()).isNotNull();
            converter.isV2();
            converter.getCrmCreds();
            converter.converterSettings();
        }
    }
}
