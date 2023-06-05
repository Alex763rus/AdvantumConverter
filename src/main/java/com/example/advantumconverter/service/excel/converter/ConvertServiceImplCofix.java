package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.exception.CarNotFoundException;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.jpa.Car;
import com.example.advantumconverter.model.jpa.CarRepository;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_COFIX;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_COFIX;
import static com.example.advantumconverter.utils.DateConverter.*;

@Component
public class ConvertServiceImplCofix extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 2;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private Set<Car> cars;

    @Autowired
    private CarRepository carRepository;

    @PostConstruct
    public void init() {
        val carsIter = carRepository.findAll();
        cars = new HashSet<>();
        carsIter.forEach(cars::add);
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_COFIX;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_COFIX;
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
            for (; row < LAST_ROW; ++row) {
                dataLine = new ArrayList<String>();
                dataLine.add(fillA(row));
                dataLine.add(convertDateFormat(getCellValue(1, 0), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
                dataLine.add(FILE_NAME_COFIX);
                dataLine.add("ООО \"Буш-Автопром\"");
                dataLine.add("");
                dataLine.add("Рефрижератор");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(String.valueOf(findCar(row).getTonnage()));
                dataLine.add(String.valueOf(findCar(row).getPallet()));
                dataLine.add("2");
                dataLine.add("4");
                dataLine.add("6");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(fillS(row));
                dataLine.add(fillT(row));
                dataLine.add(fillU(row));
                dataLine.add(fillV(row));
                dataLine.add(isStart(row) ? "Погрузка" : "Разгрузка");
                dataLine.add(fillX(row));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(fillAC(row));
                dataLine.add("");
                dataLine.add(fillAE(row));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");

                data.add(dataLine);
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
        return cars.stream().filter(e -> e.getCarName().equals(carName))
                .findFirst().orElseThrow(() -> new CarNotFoundException(""));
    }

    private String fillS(int row) throws ParseException {
        val next1 = getValueOrDefault(row, 1, 0);
        String dateResult = getCellValue(1, 0) + " " + (isStart(row) ? next1 : getCellValue(row, 5));
        return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_SLASH, TEMPLATE_DATE_TIME_DOT);
    }

    private String fillT(int row) throws ParseException {
        val dateText = fillS(row);
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
        val cur = getValueOrDefault(row, 0, 3);
        val prev1 = getValueOrDefault(row, -1, 3);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(""));
    }

}