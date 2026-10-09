package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsLenta;
import com.example.advantumconverter.service.excel.generate.RsExcelGenerateService;
import com.example.advantumconverter.support.AbstractConverterIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static com.example.advantumconverter.constant.Constant.Heap.DONE;
import static constant.TestConstant.TestFileIn.EXCEL_RS_LENTA_IN;
import static constant.TestConstant.TestFileOut.EXCEL_RS_LENTA_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.ExcelReader.read;

public class ConvertServiceImplRsLentaTest extends AbstractConverterIntegrationTest {

    @Autowired
    private ConvertServiceImplRsLenta convertServiceImplRsLenta;

    @Autowired
    private RsExcelGenerateService rsExcelGenerateService;

    @Test
    public void testConvert() throws IOException {
        var fileIn = read(EXCEL_RS_LENTA_IN);
        ConvertedBookV2 result = convertServiceImplRsLenta.getConvertedBookV2(fileIn);
        assertThat(result.getMessage()).startsWith(DONE);

        assertMatchesGolden(rsExcelGenerateService, result, EXCEL_RS_LENTA_OUT);
    }
}
