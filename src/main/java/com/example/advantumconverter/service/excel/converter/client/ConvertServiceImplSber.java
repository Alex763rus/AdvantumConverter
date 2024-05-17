package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SBER;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SBER;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplSber extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final static String YAROSLAVSKOE_HIGHWAY = "Ярославское шоссе 222";
    private final static String TWO_SPACE = "  ";
    private final static String MINUS = "-";
    private final static String EXPECTED_TIME_FORMULA = "CHOOSE(1+(B2>=12)+(B2>=23)+(B2>=34)+(B2>=45)+(B2>=56)+(B2>=67)+(B2>=78)+(B2>=89),\"4:00\",\"5:00\",\"6:00\",\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\")";

    @Override
    public String getConverterName() {
        return FILE_NAME_SBER;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SBER;
    }


    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        boolean isStart = true;
        String fio = EMPTY;
        String carNumber = EMPTY;
        try {
            sheet = book.getSheetAt(0);
//            if (!getCellValue(START_ROW, 0).equals(EXPECTED_TIME_FORMULA)) {
//                throw new ConvertProcessingException("В столбце A изменилась формула. В ячейке A1 ожидается формула:" + EXPECTED_TIME_FORMULA);
//            }
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val dateFromFile = getDateFromFile(row);
                val dateString = convertDateFormat(dateFromFile, "ddMMyyyy");
                val numberUnloading = getIntegerValue(row, 8);
                isStart = numberUnloading == 1;

                if (isStart) {
                    carNumber = getCellValue(row, 3).replace("RUS", EMPTY);
                    fio = getCellValue(row, 4);
                }
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = new ArrayList<String>();
                    dataLine.add(getCellValue(row, 7) + dateString);
                    dataLine.add(convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT));
                    dataLine.add(FILE_NAME_SBER);
                    dataLine.add(SBER_BUSH_AUTOPROM_ORGANIZATION_NAME);
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add("5000");
                    dataLine.add("12");
                    dataLine.add(fillL(row));
                    dataLine.add(fillM(row));
                    dataLine.add("2");
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(isStart, row, dateFromFile));
                    dataLine.add(fillT(isStart, row, dateFromFile));
                    dataLine.add(fillUV(isStart, row));
                    dataLine.add(fillUV(isStart, row));
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(isStart ? 0 : numberUnloading));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add("1");
                    dataLine.add(EMPTY);
                    dataLine.add(carNumber);
                    dataLine.add(EMPTY);
                    dataLine.add(fio);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                }

            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBook(getConverterName() + "_", "Экспорт", data, "Готово!");
    }

    private String prepareTemperature(int row) {
        return getCellValue(row, 15)
                .replace("(", "")
                .replace(")", "")
                .replace("+", "");
    }

    private String fillL(int row) {
        return prepareTemperature(row).split(",")[0];
    }

    private String fillM(int row) {
        return prepareTemperature(row).split(",")[1];
    }


    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date getDateFromFile(int row) {
        return convertDateFormat(getCellValue(row, 16), TEMPLATE_DATE_SLASH);
    }

    private FlightTime getFlightTime(int flightNumber) {
        if (flightNumber >= 89) {
            return FlightTime.TIME_12_00;
        } else if (flightNumber >= 78) {
            return FlightTime.TIME_11_00;
        } else if (flightNumber >= 67) {
            return FlightTime.TIME_10_00;
        } else if (flightNumber >= 56) {
            return FlightTime.TIME_9_00;
        } else if (flightNumber >= 45) {
            return FlightTime.TIME_8_00;
        } else if (flightNumber >= 34) {
            return FlightTime.TIME_7_00;
        } else if (flightNumber >= 23) {
            return FlightTime.TIME_6_00;
        } else if (flightNumber >= 12) {
            return FlightTime.TIME_5_00;
        }
        return FlightTime.TIME_4_00;
    }

    private String fillS(boolean isStart, int row, Date dateFromFile) throws ParseException {
        if (isStart) {
            val flightNumber = getFlightTime(Integer.parseInt(getCellValue(row, 7)));
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + flightNumber.getTime();
        } else {
            val time = getCellValue(row, 14).split("-")[0];
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        }
    }

    private String fillT(boolean isStart, int row, Date dateFromFile) throws ParseException {
        if (isStart) {
            val dateS = convertDateFormat(fillS(isStart, row, dateFromFile), TEMPLATE_DATE_TIME_DOT);
            val result = DateUtils.addHours(dateS, 2);
            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
        } else {
            val time = getCellValue(row, 14).split(MINUS)[1];
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        }
    }

    private String fillUV(boolean isStart, int row) {
        return isStart ? YAROSLAVSKOE_HIGHWAY : getCellValue(row, 5)
                .replace(TWO_SPACE, SPACE)
                .trim();
    }

    private enum FlightTime {
        TIME_4_00("4:00"),
        TIME_5_00("5:00"),
        TIME_6_00("6:00"),
        TIME_7_00("7:00"),
        TIME_8_00("8:00"),
        TIME_9_00("9:00"),
        TIME_10_00("10:00"),
        TIME_11_00("11:00"),
        TIME_12_00("12:00");

        private String time;

        FlightTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }
    }
}
