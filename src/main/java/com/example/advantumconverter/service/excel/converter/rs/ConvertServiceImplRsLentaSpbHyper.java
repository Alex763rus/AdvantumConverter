package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.ExcelValidationException;
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
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA_SPB_HYPER;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA_SPB_HYPER;
import static com.example.advantumconverter.constant.Constant.Heap.MINUS;
import static com.example.advantumconverter.enums.ExcelType.RS_LENTA_SPB;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplRsLentaSpbHyper extends ConvertServiceBase implements ConvertService {


    private static final String COLUMN_C_8023 = "8023";
    private static final String PALLETA = "Паллета";
    private static final String ROLIKS = "ролики";
    private static final String DOUBLE_DOT = ":";
    private static final String DOUBLE_ZERO = "00";
    private static final String ZERO = "0";
    private static final String TARA = "тара";
    private static final String ALKO = "АЛКО";

    private static final LocalTime TIME_18 = LocalTime.of(18, 0);

    private static final Map<String, Integer> TYPE_GM_MAP =
            Map.of(PALLETA, 300,
                    ROLIKS, 150);

    private static final Map<String, String> TOVAR_GROUP = Map.ofEntries(
            // Категория СОФ
            Map.entry("Склад A", "СОФ"),
            Map.entry("Склад E Производство", "СОФ"),
            Map.entry("Остаток СОФ", "СОФ"),

            // Категория СХ
            Map.entry("Склад C", "СХ"),
            Map.entry("Остаток СХ", "СХ"),
            Map.entry("Склад B", "СХ"),
            Map.entry("Склад D", "СХ"),
            Map.entry("Транзит", "СХ"),

            // Категория Заморозка
            Map.entry("Склад E Заморозка", "Заморозка"),
            Map.entry("Остаток Заморозка", "Заморозка")
    );

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
    public String getConverterName() {
        return FILE_NAME_RS_LENTA_SPB_HYPER;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_LENTA_SPB_HYPER;
    }


    @Override
    @LogExecutionTime(value = "Конвертация v2 RS внутренний" + COMPANY_NAME_LENTA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        var mainListData = readMainList(book, "Исходные данные заказов", 3);
        var svodData = readSvodList(book, "Сводная", 1);
        var orderData = readOrderList(book, "Заказ-РЦ", 1);
        try {
            mainListData.forEach(
                    reisMain -> data.add(prepareData(reisMain, svodData, orderData))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Обработка всех листов", -1, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsLentaClientV2, "Orders");
    }


    private LocalTime getTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().toLocalTime();
    }

    private Pair<String, String> prepareDateStartEnd(String date,
                                                     String timeStart,
                                                     String timeEnd,
                                                     LocalTime windowStart,
                                                     Integer squize) throws ParseException {
        if (EMPTY.equals(timeStart) || EMPTY.equals(timeEnd)) {
            return Pair.of(EMPTY, EMPTY);
        }
        var dateTime1 = convertDateFormat(date + " " + timeStart, TEMPLATE_DATE_TIME_DOT);
        var dateTime2 = convertDateFormat(date + " " + timeEnd, TEMPLATE_DATE_TIME_DOT);

        // Первое время: Если больше windowStart (18), то вычесть 1 день для первого времени
        // Второе время: Если время первого больше времени второго, И если первое время меньше 18 то прибавить 1 день
        var time1 = getTime(dateTime1);
        if (!time1.isBefore(windowStart)) {
            dateTime1 = DateUtils.addDays(dateTime1, -1);
        } else {
            var time2 = getTime(dateTime2);
            if (time1.isAfter(time2)) {
                dateTime2 = DateUtils.addDays(dateTime2, 1);
            }
        }

        // Если дата+время второго больше даты времени первого более чем на 24 часа, то из второго 1 день вычесть
        long hours = Duration.between(dateTime1.toInstant(), dateTime2.toInstant()).abs().toHours();
        if (hours >= 24) {
            dateTime2 = DateUtils.addDays(dateTime2, -1);
        }

        dateTime1 = DateUtils.addDays(dateTime1, squize);
        dateTime2 = DateUtils.addDays(dateTime2, squize);

        return Pair.of(
                convertDateFormat(dateTime1, TEMPLATE_DATE_TIME_DOT),
                convertDateFormat(dateTime2, TEMPLATE_DATE_TIME_DOT)
        );
    }

    private ConvertedListDataRsLentaSpbV2 prepareData(ReisMain reisMain, Map<Integer, Svod> swodData, Map<Integer, Order> orderData) {
        var dateString = EMPTY;
        Pair<String, String> time1 = Pair.of(EMPTY, EMPTY);
        Pair<String, String> time2 = Pair.of(EMPTY, EMPTY);
        Pair<String, String> time3 = Pair.of(EMPTY, EMPTY);
        Pair<String, String> time4 = Pair.of(EMPTY, EMPTY);

        var svod = swodData.get(reisMain.getTk());
        if (svod == null) {
            warnings.add("Не найдены данные по тк на листе Сводная, номер: " + reisMain.getTk());
        } else {
            try {
                dateString = convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);
                time1 = prepareDateStartEnd(dateString, svod.getTimeStart1(), svod.getTimeEnd1(), TIME_18, svod.getSquiz());
                time2 = prepareDateStartEnd(dateString, svod.getTimeStart2(), svod.getTimeEnd2(), TIME_18, svod.getSquiz());
                time3 = prepareDateStartEnd(dateString, svod.getTimeStart3(), svod.getTimeEnd3(), TIME_18, svod.getSquiz());
                time4 = prepareDateStartEnd(dateString, svod.getTime4Start(), svod.getTime4End(), TIME_18, 0);
            } catch (Exception ex) {
                warnings.add("не смогли собрать дату для строки: " + reisMain.getTk());
            }
        }

        var order = orderData.get(reisMain.getTk());
        if (order == null) {
            warnings.add("Не найдены данные по тк на листе Заказ-РЦ, номер: " + reisMain.getTk());
        }
        var techRepeats = new ArrayList<>(List.of(
                new ConvertedListDataRsLentaSpbV2.Repeat(order.getBaseAcol(), "СОФ", "Склад A"),
                new ConvertedListDataRsLentaSpbV2.Repeat(order.getBaseBcol(), "СХ", "Склад B"),
                new ConvertedListDataRsLentaSpbV2.Repeat(order.getBaseCcol(), "СХ", "Склад C"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseEcolCold(), "Заморозка", "Склад E Заморозка"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseEcolGen(), "СОФ", "Склад E Производство"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseDcol(), "СХ", "Склад D"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseTransit(), "СХ", "Транзит"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseOstatokSof(), "СОФ", "Остаток СОФ"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseOstatokSx(), "СХ", "Остаток СХ"),
                new ConvertedListDataRsLentaSpbV2.Repeat(reisMain.getBaseOstatokCold(), "Заморозка", "Остаток Заморозка")
        ));
        return ConvertedListDataRsLentaSpbV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisMain.getDateDelivery())
                .setColumnCdata(reisMain.getRcComplectation())
                .setColumnDdata(reisMain.getTk().toString())
                .setColumnEdata(svod.getAddress())
                .setColumnFdata(time1.getFirst())
                .setColumnGdata(time1.getSecond())
                .setColumnHdata(time2.getFirst())
                .setColumnIdata(time2.getSecond())
                .setColumnJdata(time3.getFirst())
                .setColumnKdata(time3.getSecond())
                .setColumnLdata(1)
                .setColumnMdata(svod.getTara())
                .setColumnNdata(svod.getUnloadingMinuts() * 60)
                .setColumnOdata(svod.getTonnage())
                .setColumnPdata(TARA.equalsIgnoreCase(reisMain.getTara()) ? 1 : 0)
                .setColumnQdata(svod.getFormat())
                .setColumnRdata(EMPTY.equals(time4.getFirst()) ? EMPTY : time4.getFirst() + "/" + time4.getSecond())
                //========================
                .setTechRepeats(techRepeats)
                .build();
    }

    private Map<Integer, Order> readOrderList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<Integer, Order>();
            var sheet = book.getSheet(listName);
            if (sheet == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            Integer tk;
            for (; (tk = getIntegerValueOrErrorIfFormula(sheet, row, 0, 0)) > 0; ++row) {
                try {
                    sheetData.put(tk,
                            Order.init()
                                    .setTk(tk)
                                    .setBaseAcol(getIntegerValueOrErrorIfFormula(sheet, row, 1, 0))
                                    .setBaseBcol(getIntegerValueOrErrorIfFormula(sheet, row, 2, 0))
                                    .setBaseCcol(getIntegerValueOrErrorIfFormula(sheet, row, 3, 0))
                                    .build()
                    );
                } catch (ExcelValidationException e) {
                    warnings.add("Лист: [" + listName + "], " + e.getMessage());
                }
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }


    private Map<Integer, Svod> readSvodList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<Integer, Svod>();
            var sheet = book.getSheet(listName);
            if (sheet == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            Integer tk;
            for (; (tk = getIntegerValueOrErrorIfFormula(sheet, row, 0, 0)) > 0; ++row) {
                var timesFromFile1 = getCellValue(sheet, row, 5);
                var timesFromFile2 = getCellValue(sheet, row, 6);
                var timesFromFile3 = getCellValue(sheet, row, 7);
                try {
                    sheetData.put(tk,
                            Svod.init()
                                    .setTk(tk)
                                    .setFormat(getCellValue(sheet, row, 1))
                                    .setAddress(getCellValue(sheet, row, 2))
                                    .setTara(getCellValue(sheet, row, 3))
                                    .setTonnage(getCellValue(sheet, row, 4))
                                    .setTimeStart1(getTime(tk.toString(), timesFromFile1, 0))
                                    .setTimeEnd1(getTime(tk.toString(), timesFromFile1, 1))
                                    .setTimeStart2(getTime(tk.toString(), timesFromFile2, 0))
                                    .setTimeEnd2(getTime(tk.toString(), timesFromFile2, 1))
                                    .setTimeStart3(getTime(tk.toString(), timesFromFile3, 0))
                                    .setTimeEnd3(getTime(tk.toString(), timesFromFile3, 1))
                                    .setTime4Start(getCellValue(sheet, row, 8))
                                    .setTime4End(getCellValue(sheet, row, 9))
                                    .setUnloadingMinuts(getIntegerValueOrErrorIfFormula(sheet, row, 10, 0))
                                    .setSquiz(getIntegerValueOrErrorIfFormula(sheet, row, 11, 0))
                                    .build()
                    );
                } catch (ExcelValidationException e) {
                    warnings.add("Лист: [" + listName + "], " + e.getMessage());
                }
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
            Integer tk;
            for (; (tk = getIntegerValueOrErrorIfFormula(sheetMain, row, 2, 0)) > 0; ++row) {
                try {
                    sheetData.add(
                            ReisMain.init()
                                    .setTk(tk)
                                    .setDateDelivery(getCellDate(sheetMain, row, 0))
                                    .setRcComplectation(getCellValue(sheetMain, row, 1))
                                    .setBaseEcolCold(getIntegerValueOrErrorIfFormula(sheetMain, row, 7, 0))
                                    .setBaseEcolGen(getIntegerValueOrErrorIfFormula(sheetMain, row, 8, 0))
                                    .setBaseDcol(getIntegerValueOrErrorIfFormula(sheetMain, row, 9, 0))
                                    .setBaseTransit(getIntegerValueOrErrorIfFormula(sheetMain, row, 10, 0))
                                    .setTara(getCellValue(sheetMain, row, 11))
                                    .setBaseOstatokSof(getIntegerValueOrErrorIfFormula(sheetMain, row, 13, 0))
                                    .setBaseOstatokSx(getIntegerValueOrErrorIfFormula(sheetMain, row, 14, 0))
                                    .setBaseOstatokCold(getIntegerValueOrErrorIfFormula(sheetMain, row, 15, 0))
                                    .build()
                    );
                } catch (ExcelValidationException e) {
                    warnings.add("Лист: [" + listName + "], " + e.getMessage());
                }
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

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisMain {
        @EqualsAndHashCode.Include
        private Integer tk;
        private Date dateDelivery;
        private String rcComplectation;     // РЦ комплектации
        private Integer baseEcolCold;       // Склад E Заморозка
        private Integer baseEcolGen;        // Склад E Производство
        private Integer baseDcol;
        private Integer baseTransit;
        private String tara;
        private Integer baseOstatokSof;     // Остаток СОФ
        private Integer baseOstatokSx;      // Остаток СХ
        private Integer baseOstatokCold;    // Остаток Заморозка
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Order {
        @EqualsAndHashCode.Include
        private Integer tk;
        private Integer baseAcol;
        private Integer baseBcol;
        private Integer baseCcol;
    }


    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Svod {
        @EqualsAndHashCode.Include
        private Integer tk;
        private String format;
        private String address;
        private String tara;
        private String tonnage;
        private String timeStart1;
        private String timeEnd1;
        private String timeStart2;
        private String timeEnd2;
        private String timeStart3;
        private String timeEnd3;
        private String time4Start;
        private String time4End;
        private Integer unloadingMinuts;
        private Integer squiz;
    }

}
