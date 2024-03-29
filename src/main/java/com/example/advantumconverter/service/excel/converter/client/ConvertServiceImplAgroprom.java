package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_AGROPROM;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.ExcelType.CLIENT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_AGROPROM;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplAgroprom extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;

    @Override
    public String getConverterName() {
        return FILE_NAME_AGROPROM;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_AGROPROM;
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = new ArrayList<String>();
                    dataLine.add(getCellValue(row, 0));
                    dataLine.add(convertDateFormat(getCellValue(row, 9), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
                    dataLine.add("Х5 ТЦ Новая Рига");
                    dataLine.add(BUSH_AUTOPROM_ORGANIZATION_NAME);
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 5));
                    dataLine.add(getCellValue(row, 6));
                    dataLine.add("3");
                    dataLine.add("5");
                    dataLine.add("3");
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(iRepeat == 0, row));
                    dataLine.add(fillT(iRepeat == 0, row));
                    dataLine.add(getCellValue(row, 8));
                    dataLine.add(getCellValue(row, 8));
                    dataLine.add(iRepeat == 0 ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(iRepeat));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 2));
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 3));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

                    data.add(dataLine);
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException("не удалось обработать строку:" + row
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    @Override
    public String getFileNamePrefix() {
        return getConverterName() + "_";
    }

    @Override
    public String getExcelType() {
        return CLIENT;
    }

    private String fillS(boolean isStart, int row) throws ParseException {
        if(isStart){
            val timeK = convertDateFormat(getCellValue(row, 10), TEMPLATE_TIME, TEMPLATE_TIME_SECOND);
            val dateResult = getCellValue(row, 9) + SPACE + (isStart ? timeK : "TODO");
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_SLASH, TEMPLATE_DATE_TIME_DOT);
        } else{
            val dateText = fillT(isStart, row);
            val date = convertDateFormat(dateText, TEMPLATE_DATE_TIME_DOT);
            return convertDateFormat(DateUtils.addHours(date, -2), TEMPLATE_DATE_TIME_DOT);
        }
    }

    private String fillT(boolean isStart, int row) throws ParseException {
        if(isStart){
            val dateText = fillS(isStart, row);
            val date = convertDateFormat(dateText, TEMPLATE_DATE_TIME_DOT);
            return convertDateFormat(DateUtils.addHours(date, 2), TEMPLATE_DATE_TIME_DOT);
        } else{
            val timeN = convertDateFormat(getCellValue(row, 13), TEMPLATE_TIME, TEMPLATE_TIME_SECOND);
            val dateResult = getCellValue(row, 12) + SPACE + timeN;
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_SLASH, TEMPLATE_DATE_TIME_DOT);
        }
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

}
