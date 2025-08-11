package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.config.DatabaseTestConfig;
import com.example.advantumconverter.config.DatabaseTestConfiguration;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplArtFruit;
import lombok.val;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Heap.DONE;
import static com.example.advantumconverter.constant.Constant.Heap.EXPORT;
import static constant.TestConstant.TestFileIn.EXCEL_ART_FRUIT_IN;
import static constant.TestConstant.TestFileOut.EXCEL_ART_FRUIT_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static org.example.tgcommons.utils.NumberConverter.convertToIntegerOrNull;
import static utils.ExcelReader.read;

@SpringBootTest(classes = {DatabaseTestConfig.class})
@EnableConfigurationProperties
public class ConvertServiceImplArtFruitTest {

    @Autowired
    private ConvertServiceImplArtFruit convertServiceImplArtFruit;

    private void baseTest(String fileNameIn, String fileNameOut, ConvertService converter, String expectedMessage) throws IOException, ParseException {
        var fileIn = read(fileNameIn);
        var actualResult = converter.getConvertedBookV2(fileIn);
        var expectedResult = prepareExpectedBook(fileNameOut, expectedMessage);
        assertThat(actualResult)
                .usingRecursiveComparison()
                .ignoringFields("bookName")
                .isEqualTo(expectedResult);

        int i = 0;
    }

    private ConvertedBookV2 prepareExpectedBook(String fileNameOut, String expectedMessage) throws IOException, ParseException {
        var book = read(fileNameOut);
        var sheet = book.getSheetAt(0);
        var lastRow = getLastRow(sheet);
//        var lastColNumber = sheet.getRow(lastRow).getLastCellNum() - 1;
        val data = new ArrayList<ConvertedListDataV2>();
        int row = 0;
        ConvertedListDataV2 dataLine = null;
        for (; row <= lastRow; ++row) {
            dataLine =
                    ConvertedListDataV2.init()
                            .setColumnAdata(getCellValue(sheet, row, 0))
                            .setColumnBdata(convertDateFormat(getCellValue(sheet, row, 1), TEMPLATE_DATE_DOT))
                            .setColumnCdata(getCellValue(sheet, row, 2))
                            .setColumnDdata(getCellValue(sheet, row, 3))
                            .setColumnEdata(convertToIntegerOrNull(getCellValue(sheet, row, 4)))
                            .setColumnFdata(getCellValue(sheet, row, 5))
                            .setColumnGdata(getCellValue(sheet, row, 6))
                            .setColumnHdata(getCellValue(sheet, row, 7))
                            .setColumnIdata(convertToIntegerOrNull(getCellValue(sheet, row, 8)))
                            .setColumnJdata(convertToIntegerOrNull(getCellValue(sheet, row, 9)))
                            .setColumnKdata(convertToIntegerOrNull(getCellValue(sheet, row, 10)))
                            .setColumnLdata(convertToIntegerOrNull(getCellValue(sheet, row, 11)))
                            .setColumnMdata(convertToIntegerOrNull(getCellValue(sheet, row, 12)))
                            .setColumnNdata(convertToIntegerOrNull(getCellValue(sheet, row, 13)))
                            .setColumnOdata(convertToIntegerOrNull(getCellValue(sheet, row, 14)))
                            .setColumnPdata(convertToIntegerOrNull(getCellValue(sheet, row, 15)))
                            .setColumnQdata(convertToIntegerOrNull(getCellValue(sheet, row, 16)))
                            .setColumnRdata(convertToIntegerOrNull(getCellValue(sheet, row, 17)))
                            .setColumnSdata(getCellValue(sheet, row, 18).equals(EMPTY) ? null :
                                    convertDateFormat(getCellValue(sheet, row, 18), TEMPLATE_DATE_TIME_DOT))
                            .setColumnTdata(getCellValue(sheet, row, 19).equals(EMPTY) ? null :
                                    convertDateFormat(getCellValue(sheet, row, 19), TEMPLATE_DATE_TIME_DOT))
                            .setColumnUdata(getCellValue(sheet, row, 20))
                            .setColumnVdata(getCellValue(sheet, row, 21))
                            .setColumnWdata(getCellValue(sheet, row, 22))
                            .setColumnXdata(convertToIntegerOrNull(getCellValue(sheet, row, 23)))
                            .setColumnYdata(getNumericCellValue(sheet, row, 24))
                            .setColumnZdata(getNumericCellValue(sheet, row, 25))
                            .setColumnAaData(getCellValue(sheet, row, 26))
                            .setColumnAbData(getCellValue(sheet, row, 27))
                            .setColumnAcData(getCellValue(sheet, row, 28))
                            .setColumnAdData(getCellValue(sheet, row, 29))
                            .setColumnAeData(getCellValue(sheet, row, 30))
                            .setColumnAfData(convertToIntegerOrNull(getCellValue(sheet, row, 31)))
                            .setColumnAgData(getCellValue(sheet, row, 32))
                            .setColumnAhData(getCellValue(sheet, row, 33))
//                            .setColumnAiData(convertServiceImplArtFruit.getIntegerValue(row, 34))
                            .setColumnAjData(getCellValue(sheet, row, 35))
                            .setColumnAkData(getCellValue(sheet, row, 36))
                            .setColumnAlData(getCellValue(sheet, row, 37))
                            .setColumnAmData(getCellValue(sheet, row, 38))
                            .setColumnAnData(getCellValue(sheet, row, 39))
                            .build();
            data.add(dataLine);
        }
        return ConvertedBookV2.init()
                .setBookName(EMPTY)
                .setBookV2(List.of(
                        ConvertedListV2.init()
                                .setExcelListName(EXPORT)
                                .setHeadersV2(Header.headersOutputClientV2)
                                .setExcelListContentV2(data)
                                .build()))
                .setMessage(expectedMessage)
                .build();
    }

    @Test
    public void testConvert() throws IOException, ParseException {
        baseTest(EXCEL_ART_FRUIT_IN, EXCEL_ART_FRUIT_OUT, convertServiceImplArtFruit, DONE);
    }


    private int getLastRow(XSSFSheet sheet) {
        int i = 0;
        for (; ; i++) {
            if (getCellValue(sheet, i, 0).equals(EMPTY)) {
                if (getCellValue(sheet, i + 1, 0).equals(EMPTY)) {
                    break;
                }
            }
        }
        return i - 1;
    }

    private String getCellValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return EMPTY;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return EMPTY;
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }

    private Double getNumericCellValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return null;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return null;
        }
        return getNumericCellValue(sheet.getRow(row).getCell(col));
    }

    private Double getNumericCellValue(XSSFCell xssfCell) {
        if (getCellValue(xssfCell).equals(EMPTY)) {
            return null;
        }
        return xssfCell.getNumericCellValue();
    }

    protected Double getDoubleValue(XSSFSheet sheet, int row, int col) {
        val cellValue = getCellValue(sheet, row, col);
        return cellValue.equals(EMPTY) ? null : Double.parseDouble(cellValue);
    }
}