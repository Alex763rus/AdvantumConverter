package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.DictionaryException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_OZON;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_OZON;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplOzon extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;
    private int LAST_ROW;


    @Override
    public String getConverterName() {
        return FILE_NAME_OZON;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_OZON;
    }

    //А, В, Е, К, М, Н, О, Р, С, Т, У и Х
    private Map<String, String> translitNumberCar;
    private HashSet<Integer> processedRows;

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
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
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

                    dataLine.add(getCellValue(flight.getExcelRow(), 0));
                    dataLine.add(convertDateFormat(new Date(), TEMPLATE_DATE_DOT));
                    dataLine.add(COMPANY_OZON_FRESH);
                    dataLine.add(fillD(flight.getExcelRow()));
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillJ(currentFlights.get(0).getExcelRow()));
                    //10:
                    dataLine.add(getCellValue(currentFlights.get(0).getExcelRow(), 11));
                    dataLine.add(fillL(flight.getExcelRow()));
                    dataLine.add(fillM(flight.getExcelRow()));
                    dataLine.add(getCellValue(flight.getExcelRow(), 14));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(isStart, currentFlights, iFlight));
                    dataLine.add(fillT(isStart, currentFlights, iFlight));
                    //20:
                    dataLine.add(getCellValue(flight.getExcelRow(), isStart ? 22 : 23));
                    dataLine.add(getCellValue(flight.getExcelRow(), isStart ? 22 : 23));
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(isStart ? "0" : String.valueOf(flight.getRowOrder()));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(flight.getExcelRow(), 3).equals(COMPANY_DEAL_AUTO_TRANS_INCORRECT) ? COMPANY_DEAL_AUTO_TRANS_INN : EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(translateCarNumber(getCellValue(flight.getExcelRow(), 30)));
                    dataLine.add(EMPTY);
                    //30:
                    dataLine.add(getCellValue(flight.getExcelRow(), 32));
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
        return createDefaultBook(getConverterName() + "_", "Экспорт", data, "Готово!");
    }

    private String fillS(boolean isStart, final List<Flight> currentFlights, int iFlight) throws ParseException {
        val currentFlight = currentFlights.get(iFlight);
        Date dateResult = null;
        if (isStart) {
            dateResult = getMinimalDate(currentFlights);
            currentFlight.setDatePointIncome(dateResult);
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_DOT);
        } else {
            dateResult = currentFlights.get(iFlight - 1).getDateOutcome();
            val placeStart = getCellValue(currentFlight.getExcelRow(), 22);
            val placeFinish = getCellValue(currentFlight.getExcelRow(), 23);
            val timeTransfer = dictionaryService.getOzonTransitTime(placeStart, placeFinish);
            if (timeTransfer == null) {
                throw new DictionaryException("Не найдено время в справочнике трансфера по запросу: " + placeStart + " - " + placeFinish);
            }
            val dateSplit = timeTransfer.split(":");
            dateResult = DateUtils.addHours(dateResult, Integer.parseInt(dateSplit[0]));
            dateResult = DateUtils.addMinutes(dateResult, Integer.parseInt(dateSplit[1]));
            dateResult = DateUtils.addSeconds(dateResult, Integer.parseInt(dateSplit[2]));
        }
        currentFlight.setDatePointIncome(dateResult);
        val dateS = dateResult;
        if ("0".equals(getCellValue(currentFlight.getExcelRow(), 37))) {
            return convertDateFormat(dateS, TEMPLATE_DATE_TIME_DOT);
        }
        val stockBrief = getCellValue(currentFlight.getExcelRow(), 23);
        val hTime = dateS.getHours();
        val ozonDictionary = dictionaryService.getBestDictionary(stockBrief, hTime);
        if (ozonDictionary == null) {
            return convertDateFormat(dateS, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(dateS, TEMPLATE_DATE_DOT);
        val dateTimeString = date + SPACE + ozonDictionary.getStockInTime();
        return dateTimeString;
    }

    private String fillT(boolean isStart, final List<Flight> currentFlights, int iFlight) throws ParseException {
        val currentFlight = currentFlights.get(iFlight);
        Date dateResult = null;
        if (isStart) {
            dateResult = getMinimalDate(currentFlights);
            val tonnage = calcTonnage(currentFlights.get(iFlight).getExcelRow());
            val time = dictionaryService.getOzonTonnageTime((long) tonnage);
            if (time == null) {
                throw new DictionaryException("Не найдено время в справочнике тоннажа по запросу: " + tonnage);
            }
            val dateSplit = time.split(":");
            dateResult = DateUtils.addHours(dateResult, Integer.parseInt(dateSplit[0]));
            dateResult = DateUtils.addMinutes(dateResult, Integer.parseInt(dateSplit[1]));
            dateResult = DateUtils.addSeconds(dateResult, Integer.parseInt(dateSplit[2]));

            currentFlight.setDateOutcome(dateResult);
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_DOT);
        } else {
            dateResult = currentFlight.getDatePointIncome();
            val countPallet = Integer.parseInt(getCellValue(currentFlight.getExcelRow(), 11));
            val placeFinish = getCellValue(currentFlight.getExcelRow(), 23);
            val ozonLoadUnloadTime = dictionaryService.getOzonLoadUnloadTime(placeFinish);
            if (ozonLoadUnloadTime == null) {
                throw new DictionaryException("Не найдено время в справочнике загрузки разгрузки по запросу: " + placeFinish);
            }
            val addedTime = countPallet * ozonLoadUnloadTime + 20;
            dateResult = DateUtils.addMinutes(dateResult, addedTime);
        }
        currentFlight.setDateOutcome(dateResult);
        val dateS = currentFlight.getDatePointIncome();
        val dateT = dateResult;
        if ("0".equals(getCellValue(currentFlight.getExcelRow(), 37))) {
            return convertDateFormat(dateT, TEMPLATE_DATE_TIME_DOT);
        }
        val stockBrief = getCellValue(currentFlight.getExcelRow(), 23);
        val hTime = dateS.getHours();
        val ozonDictionary = dictionaryService.getBestDictionary(stockBrief, hTime);
        if (ozonDictionary == null) {
            return convertDateFormat(dateT, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(dateT, TEMPLATE_DATE_DOT);
        val dateTimeString = date + SPACE + prepareStockOutTime(ozonDictionary.getStockOutTime());
        return dateTimeString;
    }

    private Flight getNextFlight(final String key, final int row) {
        try {
            return new Flight(key,
                    convertDateFormat(getCellValue(row, 1), "yy-MM-dd HH:mm:ss"),
                    Integer.parseInt(getCellValue(row, 38)),
                    row,
                    null,
                    null
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getKey(final int row) {
        return getCellValue(row, 0);
    }

    private List<Flight> getNextFlights(final int row) {
        val currentFlights = new ArrayList<Flight>();
        int y = row;
        while (y <= LAST_ROW && processedRows.contains(y)) {
            ++y;
        }
        if (y > LAST_ROW) {
            return Collections.emptyList();
        }
        val rowKey = getCellValue(y, 0);
        for (; y <= LAST_ROW; ++y) {
            val key = getKey(y);
            if (!processedRows.contains(y) && key.equals(rowKey)) {
                currentFlights.add(getNextFlight(key, y));
            }
        }
        val sortedFlights = currentFlights.stream()
                .sorted(Comparator.comparing(Flight::getRowOrder))
                .collect(Collectors.toList());

        sortedFlights.add(0, sortedFlights.get(0));
        return sortedFlights;
    }

    private String translateCarNumber(String carNumber) {
        val sb = new StringBuilder(carNumber.length());
        for (int i = 0; i < carNumber.length(); i++) {
            val ch = carNumber.substring(i, i + 1).toUpperCase();
            sb.append(translitNumberCar.getOrDefault(ch, ch));
        }
        return sb.toString();
    }

    private String fillL(int row) {
        val excel11 = Integer.parseInt(getCellValue(row, 12));
        val excel12 = Integer.parseInt(getCellValue(row, 13));
        return String.valueOf(Math.min(excel11, excel12));
    }

    private String fillM(int row) {
        val excel11 = Integer.parseInt(getCellValue(row, 12));
        val excel12 = Integer.parseInt(getCellValue(row, 13));
        return String.valueOf(Math.max(excel11, excel12));
    }

    private String fillD(int row) {
        val excelValue = getCellValue(row, 3);
        return excelValue.equals(COMPANY_DEAL_AUTO_TRANS_INCORRECT) ? COMPANY_DEAL_AUTO_TRANS : excelValue;
    }

    private Date getMinimalDate(final List<Flight> currentFlights) {
        return currentFlights.stream()
                .sorted(Comparator.comparing(Flight::getDate))
                .findFirst()
                .map(flight -> flight.getDate())
                .orElse(null);
    }


    private String prepareStockOutTime(String time) {
        return time.equals("00:00") ? "23:59" : time;
    }

    private int calcTonnage(int row) {
        val doubleValue = Double.parseDouble(getCellValue(row, 10).replaceAll(",", ".")) * 1000;
        return (int) doubleValue;
    }

    private String fillJ(int row) {
        return String.valueOf(calcTonnage(row));
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    private class Flight {
        private String key;
        private Date date;
        private Integer rowOrder;
        private Integer excelRow;
        private Date dateOutcome;
        private Date datePointIncome;
    }
}
