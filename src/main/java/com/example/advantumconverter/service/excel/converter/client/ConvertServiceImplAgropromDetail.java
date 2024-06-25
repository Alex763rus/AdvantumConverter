package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_AGROPROM_DETAIL;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_AGROPROM_DETAIL;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static com.example.advantumconverter.constant.Constant.Heap.*;

@Component
public class ConvertServiceImplAgropromDetail extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 4;
    private int LAST_ROW;
    private final String COMPANY_NAME = "Х5 ТЦ Новая Рига";

    @Override
    public String getConverterName() {
        return FILE_NAME_AGROPROM_DETAIL;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_AGROPROM_DETAIL;
    }

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            val dateB = convertDateFormat(getCellValue(row, 15), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_DOT);
            for (; row <= LAST_ROW; ++row) {
                dataLine = new ArrayList<String>();
                dataLine.add(getCellValue(row, 1));
                dataLine.add(dateB);
                dataLine.add(COMPANY_NAME);
                dataLine.add(BUSH_AUTOPROM_ORGANIZATION_NAME);
                dataLine.add(EMPTY);
                dataLine.add(REFRIGERATOR);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add("10");
                dataLine.add("16");
                dataLine.add(getCellValue(row, 30));
                dataLine.add(getCellValue(row, 31));
                dataLine.add("2");
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(convertDateFormat(getCellValue(row, 15), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT));
                dataLine.add(convertDateFormat(getCellValue(row, 18), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT));
                dataLine.add(getCellValue(row, 11));
                dataLine.add(getCellValue(row, 12));
                dataLine.add(getCellValue(row, 20).equals("1") ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                dataLine.add(getCellValue(row, 20));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(getCellValue(row, 4));
                dataLine.add(EMPTY);
                dataLine.add(getCellValue(row, 6));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);

                data.add(dataLine);
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBook(getConverterName() + UNDERSCORE,EXPORT, data, DONE);
    }

    @Override
    public ExcelType getExcelType() {
        return ExcelType.CLIENT;
    }
}
