package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsLentaSpbV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.Heap.MINUS;
import static com.example.advantumconverter.enums.ExcelType.RS_LENTA_SPB;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public abstract class AbstractConvertServiceImplRsLentaCity extends ConvertServiceBase implements ConvertService {

    private static final String COLUMN_C_8023 = "8023";
    private static final String PALLETA = "Паллета";
    private static final String ROLIKS = "ролики";
    private static final String DOUBLE_DOT = ":";
    private static final String DOUBLE_ZERO = "00";
    private static final String ZERO = "0";
    private static final String TARA = "тара";
    private static final String ALKO = "Алко";

    private static final Map<String, Integer> TYPE_GM_MAP =
            Map.of(PALLETA, 300,
                    ROLIKS, 150);

    private static final Set<String> WHITE_LIST_FORMAT =
            Set.of("СМ", "ГМ", "АЛКО", "РЦ");

    private List<String> warnings = new ArrayList<>();

    @Override
    public ExcelType getExcelType() {
        return RS_LENTA_SPB;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 RS внутренний" + COMPANY_NAME_LENTA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        var mainListData = readMainList(book, "Исходные данные заказов", 3);
        var spWindowsData = readSpWindowsList(book, "Сп-к ТТ-окна", 3);
        var spParamsData = readSpParamsList(book, "СП -параметры ТК", 2);
        var svodData = readSvodList(book, "Свод", 1);
        try {
            mainListData.forEach(
                    reisMain -> data.add(prepareData(reisMain, spWindowsData, spParamsData, svodData))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Обработка всех листов", -1, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsLentaClientV2, "Orders");
    }

    private Pair<String, String> prepareDateStartEnd(String date, String timeStart, String timeEnd) throws ParseException {
        if (EMPTY.equals(timeStart) || EMPTY.equals(timeEnd)) {
            return Pair.of(EMPTY, EMPTY);
        }
        var dateTime1 = convertDateFormat(date + " " + timeStart, TEMPLATE_DATE_TIME_DOT);
        var dateTime2 = convertDateFormat(date + " " + timeEnd, TEMPLATE_DATE_TIME_DOT);

        var dateStart = dateTime1.before(dateTime2) ? dateTime1 : DateUtils.addDays(dateTime1, -1);

        return Pair.of(
                convertDateFormat(dateStart, TEMPLATE_DATE_TIME_DOT),
                convertDateFormat(dateTime2, TEMPLATE_DATE_TIME_DOT)
        );
    }

    private ConvertedListDataRsLentaSpbV2 prepareData(
            ReisMain reisMain,
            Map<String, SpWindows> spWindowsData,
            Map<String, SpParams> spParamsData,
            Map<String, Svod> swodData) {
        var window = spWindowsData.get(reisMain.getNumberYr());

        Pair<String, String> time1 = Pair.of(EMPTY, EMPTY);
        Pair<String, String> time2 = Pair.of(EMPTY, EMPTY);
        Pair<String, String> time3 = Pair.of(EMPTY, EMPTY);
        if (window == null) {
            warnings.add("Не найдены данные по тк на листе Сп-к ТТ-окна, номер: " + reisMain.getNumberYr());
        } else {
            try {
                var dateString = convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);
                time1 = prepareDateStartEnd(dateString, window.getTimeStart1(), window.getTimeEnd1());
                time2 = prepareDateStartEnd(dateString, window.getTimeStart2(), window.getTimeEnd2());
                time3 = prepareDateStartEnd(dateString, window.getTimeStart3(), window.getTimeEnd3());
            } catch (Exception ex) {
                warnings.add("не смогли собрать дату для строки: " + reisMain.getNumberYr());
            }
        }
        String typeGm = EMPTY;
        String tonnageMax = EMPTY;
        String teg = EMPTY;
        var params = spParamsData.get(reisMain.getNumberYr());

        if (params == null) {
            warnings.add("Не найдены данные по тк на листе СП -параметры ТК, номер: " + reisMain.getNumberYr());
        } else {
            typeGm = params.getTypeGm();
            var swod = swodData.get(reisMain.getNumberYr());

            if (swod == null) {
                tonnageMax = params.getTonnageMax();
                teg = EMPTY;
            } else {
                if (ALKO.equalsIgnoreCase(swod.getFormat())) {
                    tonnageMax = params.getTonnageMax() + "; " + swod.getFormat();
                }
                teg = swod.getFormat();
            }
        }
        return ConvertedListDataRsLentaSpbV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisMain.getDateDelivery())
                .setColumnCdata(COLUMN_C_8023)
                .setColumnDdata(reisMain.getNumberYr())
                .setColumnEdata(reisMain.getCity() + " " + reisMain.getAddress())
                .setColumnFdata(time1.getFirst())
                .setColumnGdata(time1.getSecond())
                .setColumnHdata(time2.getFirst())
                .setColumnIdata(time2.getSecond())
                .setColumnJdata(time3.getFirst())
                .setColumnKdata(time3.getSecond())
                .setColumnLdata(1)
                .setColumnMdata(typeGm)
                .setColumnNdata(TYPE_GM_MAP.getOrDefault(typeGm, 0))
                .setColumnOdata(tonnageMax)
                .setColumnPdata(TARA.equalsIgnoreCase(reisMain.getTara()) ? 1 : 0)
                .setColumnRdata(teg)
                .setTechCountRepeat(reisMain.getPalletCount())
                .build();
    }


    private Map<String, Svod> readSvodList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<String, Svod>();
            var sheet = book.getSheet(listName);
            if (sheet == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheet, row, 0)); ++row) {
                sheetData.put(numberYr,
                        Svod.init()
                                .setNumberYr(numberYr)
                                .setFormat(getCellValue(sheet, row, 1))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private List<ReisMain> readMainList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new ArrayList<ReisMain>();
            var sheetMain = book.getSheet(listName);
            if (sheetMain == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 5)); ++row) {
                sheetData.add(
                        ReisMain.init()
                                .setDateDelivery(getCellDate(sheetMain, row, 3))
                                .setNumberYr(numberYr)
                                .setCity(getCellValue(sheetMain, row, 7))
                                .setAddress(getCellValue(sheetMain, row, 8))
                                .setPalletCount(getIntegerValue(sheetMain, row, 10, 0))
                                .setTara(getCellValue(sheetMain, row, 13))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }


    private Map<String, SpWindows> readSpWindowsList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<String, SpWindows>();
            var sheetMain = book.getSheet(listName);
            if (sheetMain == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 3)); ++row) {
                var timesFromFile1 = getCellValue(sheetMain, row, 5);
                var timesFromFile2 = getCellValue(sheetMain, row, 6);
                var timesFromFile3 = getCellValue(sheetMain, row, 7);

                sheetData.put(
                        numberYr,
                        SpWindows.init()
                                .setNumberYr(numberYr)
                                .setTimeStart1(getTime(numberYr, timesFromFile1, 0))
                                .setTimeEnd1(getTime(numberYr, timesFromFile1, 1))
                                .setTimeStart2(getTime(numberYr, timesFromFile2, 0))
                                .setTimeEnd2(getTime(numberYr, timesFromFile2, 1))
                                .setTimeStart3(getTime(numberYr, timesFromFile3, 0))
                                .setTimeEnd3(getTime(numberYr, timesFromFile3, 1))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private String getTime(String numberYr, String timesFromFile, int index) {
        if (EMPTY.equals(timesFromFile) || timesFromFile.equals("-")) {
            return EMPTY;
        }
        try {
            var times = timesFromFile.split(MINUS);
            var time = times[index].split(DOUBLE_DOT);
            var timeH = time[0].length() == 2 ? time[0] : time[0].replace(DOUBLE_ZERO, ZERO);
            return timeH + ":" + time[1];
        } catch (Exception ex) {
            warnings.add("Не обработать время для рейса: " + numberYr + ", время: " + timesFromFile);
            return EMPTY;
        }
    }

    private Map<String, SpParams> readSpParamsList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<String, SpParams>();
            var sheetMain = book.getSheet(listName);
            if (sheetMain == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 0)); ++row) {
                sheetData.put(
                        numberYr,
                        SpParams.init()
                                .setNumberYr(numberYr)
                                .setTypeGm(getCellValue(sheetMain, row, 2))
                                .setTonnageMax(getCellValue(sheetMain, row, 3))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisMain {
        @EqualsAndHashCode.Include
        private String numberYr;
        private Date dateDelivery;
        private String city;
        private String address;
        private Integer palletCount;
        private String tara;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class SpWindows {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String timeStart1;
        private String timeEnd1;
        private String timeStart2;
        private String timeEnd2;
        private String timeStart3;
        private String timeEnd3;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    private static class SpParams {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String typeGm;
        private String tonnageMax;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Svod {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String format;
    }

}
