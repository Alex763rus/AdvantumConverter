package com.example.advantumconverter.enums;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumsAndDefaultsTest {

    @Test
    void enumGetters() {
        for (FileType type : FileType.values()) {
            assertThat(type.getFolderName()).isNotNull();
        }
        for (ResultCode code : ResultCode.values()) {
            assertThat(code.getTitle()).isNotNull();
        }
        for (ExcelType type : ExcelType.values()) {
            assertThat(type).isNotNull();
        }
    }

    @Test
    void convertServiceDefaults() {
        var service = new ConvertService() {
            @Override
            public String getConverterName() {
                return "name";
            }

            @Override
            public String getConverterCommand() {
                return "cmd";
            }

            @Override
            public ExcelType getExcelType() {
                return ExcelType.CLIENT;
            }
        };
        assertThat(service.getConvertedBook(new XSSFWorkbook())).isNull();
        assertThat(service.getConvertedBookV2(new XSSFWorkbook())).isNull();
        assertThat(service.getCrmCreds()).isNull();
        assertThat(service.converterSettings()).isNull();
        assertThat(service.isV2()).isFalse();
    }

    @Test
    void excelGenerateServiceDefaults() {
        var service = new ExcelGenerateService() {
        };
        assertThatThrownBy(() -> service.createXlsx(null)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> service.createXlsxV2((ConvertedBookV2) null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void companySetting_accessors() {
        var setting = new CompanySetting(new java.util.HashMap<>());
        setting.setCompanyConverter(null);
        assertThat(setting.getCompanyConverter()).isNull();
        assertThat(setting.toString()).isNotNull();
        assertThat(new CompanySetting()).isNotNull();
    }
}
