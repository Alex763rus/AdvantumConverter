package com.example.advantumconverter.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsTest {

    @Test
    void carNotFound() {
        assertThat(new CarNotFoundException("VIN").getMessage()).endsWith("VIN");
    }

    @Test
    void convertProcessing() {
        assertThat(new ConvertProcessingException("boom").getMessage()).endsWith("boom");
        assertThat(ConvertProcessingException.of("value=%s", "x").getMessage()).endsWith("value=x");
    }

    @Test
    void dictionary() {
        assertThat(new DictionaryException("dict").getMessage()).endsWith("dict");
    }

    @Test
    void excelGeneration() {
        assertThat(new ExcelGenerationException("gen").getMessage()).endsWith("gen");
    }

    @Test
    void excelListNotFound() {
        assertThat(new ExcelListNotFoundException("Sheet1").getMessage()).contains("Sheet1");
    }

    @Test
    void excelValidation() {
        var message = new ExcelValidationException(3, 5).getMessage();
        assertThat(message).contains("[3]").contains("[5]");
    }

    @Test
    void sberAddressNotFound() {
        assertThat(new SberAddressNotFoundException().getMessage()).isNotNull();
    }

    @Test
    void temperatureNotValid() {
        assertThat(new TemperatureNodValidException("7").getMessage()).endsWith("7");
    }

    @Test
    void webConvertProcessing() {
        var ex = new WebConvertProcessingException(HttpStatus.BAD_REQUEST, "bad");
        assertThat(ex.getMessage()).endsWith("bad");
        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
