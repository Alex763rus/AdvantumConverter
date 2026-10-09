package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSielMilk;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import com.example.advantumconverter.support.AbstractConverterIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static com.example.advantumconverter.constant.Constant.Heap.DONE;
import static constant.TestConstant.TestFileIn.EXCEL_SIEL_MILK_IN;
import static constant.TestConstant.TestFileOut.EXCEL_SIEL_MILK_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.ExcelReader.read;

public class ConvertServiceImplSielMilkTest extends AbstractConverterIntegrationTest {

    @Autowired
    private ConvertServiceImplSielMilk convertServiceImplSielMilk;

    @Autowired
    private ClientExcelGenerateService clientExcelGenerateService;

    @Test
    public void testConvert() throws IOException {
        var fileIn = read(EXCEL_SIEL_MILK_IN);
        ConvertedBookV2 result = convertServiceImplSielMilk.getConvertedBookV2(fileIn);
        assertThat(result.getMessage()).startsWith(DONE);

        assertMatchesGolden(clientExcelGenerateService, result, EXCEL_SIEL_MILK_OUT);
    }
}
