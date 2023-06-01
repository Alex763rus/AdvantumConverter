package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.jpa.LentaDictionary;
import com.example.advantumconverter.model.jpa.LentaDictionaryRepository;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_LENTA;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_LENTA;
import static com.example.advantumconverter.enums.LentaDictionaryType.ADDRESS_RC;
import static com.example.advantumconverter.enums.LentaDictionaryType.WINDOW;
import static com.example.advantumconverter.utils.DateConverter.*;
import static com.example.advantumconverter.utils.DateConverter.convertDateFormat;

@Component
public class ConvertServiceImplLenta extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 2;

    private int LAST_ROW;

    private HashSet<LentaDictionary> addresses;
    @Autowired
    private LentaDictionaryRepository lentaDictionaryRepository;

    @PostConstruct
    public void init() {
        val addressesIter = lentaDictionaryRepository.findAll();
        addresses = new HashSet<>();
        addressesIter.forEach(addresses::add);
    }


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

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        int row = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
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
                dataLine.add("ООО ''ЛЕНТА''");
                dataLine.add(getCellValue(row, 8));
                dataLine.add("");
                dataLine.add("Рефрижератор");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("5000");
                dataLine.add("1");
                dataLine.add("2");
                dataLine.add("6");
                dataLine.add("2");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(fillS(row, isStart));
                dataLine.add(fillT(row, isStart));
                dataLine.add(fillU(row));
                dataLine.add(fillU(row));
                dataLine.add(isStart ? "Погрузка" : "Разгрузка");
                dataLine.add(String.valueOf(counterCopy));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(getCellValue(row, 5).replaceAll(" ", ""));
                dataLine.add("");
                dataLine.add(fillAE(row));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");

                ++counterCopy;
                data.add(dataLine);
            }
        } catch (Exception e) {
            throw new ConvertProcessingException("не удалось обработать строку:" + row
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private String fillAE(int row) {
        return WordUtils.capitalize(getCellValue(row, 2).toLowerCase());
    }


    private String fillS(int row, boolean isStart) throws ParseException {
        val date = convertDateFormat(getCellValue(START_ROW, 9), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_DOT);
        val code = Long.parseLong(getCellValue(row, 0));
        val dictionaryType = isStart ? ADDRESS_RC : WINDOW;
        val lentaDictionary = addresses.stream()
                .filter(e -> e.getLentaDictionaryKey() == code && e.getLentaDictionaryType() == dictionaryType)
                .findFirst().orElse(null);
        if (lentaDictionary == null) {
            return convertDateFormat(date, TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT) + " 10:00";
        }
        val time = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME, TEMPLATE_TIME);
        return date + " " + time;
    }

    private String fillT(int row, boolean isStart) throws ParseException {
        val date = convertDateFormat(getCellValue(START_ROW, 9), TEMPLATE_DATE_TIME_DOT);
        val code = Long.parseLong(getCellValue(row, 0));
        val dictionaryType = isStart ? ADDRESS_RC : WINDOW;
        val lentaDictionary = addresses.stream()
                .filter(e -> e.getLentaDictionaryKey() == code && e.getLentaDictionaryType() == dictionaryType)
                .findFirst().orElse(null);
        if (lentaDictionary == null) {
            return convertDateFormat(date, TEMPLATE_DATE_DOT) + " 22:00";
        }
        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME);
        val timeShop = convertDateFormat(lentaDictionary.getTimeShop(), TEMPLATE_TIME);
        val dateResult = timeStock.after(timeShop) ? DateUtils.addDays(date, 1) : date;
        val dateResultString = convertDateFormat(dateResult, TEMPLATE_DATE_DOT);
        return dateResultString + " " + lentaDictionary.getTimeShop();
    }

    private String fillU(int row) {
        val code = Long.parseLong(getCellValue(row, 0));
        val address = addresses.stream().filter(e -> e.getLentaDictionaryKey() == code).findFirst().orElse(null);
        return address == null ? "Код адреса не найден в справочнике:" + address : address.getAddressName();
    }

    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > LAST_ROW) {
            return "";
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 1);
        val prev1 = getValueOrDefault(row, -1, 1);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(""));
    }
}
