package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.SberAddressNotFoundException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.jpa.sber.SberAddressDictionary;
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
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SBER;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SBER;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplSber extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 2;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final static String YAROSLAVSKOE_HIGHWAY = "Ярославское шоссе 222";
    private final static String EXPECTED_TIME_FORMULA = "CHOOSE(1+(B2>=12)+(B2>=23)+(B2>=34)+(B2>=45)+(B2>=56)+(B2>=67)+(B2>=78)+(B2>=89),\"4:00\",\"5:00\",\"6:00\",\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\")";
    private final static Map<String, String> TK_NAME_NUMBER_MAP = Map.of(
            SBER_BUSH_AUTOPROM_ORGANIZATION_NAME, "1",
            SBER_SQUIRREL_ORGANIZATION_NAME, "2"
    );

    @Override
    public String getConverterName() {
        return FILE_NAME_SBER;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SBER;
    }

    private String addressFromFile = EMPTY;
    private SberAddressDictionary cityFromDictionary = null;

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        boolean isStart = true;
        String reisNumber = EMPTY;
        String fio = EMPTY;
        String carNumber = EMPTY;
        String organization = EMPTY;
        try {
            sheet = book.getSheetAt(0);
//            if (!getCellValue(START_ROW, 0).equals(EXPECTED_TIME_FORMULA)) {
//                throw new ConvertProcessingException("В столбце A изменилась формула. В ячейке A1 ожидается формула:" + EXPECTED_TIME_FORMULA);
//            }
            LAST_ROW = getLastRow(START_ROW, 1);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            addressFromFile = getCellValue(0, 0).trim();
            cityFromDictionary = dictionaryService.getSberCity(addressFromFile);
            if (cityFromDictionary == null) {
                throw new SberAddressNotFoundException();
            }
            for (; row <= LAST_ROW; ++row) {
                val dateFromFile = getDateFromFile(row);
                var dateFromFileForSt = dateFromFile;
                val dateString = convertDateFormat(dateFromFile, "ddMMyy");
                val numberUnloading = getIntegerValue(row, 8);
                isStart = numberUnloading == 1;
                if (isStart) {
                    dateFromFileForSt = DateUtils.isSameDay(convertDateFormat(fillT(true, row, dateFromFile), TEMPLATE_DATE_TIME_DOT), dateFromFile) ?
                            dateFromFile : DateUtils.addDays(dateFromFile, -1);
                    reisNumber = generateId() + UNDERSCORE + dateString;
                    carNumber = deleteSpace(getCellValue(row, 3).replace("RUS", EMPTY));
                    fio = prepareFio(getCellValue(row, 4));
                    organization = deleteSpace(getCellValue(row, 10));
                }

                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = new ArrayList<String>();
                    dataLine.add(reisNumber);
                    dataLine.add(convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT));
                    dataLine.add(cityFromDictionary.getCity().trim());
                    dataLine.add(organization);
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
                    dataLine.add(fillS(isStart, row, isStart ? dateFromFileForSt : dateFromFile));
                    dataLine.add(fillT(isStart, row, isStart ? dateFromFileForSt : dateFromFile));
                    dataLine.add(fillU(isStart, row));
                    dataLine.add(fillV(isStart, row));
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(isStart ? 0 : numberUnloading));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(TK_NAME_NUMBER_MAP.getOrDefault(organization, EMPTY));
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
        } catch (SberAddressNotFoundException | TemperatureNodValidException e) {
            throw e;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBook(getConverterName() + UNDERSCORE, EXPORT, data, DONE);
    }

    private String prepareFio(String value) {
        if (value == null) {
            return EMPTY;
        }
        return value.split(" ВУ ")[0];
    }

    private String deleteSpace(String value) {
        return value == null ? EMPTY : value.trim();
    }

    private String prepareTemperature(int row) {
        return getCellValue(row, 15)
                .replace(SPACE, EMPTY)
                .replace("(", EMPTY)
                .replace(")", EMPTY)
                .replace("+", EMPTY);
    }

    private String generateId() {
        return String.valueOf((int) (11111 + Math.random() * (99999 - 11111 + 1)));
    }

    private String fillL(int row) {
        var temperature = prepareTemperature(row).split(",");
        if (temperature.length < 2) {
            throw new TemperatureNodValidException(String.valueOf(row + 1));
        }
        return temperature[0];
    }

    private String fillM(int row) {
        var temperature = prepareTemperature(row).split(",");
        if (temperature.length < 2) {
            throw new TemperatureNodValidException(String.valueOf(row + 1));
        }
        return temperature[1];
    }


    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date getDateFromFile(int row) {
        return convertDateFormat(getCellValue(row, 16), TEMPLATE_DATE_SLASH);
    }

    private String fillS(boolean isStart, int row, Date dateFromFile) throws ParseException {
        //для погрузки лдя времени первого столбца точки. убытие 6 часов. Обытие Прибытие минус 2 от прибытия.
        //разгрузки:
        if (isStart) {
            val time = getCellValue(row, 0);
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        } else {
            val time = getCellValue(row, 14).split(MINUS)[0];
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        }
    }

    private String fillT(boolean isStart, int row, Date dateFromFile) throws ParseException {
        if (isStart) {
            val dateT = convertDateFormat(fillS(isStart, row, dateFromFile), TEMPLATE_DATE_TIME_DOT);
            val result = DateUtils.addHours(dateT, 2);
            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
        } else {
            val time = getCellValue(row, 14).split(MINUS)[1];
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        }
    }

    private String fillU(boolean isStart, int row) {
        return isStart ? cityFromDictionary.getCityAndRegion().trim() : getCellValue(row, 5)
                .replace(TWO_SPACE, SPACE)
                .trim();
    }

    private String fillV(boolean isStart, int row) {
        return isStart ? addressFromFile : getCellValue(row, 5)
                .replace(TWO_SPACE, SPACE)
                .trim();
    }

    protected int getLastRow(int startRow, int col) {
        int i = startRow;
        for (; ; i++) {
            if (getCellValue(i, col).equals(EMPTY)) {
                if (getCellValue(i + 1, col).equals(EMPTY)) {
                    break;
                }
            }
        }
        return i - 1;
    }
}
