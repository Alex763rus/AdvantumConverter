package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_OZON;
import static com.example.advantumconverter.constant.Constant.Converter.REFRIGERATOR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_OZON;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplOzon extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;
    private int LAST_ROW;

    @Override
    public String getFileNamePrefix() {
        return getConverterName() + "_";
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_OZON;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_OZON;
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        sheet = book.getSheetAt(0);
        int row = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                dataLine = new ArrayList<>();
                dataLine.add(getCellValue(row, 0));
                dataLine.add(convertDateFormat(getCellValue(row, 1), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
                dataLine.add(getCellValue(row, 2));
                dataLine.add(getCellValue(row, 3));
                dataLine.add(EMPTY);
                dataLine.add(REFRIGERATOR);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(fillJ(row));
                dataLine.add(getCellValue(row, 10));
                dataLine.add(getCellValue(row, 11));
                dataLine.add(getCellValue(row, 12));
                dataLine.add(getCellValue(row, 13));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(fillS(row));
                dataLine.add(fillT(row));
                dataLine.add(getCellValue(row, 20));
                dataLine.add(getCellValue(row, 21));
                dataLine.add(getCellValue(row, 22));
                dataLine.add(getCellValue(row, 23));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(getCellValue(row, 28));
                dataLine.add(EMPTY);
                dataLine.add(getCellValue(row, 30));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);

                ++counterCopy;
                data.add(dataLine);
            }
        } catch (Exception e) {
            throw new ConvertProcessingException("не удалось обработать строку:" + row
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private String fillS(int row) throws ParseException {
        val dateS = convertDateFormat(getCellValue(row, 18), TEMPLATE_DATE_TIME_SLASH);
        if ("0".equals(getCellValue(row, 34))) {
            return convertDateFormat(dateS, TEMPLATE_DATE_TIME_DOT);
        }
        val stockBrief = getCellValue(row, 20);
        val hTime = dateS.getHours();
        val ozonDictionary = dictionaryService.getBestDictionary(stockBrief, hTime);
        if (ozonDictionary == null) {
            return convertDateFormat(dateS, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(dateS, TEMPLATE_DATE_DOT);
        val dateTimeString = date + SPACE + ozonDictionary.getStockInTime();
        return dateTimeString;
    }

    private String fillT(int row) throws ParseException {
        val dateT = convertDateFormat(getCellValue(row, 19), TEMPLATE_DATE_TIME_SLASH);
        val dateS = convertDateFormat(getCellValue(row, 18), TEMPLATE_DATE_TIME_SLASH);
        if ("0".equals(getCellValue(row, 34))) {
            return convertDateFormat(dateT, TEMPLATE_DATE_TIME_DOT);
        }
        val stockBrief = getCellValue(row, 20);
        val hTime = dateS.getHours();
        val ozonDictionary = dictionaryService.getBestDictionary(stockBrief, hTime);
        if (ozonDictionary == null) {
            return convertDateFormat(dateT, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(dateT, TEMPLATE_DATE_DOT);
        val dateTimeString = date + SPACE + prepareStockOutTime(ozonDictionary.getStockOutTime());
        return dateTimeString;
    }

    private String prepareStockOutTime(String time) {
        return time.equals("00:00") ? "23:59" : time;
    }

    private String fillJ(int row) {
        val doubleValue = Double.parseDouble(getCellValue(row, 9).replaceAll(",", ".")) * 1000;
        return String.valueOf((int) doubleValue);
    }

}
