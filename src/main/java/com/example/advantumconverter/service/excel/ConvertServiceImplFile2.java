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
import java.util.*;

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
                    dataLine.add(fillT(row, copy));
                    dataLine.add(fillU(row, copy));
                    dataLine.add(fillU(row, copy));
                    dataLine.add(copy == 1 ? "Погрузка" : "Разгрузка");
                    dataLine.add(String.valueOf(copy));
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add(getCellValue(row, 14));
                    dataLine.add("");
                    dataLine.add(getCellValue(row, 16));
                    dataLine.add("");
                    dataLine.add("");
                    dataLine.add("");

                    data.add(dataLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private Date getTimeForS(int row, int copy) throws ParseException {
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
        return getCellDate(row, column);
    }

    private String fillS(int row, int copy) throws ParseException {
        val date = convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT) + " ";
        val time = getTimeForS(row, copy);
        return date + convertDateFormat(time, TEMPLATE_TIME);
    }

    private String fillT(int row, int copy) throws ParseException {
        val date = convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT) + " ";
        val time = DateUtils.addHours(getTimeForS(row, copy), 2);
        return date + convertDateFormat(time, TEMPLATE_TIME);
    }

    private String fillU(int row, int copy) {
        int column = 0;
        switch (copy) {
            case 1:
                column = 13;
                break;
            case 2:
                column = 21;
                break;
            case 3:
                column = 23;
                break;
        }
        return getCellValue(row, column);
    }

}
