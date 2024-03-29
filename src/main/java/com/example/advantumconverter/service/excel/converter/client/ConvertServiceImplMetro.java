package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.DictionaryException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_METRO;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.ExcelType.CLIENT;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_METRO;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplMetro extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;
    private int LAST_ROW;

    @Override
    public String getFileNamePrefix() {
        return getConverterName() + "_";
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_METRO;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_METRO;
    }

    private HashSet<Integer> processedRows;

    //А, В, Е, К, М, Н, О, Р, С, Т, У и Х
    private Map<String, String> translitNumberCar;

    @PostConstruct
    public void init() {
        translitNumberCar = new HashMap<>();
        translitNumberCar.put("А", "A");
        translitNumberCar.put("В", "B");
        translitNumberCar.put("Е", "E");
        translitNumberCar.put("К", "K");
        translitNumberCar.put("М", "M");
        translitNumberCar.put("Н", "H");
        translitNumberCar.put("О", "O");
        translitNumberCar.put("Р", "P");
        translitNumberCar.put("С", "C");
        translitNumberCar.put("Т", "T");
        translitNumberCar.put("У", "Y");
        translitNumberCar.put("Х", "X");
    }

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        processedRows = new HashSet<>();
        val currentFlights = new ArrayList<Flight>();
        Flight flight = null;
        boolean isStart = true;
        data.add(Header.headersOutputClient);
        sheet = book.getSheetAt(0);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ) {
                currentFlights.addAll(getNextFlights(row));
                if (currentFlights.isEmpty()) {
                    break;
                }
                row = row + currentFlights.size() - 1;
                isStart = true;
                for (int iFlight = 0; iFlight < currentFlights.size(); iFlight++) {
                    flight = currentFlights.get(iFlight);

                    dataLine = new ArrayList<>();

                    dataLine.add(fillA(flight.getExcelRow()));
                    dataLine.add(convertDateFormat(getCellValue(flight.getExcelRow(), 0), "dd/MM/yy", TEMPLATE_DATE_DOT));
                    dataLine.add(COMPANY_METRO);
                    dataLine.add(getCellValue(flight.getExcelRow(), 16));
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add("1000");
                    //10:
                    dataLine.add("1");
                    dataLine.add(fillL(flight.getExcelRow()));
                    dataLine.add(fillM(flight.getExcelRow()));
                    dataLine.add("2");
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(isStart, flight.getExcelRow()));
                    dataLine.add(fillT(isStart, flight.getExcelRow()));
                    //20:
                    dataLine.add(fillU(isStart, flight.getExcelRow()));
                    dataLine.add(fillV(isStart, flight.getExcelRow()));
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(isStart ? "1" : String.valueOf(iFlight + 1));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(translateCarNumber(getCellValue(flight.getExcelRow(), 12)));
                    dataLine.add(translateCarNumber(getCellValue(flight.getExcelRow(), 13)));
                    //30:
                    dataLine.add(fillAE(flight.getExcelRow()));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

                    data.add(dataLine);
                    isStart = false;
                }
                currentFlights.clear();
            }
        } catch (Exception e) {
            throw new ConvertProcessingException("не удалось обработать строку:" + row
                    + " , после значения:" + dataLine
                    + " , flight:" + flight
                    + ". Ошибка:" + e);
        }
        return data;
    }


    @Override
    public String getExcelType() {
        return CLIENT;
    }

    private String fillA(int row) {
        val excelValue = getCellValue(row, 1);
        return excelValue.substring(2, excelValue.length() - 2);
    }

    private String fillAE(int row) {
        return WordUtils.capitalize(getCellValue(row, 14).toLowerCase());
    }

    private String fillU(boolean isStart, final int row) {
        //Идем в C, там берем номер магазина, идем в справочник. Если не нашли, то ошибка
        if (isStart) {
            //Новый тип файла, смотреть столбец E в новом справочнике. Если не нашли, то ногинск
            return dictionaryService.getMetroDcAddressBrief(getTypeTc(row), RC_NOGINSK);
        }
        return getAddress(row);
    }

    private String fillV(boolean isStart, final int row) {
        //Идем в C, там берем номер магазина, идем в справочник. Если не нашли, то ошибка
        if (isStart) {
            //Новый тип файла, смотреть столбец E в новом справочнике. Если не нашли, то ногинск
            return dictionaryService.getMetroDcAddressName(getTypeTc(row), DC_NOGINSK);
        }
        return getAddress(row);
    }

    private String getTypeTc(final int row) {
        return getCellValue(row, 4);
    }

    private String getAddress(final int row) {
        val magazineCode = getMagazineId(row);
        val address = dictionaryService.getMetroTimeDictionary(String.valueOf(magazineCode));
        if (address == null) {
            throw new DictionaryException("Не найден адрес в справочнике адресов Метро по запросу: " + magazineCode);
        }
        return address;
    }

    private String fillS(boolean isStart, final int row) throws ParseException {
//        Приезд на погрузку:
//        L - дата + R время
        if (isStart) {
            val dateL = convertDateFormat(getCellValue(row, 11), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val timeR = getCellValue(row, 17);
            val dateResultString = dateL + " " + timeR;
            return dateResultString;
        } else {
//            Приезд на разгрузку:
//            Дата - Y, время из справочника:Справочник_временных_окон_приемки
//            столбец C - код магазина для справочника. Время прибытия (С)
            val dateY = convertDateFormat(getCellValue(row, 24), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val magazineId = getMagazineId(row);
            val magazineCodes = getMagazineCodes(row);
            val dictionary = dictionaryService.getMetroTimeStart(magazineId, magazineCodes);
            if (dictionary == null) {
                throw new DictionaryException("Не найдено временное окно в справочнике окон Метро по запросу: "
                        + magazineId + " [" + String.join(",", magazineCodes) + "]");
            }
            val dateResultString = dateY + " " + dictionary;
            return dateResultString;
        }
    }

    private Set<String> getMagazineCodes(final int row) {
        val set = new HashSet<String>();
        set.addAll(prepareMagazineCode(row, 6));
        set.addAll(prepareMagazineCode(row, 7));
        set.addAll(prepareMagazineCode(row, 8));
        set.addAll(prepareMagazineCode(row, 9));
        return set.stream().filter(code -> !code.equals(EMPTY))
                .collect(Collectors.toSet());
    }

    private List prepareMagazineCode(final int row, final int column) {
        return List.of(getCellValue(row, column).split("/"));
    }


    private String fillT(boolean isStart, final int row) throws ParseException {
        String dateResultString;
        if (isStart) {
            //убытие с погрузки:
            //L - дата + W время
            val dateL = convertDateFormat(getCellValue(row, 11), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val timeW = getCellValue(row, 22);
            dateResultString = dateL + " " + timeW;
        } else {
            //Выезд с ращгрузки:
            //Дата - Y, время из справочника:Справочник_временных_окон_приемки
            //столбец C - код магазина для справочника. Время прибытия (D)
            val dateY = convertDateFormat(getCellValue(row, 24), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val magazineId = getMagazineId(row);
            val magazineCodes = getMagazineCodes(row);
            val dictionary = dictionaryService.getMetroTimeEnd(magazineId, magazineCodes);
            if (dictionary == null) {
                throw new DictionaryException("Не найдено временное окно в справочнике окон Метро по запросу: "
                        + magazineId + " [" + String.join(",", magazineCodes) + "]");
            }
            dateResultString = dateY + " " + dictionary;
        }
        val dateResult = convertDateFormat(dateResultString, TEMPLATE_DATE_TIME_DOT);
        val dateS = convertDateFormat(fillS(isStart, row), TEMPLATE_DATE_TIME_DOT);
        val result = dateResult.before(dateS) ? DateUtils.addDays(dateResult, 1) : dateResult;
        return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
    }

    private Flight getNextFlight(final String key, final int row) {
        try {
            return new Flight(key,
//                    convertDateFormat(getCellValue(row, 1), "yy-MM-dd HH:mm:ss"),
//                    Integer.parseInt(getCellValue(row, 38)),
                    row
//                    null,
//                    null
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getKey(final int row) {
        return getCellValue(row, 1);
    }

    private List<Flight> getNextFlights(final int row) {
        val currentFlights = new ArrayList<Flight>();
        int y = row;
        while (y <= LAST_ROW && (processedRows.contains(y))) {
            ++y;
        }
        while (y <= LAST_ROW && !getCellValue(y, 10).equals(LEFT_FOR_A_FLIGHT)) {
            processedRows.add(y);
            ++y;
        }
        if (y > LAST_ROW) {
            return Collections.emptyList();
        }
        val rowKey = getKey(y);
        for (; y <= LAST_ROW; ++y) {
            val key = getKey(y);
            if (!processedRows.contains(y) && key.equals(rowKey)) {
                currentFlights.add(getNextFlight(key, y));
                processedRows.add(y);
            }
        }
        currentFlights.add(0, currentFlights.get(0));
        return currentFlights;
    }

    private String translateCarNumber(String carNumber) {
        val sb = new StringBuilder(carNumber.length());
        for (int i = 0; i < carNumber.length(); i++) {
            val ch = carNumber.substring(i, i + 1).toUpperCase();
            if (ch.equals(SPACE)) {
                continue;
            }
            sb.append(translitNumberCar.getOrDefault(ch, ch));
        }
        return sb.toString();
    }

    private Long getMagazineId(int row) {
        return Long.parseLong(getCellValue(row, 2));
    }

    private String getExcelTemperature(int row) {
        return getCellValue(row, 27);
    }

    private String fillL(int row) {
        //Столбец AB и справочник ищем по коду, забираем B и C. Если пусто, не заполняем
        val excelTemperature = getExcelTemperature(row);
        if (excelTemperature.equals(EMPTY)) {
            return EMPTY;
        }
        val temperature = dictionaryService.getMetroMinTemperature(excelTemperature);
        if (temperature == null) {
            throw new DictionaryException("Не найдена температура в справочнике температур Метро по запросу: " + excelTemperature);
        }
        return String.valueOf(temperature);
    }

    private String fillM(int row) {
        //Столбец AB и справочник ищем по коду, забираем B и C. Если пусто, не заполняем
        val excelTemperature = getExcelTemperature(row);
        if (excelTemperature.equals(EMPTY)) {
            return EMPTY;
        }
        val temperature = dictionaryService.getMetroMaxTemperature(excelTemperature);
        if (temperature == null) {
            throw new DictionaryException("Не найдена температура в справочнике температур Метро по запросу: " + excelTemperature);
        }
        return String.valueOf(temperature);
    }

    private int calcTonnage(int row) {
        val doubleValue = Double.parseDouble(getCellValue(row, 10).replaceAll(",", ".")) * 1000;
        return (int) doubleValue;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    private class Flight {
        private String key;
        private Integer excelRow;
    }
}
