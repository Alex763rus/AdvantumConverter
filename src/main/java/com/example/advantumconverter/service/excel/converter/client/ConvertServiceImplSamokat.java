package com.example.advantumconverter.service.excel.converter.client;

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
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SAMOKAT;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SAMOKAT;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static com.example.advantumconverter.constant.Constant.Heap.*;

@Component
public class ConvertServiceImplSamokat extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;

    private int LAST_ROW;

    @Override
    public String getConverterName() {
        return FILE_NAME_SAMOKAT;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SAMOKAT;
    }


    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        int mainRow = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                boolean isStart = isStart(row);
                if (isStart) {
                    mainRow = row;
                    counterCopy = 1;
                }
                for (int copy = 1; copy <= 2; ++copy) {
                    dataLine = new ArrayList<>();
                    dataLine.add(fillA(mainRow));
                    dataLine.add(getCurrentDate(TEMPLATE_DATE_DOT));
                    dataLine.add("Самокат");
                    dataLine.add(BUSH_AUTOPROM_ORGANIZATION_NAME);
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add("5");
                    dataLine.add("10");
                    dataLine.add("2");
                    dataLine.add("4");
                    dataLine.add("6");
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(row, isStart));
                    dataLine.add(fillT(row, isStart));
                    dataLine.add(fillU(row, isStart));
                    dataLine.add(fillV(row, isStart));
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(counterCopy));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(mainRow, 3).replaceAll(SPACE, EMPTY));
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(mainRow, 4));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

                    ++counterCopy;
                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    } else {
                        isStart = false;
                    }
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBook(getConverterName() + UNDERSCORE, EXPORT, data, DONE);
    }

    private String fillU(int row, boolean isStart) {
        return isStart ? "Склад FMCG МСК" : getCellValue(row, 10);
    }

    private String fillV(int row, boolean isStart) {
        val textStart = "Московская область, Пушкинский район, г.п. Софрино, 48 км Ярославского шоссе, владение 1";
        return isStart ? textStart : getCellValue(row, 10);
    }

    private String fillA(int row) throws ParseException {
        return getCellValue(row, 1) + getCurrentDate(TEMPLATE_DATE);
    }

    private String fillS(int row, boolean isStart) throws ParseException {
        val date = getCurrentDate(TEMPLATE_DATE_DOT);
        val timeA = getCellDate(row, 0);
        val timeResult = isStart ? DateUtils.addHours(timeA, -2) : timeA;
        return date + SPACE + convertDateFormat(timeResult, TEMPLATE_TIME);
    }

    private String fillT(int row, boolean isStart) throws ParseException {
        val date = getCurrentDate(TEMPLATE_DATE_DOT);
        val timeA = getCellDate(row, 0);
        val timeResult = isStart ? DateUtils.addHours(timeA, 1) : DateUtils.addHours(timeA, 4);
        return date + SPACE + convertDateFormat(timeResult, TEMPLATE_TIME);
    }

    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > LAST_ROW) {
            return EMPTY;
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
            return EMPTY;
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 1);
        val prev1 = getValueOrDefault(row, -1, 1);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(EMPTY));
    }
}
