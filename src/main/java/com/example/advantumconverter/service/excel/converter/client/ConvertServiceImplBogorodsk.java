package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.ConverterProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BOGORODSK;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOGORODSK;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplBogorodsk extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;

    private int LAST_ROW;

    @Override
    public String getConverterName() {
        return FILE_NAME_BOGORODSK;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_BOGORODSK;
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
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                for (int copy = 1; copy < 4; ++copy) {
                    dataLine = new ArrayList<>();
                    dataLine.add(getCellValue(row, 1));
                    dataLine.add(convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
                    dataLine.add("Х5 Богородск");
                    dataLine.add(BUSH_AUTOPROM_ORGANIZATION_NAME);
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 4));
                    dataLine.add(getCellValue(row, 5));
                    dataLine.add("2");
                    dataLine.add("4");
                    dataLine.add("6");
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(row, copy));
                    dataLine.add(fillT(row, copy));
                    dataLine.add(fillU(row, copy));
                    dataLine.add(fillU(row, copy));
                    dataLine.add(copy == 1 ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(copy));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 14));
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 16));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

                    data.add(dataLine);
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBook(getConverterName() + UNDERSCORE, EXPORT, data, DONE);
    }

    private Date getTimeForS(int row, int copy) {
        int column = switch (copy) {
            case 1 -> 3;
            case 2 -> 20;
            case 3 -> 22;
            default -> 0;
        };
        return getCellDate(row, column);
    }

    private String fillS(int row, int copy) throws ParseException {
        val date = convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT) + SPACE;
        val time = getTimeForS(row, copy);
        return date + convertDateFormat(time, TEMPLATE_TIME);
    }

    private String fillT(int row, int copy) throws ParseException {
        val date = convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT) + SPACE;
        val time = DateUtils.addHours(getTimeForS(row, copy), 2);
        return date + convertDateFormat(time, TEMPLATE_TIME);
    }

    private String fillU(int row, int copy) {
        int column = switch (copy) {
            case 1 -> 13;
            case 2 -> 21;
            case 3 -> 23;
            default -> 0;
        };
        return getCellValue(row, column);
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public ConverterProperties.ConverterSettings converterSettings() {
        return converterProperties.getBogorodsk();
    }
}
