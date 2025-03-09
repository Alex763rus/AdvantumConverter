package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.DictionaryException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
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
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_METRO;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplMetro extends ConvertServiceBase implements ConvertService {
    private final int START_ROW = 1;
    private int LAST_ROW;


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

    private List<String> warnings = new ArrayList<>();

    @Override
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataV2 dataLine = null;


        processedRows = new HashSet<>();
        val currentFlights = new ArrayList<Flight>();
        Flight flight = null;
        boolean isStart = true;
        sheet = book.getSheetAt(0);
        int row = START_ROW;
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
                    dataLine = ConvertedListDataV2.init()
                            .setColumnAdata(fillA(flight.getExcelRow()))
                            .setColumnBdata(convertDateFormat(getCellValue(flight.getExcelRow(), 0), "dd/MM/yy"))
                            .setColumnCdata(COMPANY_METRO)
                            .setColumnDdata(getCellValue(flight.getExcelRow(), 16))
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(1000)
                            .setColumnKdata(1)
                            .setColumnLdata(fillL(flight.getExcelRow()))
                            .setColumnMdata(fillM(flight.getExcelRow()))
                            .setColumnNdata(2)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(fillS(isStart, flight.getExcelRow()))
                            .setColumnTdata(fillT(isStart, flight.getExcelRow()))
                            .setColumnUdata(fillU(isStart, flight.getExcelRow()))
                            .setColumnVdata(fillV(isStart, flight.getExcelRow()))
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 1 : (iFlight + 1))
                            .setColumnYdata(null)
                            .setColumnZdata(null)
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(translateCarNumber(getCellValue(flight.getExcelRow(), 12)))
                            .setColumnAdData(translateCarNumber(getCellValue(flight.getExcelRow(), 13)))
                            .setColumnAeData(fillAE(flight.getExcelRow()))
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .setColumnAiData(EMPTY)
                            .setColumnAjData(EMPTY)
                            .setColumnAkData(EMPTY)
                            .setColumnAlData(EMPTY)
                            .setColumnAmData(EMPTY)
                            .setColumnAnData(EMPTY)
                            .setColumnAoData(EMPTY)
                            .setColumnApData(EMPTY)
                            .setTechFullFio(EMPTY)
                            .build();

                    data.add(dataLine);
                    isStart = false;
                }
                currentFlights.clear();
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return ConvertedBookV2.init()
                .setBookV2(List.of(
                        ConvertedListV2.init()
                                .setHeadersV2(Header.headersOutputClientV2)
                                .setExcelListName(EXPORT)
                                .setExcelListContentV2(data)
                                .build()
                ))
                .setMessage(DONE + warnings.stream().distinct().collect(Collectors.joining("")))
                .setBookName(getConverterName() + UNDERSCORE)
                .build();
    }


    @Override
    public ExcelType getExcelType() {
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

    private Date fillS(boolean isStart, final int row) throws ParseException {
//        Приезд на погрузку:
//        L - дата + R время
        if (isStart) {
            val dateL = convertDateFormat(getCellValue(row, 11), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val timeR = getCellValue(row, 17);
            val dateResultString = dateL + SPACE + timeR;
            return convertDateFormat(dateResultString, TEMPLATE_DATE_TIME_DOT);
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
            val dateResultString = dateY + SPACE + dictionary;
            return convertDateFormat(dateResultString, TEMPLATE_DATE_TIME_DOT);
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


    private Date fillT(boolean isStart, final int row) throws ParseException {
        String dateResultString;
        if (isStart) {
            //убытие с погрузки:
            //L - дата + W время
            val dateL = convertDateFormat(getCellValue(row, 11), "dd/MM/yy", TEMPLATE_DATE_DOT);
            val timeW = getCellValue(row, 22);
            dateResultString = dateL + SPACE + timeW;
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
            dateResultString = dateY + SPACE + dictionary;
        }
        val dateResult = convertDateFormat(dateResultString, TEMPLATE_DATE_TIME_DOT);
        val dateS = fillS(isStart, row);
        return dateResult.before(dateS) ? DateUtils.addDays(dateResult, 1) : dateResult;
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

    private Integer fillL(int row) {
        //Столбец AB и справочник ищем по коду, забираем B и C. Если пусто, не заполняем
        val excelTemperature = getExcelTemperature(row);
        if (excelTemperature.equals(EMPTY)) {
            return null;
        }
        val temperature = dictionaryService.getMetroMinTemperature(excelTemperature);
        if (temperature == null) {
            throw new DictionaryException("Не найдена температура в справочнике температур Метро по запросу: " + excelTemperature);
        }
        return (int) (long) temperature;
    }

    private Integer fillM(int row) {
        //Столбец AB и справочник ищем по коду, забираем B и C. Если пусто, не заполняем
        val excelTemperature = getExcelTemperature(row);
        if (excelTemperature.equals(EMPTY)) {
            return null;
        }
        val temperature = dictionaryService.getMetroMaxTemperature(excelTemperature);
        if (temperature == null) {
            throw new DictionaryException("Не найдена температура в справочнике температур Метро по запросу: " + excelTemperature);
        }
        return (int) (long) temperature;
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
