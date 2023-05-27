package com.example.advantumconverter.service.excel;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.model.excel.Car;
import com.example.advantumconverter.model.excel.Header;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.advantumconverter.utils.DateConverter.*;

@Component
public class ConvertServiceImplFile2 extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;

    private Set<Car> cars;

    @PostConstruct
    public void init() {
        cars = new HashSet<>();
//        cars.add(new Car("4500 - *HYUN 5ТБУШ.10", 5, 10));
//        cars.add(new Car("4501 - *HYUN 10ТБУШ 16", 10, 16));
//        cars.add(new Car("11 - *HYUN 20ТБУШ.33", 20, 33));
//        cars.add(new Car("4700 - *HYUN 5ТСОF-БУШ", 5, 10));
//        cars.add(new Car("4701 - *HYUN 3TCOF-БУШ", 3, 10));
//        cars.add(new Car("2350 - *10П2Т (РЕФЗАК)", 2, 10));
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        try {
            sheet = book.getSheet("Sheet1");
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (int row = START_ROW; row <= sheet.getLastRowNum(); ++row) {
                for (int copy = 1; copy < 4; ++copy) {
                    val dataLine = new ArrayList<String>();
                    dataLine.add(getCellValue(row, 1));
                    dataLine.add(convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
                    dataLine.add("Х5 Богородск");
                    dataLine.add("ООО \"Буш-Автопром\"");
                    dataLine.add("");
                    dataLine.add("Рефрижератор");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add(getCellValue(row, 4));
                    dataLine.add(getCellValue(row, 5));
                    dataLine.add("2");
                    dataLine.add("4");
                    dataLine.add("6");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add(fillS(row, copy));
//                    dataLine.add(fillT(row));
//                    dataLine.add(fillU(row));
//                    dataLine.add(fillV(row));
//                    dataLine.add(isStart(row) ? "Погрузка" : "Разгрузка");
//                    dataLine.add(fillX(row));
//                    dataLine.add("");
//                    dataLine.add("");
//                    dataLine.add("");
//                    dataLine.add("");
//                    dataLine.add(fillAC(row));
//                    dataLine.add("");
//                    dataLine.add(fillAE(row));
//                    dataLine.add("");
//                    dataLine.add("");
//                    dataLine.add("");
                    //===========================
                    data.add(dataLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > sheet.getLastRowNum()) {
            return "";
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private String getCellValue(int row, int col) {
        if (sheet.getRow(row) == null) {
            return "";
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }

    private String fillA(int row) throws ParseException {
        val cur = getValueOrDefault(row, 0, 3);
        val next1 = getValueOrDefault(row, 1, 3);
        val next2 = getValueOrDefault(row, 2, 3);
        val date = convertDateFormat(getCellValue(1, 0), TEMPLATE_DATE_SLASH, TEMPLATE_DATE);
        return (next1.equals(next2) ? next1 : cur) + date;
    }


    private Car findCar(int row) {
        val cur = getValueOrDefault(row, 0, 9);
        val next1 = getValueOrDefault(row, 1, 9);
        val next2 = getValueOrDefault(row, 2, 9);
        val carName = next1.equals(next2) && !next1.equals("") ? next1 : cur;
        return cars.stream().filter(e -> e.getName().equals(carName))
                .findFirst().orElseThrow(() -> new CarNotFoundException(""));
    }

    private String fillS(int row, int copy) throws ParseException {
        val date = convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT) + " ";
        int column = 0;
        switch (copy) {
            case 1:
                column = 3;
                break;
            case 2:
                column = 20;
                break;
            case 3:
                column = 22;
                break;
        }
        return convertDateFormat(date + getCellValue(row, column), TEMPLATE_DATE_TIME_SEC_DOT, TEMPLATE_DATE_TIME_DOT);
    }

    private String fillT(int row, int copy) throws ParseException {
        val dateText = fillS(row, copy);
        val date = convertDateFormat(dateText, TEMPLATE_DATE_TIME_DOT);
        return convertDateFormat(DateUtils.addHours(date, 1), TEMPLATE_DATE_TIME_DOT);
    }

    private String fillU(int row) {
        return isStart(row) ? "Склад Рулог М3" : getCellValue(row, 4);
    }

    private String fillV(int row) {
        return isStart(row) ? fillU(row) : getCellValue(row, 11);
    }

    private String fillX(int row) {
        int i = row;
        for (; i >= START_ROW; --i) {
            if (isStart(i)) {
                break;
            }
            if (getValueOrDefault(i, 0, 0).equals("") && i != row) {
                break;
            }
        }
        return String.valueOf(row - i + 1);
    }

    private String fillAC(int row) {
        return getFioTrack(row, 13);
    }

    private String fillAE(int row) {
        return getFioTrack(row, 12);
    }

    private String getFioTrack(int row, int column) {
        if (isStart(row)) {
            val result = getValueOrDefault(row, 1, column);
            return result.equals("") ? "0" : result;
        }
        for (int i = row; i >= START_ROW; --i) {
            val value = getValueOrDefault(i, 0, column);
            if (!value.equals("")) {
                return value;
            }
            if (getValueOrDefault(i, 0, 0).equals("") && i != row) {
                break;
            }
        }
        return "0";
    }

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 3);
        val prev1 = getValueOrDefault(row, -1, 3);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(""));
    }

}
