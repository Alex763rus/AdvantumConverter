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
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA_MSK;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA_MSK;
import static com.example.advantumconverter.enums.ExcelType.RS_LENTA_SPB;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplRsLentaMsk extends ConvertServiceBase implements ConvertService {

    @Override
    public ExcelType getExcelType() {
        return RS_LENTA_SPB;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_RS_LENTA_MSK;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_LENTA_MSK;
    }

    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    private static final String PALLETA = "Паллета";
    private static final String ROLIKS = "ролики";

    @Override
    @LogExecutionTime(value = "Конвертация v2 RS внутренний" + COMPANY_NAME_LENTA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        var mainListData = readMainList(book);
        var spWindowsData = readSpWindowsList(book);
        var spParamsData = readSpParamsList(book);
        var svodData = readSvodList(book);
        try {
            mainListData.forEach(
                    reisMain -> data.add(prepareData(reisMain, spWindowsData, spParamsData, svodData))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Обработка всех листов", -1, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsLentaClientV2, "Orders");
    }

    private ConvertedListDataRsLentaSpbV2 prepareData(
            ReisMain reisMain,
            Map<String, SpWindows> spWindowsData,
            Map<String, SpParams> spParamsData,
            Map<String, Svod> swodData) {
        var window = spWindowsData.get(reisMain.getNumberYr());

        String windowsStart1 = EMPTY;
        String windowsEnd1 = EMPTY;
        String windowsStart2 = EMPTY;
        String windowsEnd2 = EMPTY;
        String windowsStart3 = EMPTY;
        String windowsEnd3 = EMPTY;
        if (window == null) {
            warnings.add("Не найдены окна для рейса: " + reisMain.getNumberYr() + NEW_LINE);
        } else {
            reisMain.getDateDelivery();

            try {
                if (!EMPTY.equals(window.getTimeStart1()) && !EMPTY.equals(window.getTimeEnd1())) {
                    var dateString =
                            convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);

                    var dateTime1 = convertDateFormat(dateString + " " + window.getTimeStart1(), TEMPLATE_DATE_TIME_DOT);
                    var dateTime2 = convertDateFormat(dateString + " " + window.getTimeEnd1(), TEMPLATE_DATE_TIME_DOT);

                    var dateStart = dateTime1.before(dateTime2) ? dateTime1 : DateUtils.addDays(dateTime1, -1);

                    windowsStart1 = convertDateFormat(dateStart, TEMPLATE_DATE_TIME_DOT);
                    windowsEnd1 = convertDateFormat(dateTime2, TEMPLATE_DATE_TIME_DOT);
                }
                if (!EMPTY.equals(window.getTimeStart2()) && !EMPTY.equals(window.getTimeEnd2())) {
                    var dateString =
                            convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);

                    var dateTime1 = convertDateFormat(dateString + " " + window.getTimeStart2(), TEMPLATE_DATE_TIME_DOT);
                    var dateTime2 = convertDateFormat(dateString + " " + window.getTimeEnd2(), TEMPLATE_DATE_TIME_DOT);

                    var dateStart = dateTime1.before(dateTime2) ? dateTime1 : DateUtils.addDays(dateTime1, -1);

                    windowsStart2 = convertDateFormat(dateStart, TEMPLATE_DATE_TIME_DOT);
                    windowsEnd2 = convertDateFormat(dateTime2, TEMPLATE_DATE_TIME_DOT);
                }
                if (!EMPTY.equals(window.getTimeStart3()) && !EMPTY.equals(window.getTimeEnd3())) {
                    var dateString =
                            convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);

                    var dateTime1 = convertDateFormat(dateString + " " + window.getTimeStart3(), TEMPLATE_DATE_TIME_DOT);
                    var dateTime2 = convertDateFormat(dateString + " " + window.getTimeEnd3(), TEMPLATE_DATE_TIME_DOT);

                    var dateStart = dateTime1.before(dateTime2) ? dateTime1 : DateUtils.addDays(dateTime1, -1);

                    windowsStart3 = convertDateFormat(dateStart, TEMPLATE_DATE_TIME_DOT);
                    windowsEnd3 = convertDateFormat(dateTime2, TEMPLATE_DATE_TIME_DOT);
                }
            } catch (Exception ex) {
                warnings.add("не смогли собрать дату для строки: " + reisMain.getNumberYr() + NEW_LINE);
            }
        }
        String typeGm = EMPTY;
        String tonnageMax = EMPTY;
        var params = spParamsData.get(reisMain.getNumberYr());
        if (params == null) {
            warnings.add("Не найдены параметры для рейса: " + reisMain.getNumberYr() + NEW_LINE);
        } else {
            typeGm = params.getTypeGm();
            var swod = swodData.get(reisMain.getNumberYr());
            tonnageMax = params.getTonnageMax() +
                    (swod == null ? EMPTY : ("; " + swod.getFormat()));
        }
        return ConvertedListDataRsLentaSpbV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisMain.getDateDelivery())
                .setColumnCdata("8023")
                .setColumnDdata(reisMain.getNumberYr())
                .setColumnEdata(reisMain.getCity() + " " + reisMain.getAddress())
                .setColumnFdata(windowsStart1)
                .setColumnGdata(windowsEnd1)
                .setColumnHdata(windowsStart2)
                .setColumnIdata(windowsEnd2)
                .setColumnJdata(windowsStart3)
                .setColumnKdata(windowsEnd3)
                .setColumnLdata(1)
                .setColumnMdata(typeGm)
                .setColumnNdata(PALLETA.equalsIgnoreCase(typeGm) ? 300 :
                        ROLIKS.equalsIgnoreCase(typeGm) ? 150 : 0)
                .setColumnOdata(tonnageMax)
                .setTechCountRepeat(reisMain.getPalletCount())
                .build();
    }

    private Map<String, Svod> readSvodList(XSSFWorkbook book) {
        int row = 1;
        String listName = "Свод";
        try {
            var sheetData = new HashMap<String, Svod>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 0)); ++row) {
                var format = getCellValue(sheetMain, row, 1);
                if (!format.equals("Алко")) {
                    continue;
                }
                sheetData.put(numberYr,
                        Svod.init()
                                .setNumberYr(numberYr)
                                .setFormat(format)
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private List<ReisMain> readMainList(XSSFWorkbook book) {
        int row = 3;
        String listName = "Исходные данные заказов";
        try {
            var sheetData = new ArrayList<ReisMain>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 5)); ++row) {
                sheetData.add(
                        ReisMain.init()
                                .setDateDelivery(getCellDate(sheetMain, row, 3))
                                .setNumberYr(numberYr)
                                .setCity(getCellValue(sheetMain, row, 7))
                                .setAddress(getCellValue(sheetMain, row, 8))
                                .setPalletCount(getIntegerValue(sheetMain, row, 10, 0))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }


    private Map<String, SpWindows> readSpWindowsList(XSSFWorkbook book) {
        int row = 3;
        String listName = "Сп-к ТТ-окна";
        try {
            var sheetData = new HashMap<String, SpWindows>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 3)); ++row) {
                var timesFromFile1 = getCellValue(sheetMain, row, 5);
                var timesFromFile2 = getCellValue(sheetMain, row, 6);
                var timesFromFile3 = getCellValue(sheetMain, row, 7);

                sheetData.put(
                        numberYr,
                        SpWindows.init()
                                .setNumberYr(numberYr)
                                .setTimeStart1(getTimeStart(numberYr, timesFromFile1))
                                .setTimeEnd1(getTimeEnd(numberYr, timesFromFile1))
                                .setTimeStart2(getTimeStart(numberYr, timesFromFile2))
                                .setTimeEnd2(getTimeEnd(numberYr, timesFromFile2))
                                .setTimeStart3(getTimeStart(numberYr, timesFromFile3))
                                .setTimeEnd3(getTimeEnd(numberYr, timesFromFile3))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private String getTimeStart(String numberYr, String timesFromFile) {
        if (EMPTY.equals(timesFromFile) || timesFromFile.equals("-")) {
            return EMPTY;
        }
        try {
            var times = timesFromFile.split("-");
            var timeStart = times[0].split(":");
            var timeStartH = timeStart[0].length() == 2 ?
                    timeStart[0] :
                    timeStart[0].replace("00", "0");
            return timeStartH + ":" + timeStart[1];
        } catch (Exception ex) {
            warnings.add("Не обработать время старта для рейса: " + numberYr + ", время: " + timesFromFile + NEW_LINE);
            return EMPTY;
        }
    }

    private String getTimeEnd(String numberYr, String timesFromFile) {
        if (EMPTY.equals(timesFromFile) || timesFromFile.equals("-")) {
            return EMPTY;
        }
        try {
            var times = timesFromFile.split("-");
            var timeEnd = times[1].split(":");

            var timeEndH = timeEnd[0].length() == 2 ?
                    timeEnd[0] :
                    timeEnd[0].replace("00", "0");
            return timeEndH + ":" + timeEnd[1];
        } catch (Exception ex) {
            warnings.add("Не обработать время окончания для рейса: " + numberYr + ", время: " + timesFromFile + NEW_LINE);
            return EMPTY;
        }
    }

    private Map<String, SpParams> readSpParamsList(XSSFWorkbook book) {
        int row = 2;
        String listName = "СП -параметры ТК";
        try {
            var sheetData = new HashMap<String, SpParams>();
            var sheetMain = book.getSheet(listName);
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
    private static class ReisFull {
        private Integer pointNumber;
        private String pointName;
        private String pointType;
        private Integer cargoSpace; //грузомест
        private Double cargoWeight; //вес груза
        private Double cargoVolume; //объем груза
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
