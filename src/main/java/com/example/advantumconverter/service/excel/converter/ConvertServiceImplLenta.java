package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import lombok.val;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_LENTA;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_LENTA;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplLenta extends ConvertServiceBase implements ConvertService {
    private int START_ROW;

    private int LAST_ROW;

    @Override
    public String getFileNamePrefix() {
        return getConverterName() + "_";
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_LENTA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_LENTA;
    }

    private String START_ROW_TEXT = "ТК/РЦ";


    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        sheet = book.getSheetAt(0);
        START_ROW = getStartRow(START_ROW_TEXT);

        int row = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {

            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val isStart = isStart(row);
                if (isStart) {
                    counterCopy = 1;
                }
                dataLine = new ArrayList<>();
                dataLine.add(getCellValue(row, 1));
                dataLine.add(getCurrentDate(TEMPLATE_DATE_DOT));
                dataLine.add(fillC(row));
                dataLine.add(fillD(row));
                dataLine.add(EMPTY);
                dataLine.add(REFRIGERATOR);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(fillJ(row));
                dataLine.add("1");
                dataLine.add(fillL(row));
                dataLine.add(fillM(row));
                dataLine.add("2");
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(fillS(row, isStart));
                dataLine.add(fillT(row, isStart));
                dataLine.add(String.valueOf(getCode(row)));
                dataLine.add(fillU(row));
                dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                dataLine.add(String.valueOf(counterCopy));
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(EMPTY);
                dataLine.add(getCarNumber(row));
                dataLine.add(EMPTY);
                dataLine.add(fillAE(row));
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

    private String getCarNumber(int row) {
        return getCellValue(row, 5).replaceAll(SPACE, EMPTY);
    }

    private String fillD(int row) {
        val companyName = getCellValue(row, 8);
        if (companyName.equals(COMPANY_OOO_LENTA)
                && (dictionaryService.getCarNumberOrElse(getCarNumber(row), null) == null)) {
            return COMPANY_OOO_LENTA_HIRING;
        }
        return companyName;
    }

    private String fillL(int row) {
        val carName = getCellValue(row, 3).replaceAll("\\\\", EMPTY);
        val car = dictionaryService.getCarOrElse(carName, null);
        if (carName.length() < 5 || car == null) {
            return "2";
        }
        return String.valueOf(car.getTemperatureMin());
    }

    private String fillM(int row) {
        val carName = getCellValue(row, 3).replaceAll("\\\\", EMPTY);
        val car = dictionaryService.getCarOrElse(carName, null);
        if (carName.length() < 5 || car == null) {
            return "6";
        }
        return String.valueOf(car.getTemperatureMax());
    }

    private String fillJ(int row) {
        val carName = getCellValue(row, 3).replaceAll("\\\\", EMPTY);
        if (carName.length() < 5) {
            val doubleValue = Double.parseDouble(carName.replaceAll(",", ".")) * 1000;
            return String.valueOf((int) doubleValue);
        }
        val car = dictionaryService.getCar(carName);
        return String.valueOf(car.getTonnage());
    }

    private String fillC(int row) {
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        return lentaDictionary == null ? "Нет региона" : "Лента (" + lentaDictionary.getRegion() + ")";
    }

    private String fillAE(int row) {
        return WordUtils.capitalize(getCellValue(row, 2).toLowerCase());
    }


    private Date getExpectedTimeIncome(int row) throws ParseException {
        val date = getCellValue(row, 9);
        return convertDateFormat(date, date.contains(".") ? TEMPLATE_DATE_TIME_DOT : TEMPLATE_DATE_TIME_SLASH);
    }

    private String fillS(int row, boolean isStart) throws ParseException {
        val dateTime = getExpectedTimeIncome(row);
        if (isStart) {
            return convertDateFormat(dateTime, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(dateTime, TEMPLATE_DATE_DOT);
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        if (lentaDictionary == null) {
            return convertDateFormat(date, TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT) + " 10:00";
        }
        val time = convertDateFormat(lentaDictionary.getTimeShop(), TEMPLATE_TIME, TEMPLATE_TIME);
        return date + SPACE + time;
    }

    private String fillT(int row, boolean isStart) throws ParseException {
        val dateTime = getExpectedTimeIncome(row);
        if (isStart) {
            val result = DateUtils.addMinutes(dateTime, 90);
            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
        }
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        if (lentaDictionary == null) {
            return convertDateFormat(dateTime, TEMPLATE_DATE_DOT) + " 22:00";
        }
        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME);
        val timeShop = convertDateFormat(lentaDictionary.getTimeShop(), TEMPLATE_TIME);
        val dateResult = timeShop.after(timeStock) ? DateUtils.addDays(dateTime, 1) : dateTime;
        val dateResultString = convertDateFormat(dateResult, TEMPLATE_DATE_DOT);
        return dateResultString + SPACE + lentaDictionary.getTimeStock();
    }

    private Long getCode(int row) {
        return Long.parseLong(getCellValue(row, 0));
    }

    private String fillU(int row) {
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        return lentaDictionary == null ? "Код адреса не найден в справочнике: " + code : lentaDictionary.getAddressName();
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
