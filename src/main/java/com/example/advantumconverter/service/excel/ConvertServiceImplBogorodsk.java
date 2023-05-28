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
import static com.example.advantumconverter.utils.DateConverter.*;

@Component
public class ConvertServiceImplBogorodsk extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;

    private Set<Car> cars;
    private int LAST_ROW;

    @PostConstruct
    public void init() {
        cars = new HashSet<>();
    }

    @Override
    public String getFileNamePrefix() {
        return BOGORODSK + "_";
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                for (int copy = 1; copy < 4; ++copy) {
                    dataLine = new ArrayList<String>();
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
            throw new ConvertProcessingException("не удалось обработать строку:" + row
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private Date getTimeForS(int row, int copy) {
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
