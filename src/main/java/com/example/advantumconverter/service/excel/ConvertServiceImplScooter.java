package com.example.advantumconverter.service.excel;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.excel.Car;
import com.example.advantumconverter.model.excel.Header;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.BOGORODSK;
import static com.example.advantumconverter.constant.Constant.SCOOTER;
import static com.example.advantumconverter.utils.DateConverter.*;

@Component
public class ConvertServiceImplScooter extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;

    private int LAST_ROW;

    @Override
    public String getFileNamePrefix() {
        return SCOOTER + "_";
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        int row = START_ROW;
        int mainRow = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val isStart = isStart(row);
                if (isStart) {
                    mainRow = row;
                    counterCopy = 1;
                }
                dataLine = new ArrayList<>();
                dataLine.add(fillA(mainRow));
                dataLine.add(getCurrentDate(TEMPLATE_DATE_DOT));
                dataLine.add("Самокат");
                dataLine.add("ООО \"Буш-Автопром\"");
                dataLine.add("");
                dataLine.add("Рефрижератор");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("5");
                dataLine.add("10");
                dataLine.add("2");
                dataLine.add("4");
                dataLine.add("6");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(fillS(mainRow, isStart));
                dataLine.add(fillT(mainRow, isStart));
                dataLine.add("Склад FMCG МСК");
                dataLine.add("Московская область, Пушкинский район, г.п. Софрино, 48 км Ярославского шоссе, владение 1\n");
                dataLine.add(isStart ? "Погрузка" : "Разгрузка");
                dataLine.add(String.valueOf(counterCopy));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(getCellValue(mainRow, 3));
                dataLine.add("");
                dataLine.add(getCellValue(mainRow, 4));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");

                ++counterCopy;
                data.add(dataLine);
            }
        } catch (Exception e) {
            throw new ConvertProcessingException("не удалось обработать строку:" + mainRow
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private String fillA(int row) throws ParseException {
        return getCellValue(row, 0) + getCurrentDate(TEMPLATE_DATE);
    }

    private String fillS(int row, boolean isStart) throws ParseException {
        val date = getCurrentDate(TEMPLATE_DATE_DOT);
        val time = getCellDate(row, 0);
        if (isStart) {
            DateUtils.addHours(time, -2);
        }
        return date + " " + convertDateFormat(time, TEMPLATE_TIME);
    }

    private String fillT(int row, boolean isStart) throws ParseException {
        val date = getCurrentDate(TEMPLATE_DATE_DOT);
        val time = getCellDate(row, 0);
        if (isStart) {
            DateUtils.addHours(time, -2);
        } else {
            DateUtils.addHours(time, -1);
        }
        return date + " " + convertDateFormat(time, TEMPLATE_TIME);
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
