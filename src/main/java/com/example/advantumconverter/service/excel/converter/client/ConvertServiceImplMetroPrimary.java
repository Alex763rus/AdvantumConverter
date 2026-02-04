package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.tgcommons.constant.Constant;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_METRO_PRIMARY;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_METRO;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_METRO_PRIMARY;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplMetroPrimary extends ConvertServiceBase implements ConvertService {
    private int START_ROW;

    private int LAST_ROW;

    private static final String METRO_GROUP_LOGISTIC = "Метро Групп Логистик";
    private static final String DC_NOGINSK = "DC Noginsk";
    private static final String DC_NOGINSK_RUS = "РЦ НОГИНСК";
    private static final String INN = "84563932";


    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_METRO_PRIMARY;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_METRO_PRIMARY;
    }

    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_METRO, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        var mainListData = readMainList(book, "manual", 5);
        var windowsListData = readWindowsList(book, "Лист1", 5, mainListData);
        try {
            mainListData.values().forEach(
                    reisMain -> data.addAll(prepareData(reisMain, windowsListData))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Обработка всех листов", -1, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    private List<ConvertedListDataV2> prepareData(ReisMain reisMain, Map<String, List<Windows>> windowsListData) {
        var windows = windowsListData.get(reisMain.getNumberYr());

        if (CollectionUtils.isEmpty(windows)) {
            warnings.add("Не найдены данные по рейсам на листе Лист1, номер: " + reisMain.getNumberYr());
            return List.of(ConvertedListDataClientsV2.init().build());
        }
        var results = new ArrayList<ConvertedListDataV2>();
        var isStart = true;
        int numberUnloadingCounter = 0;
        ConvertedListDataClientsV2 dataLine;
        try {
            var dateDelivery = convertDateFormat(reisMain.getDateDelivery(), TEMPLATE_DATE_DOT);

            for (int i = 0; i < windows.size(); ++i) {
                var window = windows.get(i);
                dataLine = ConvertedListDataClientsV2.init()
                        .setColumnAdata(reisMain.getNumberYr())
                        .setColumnBdata(dateDelivery)
                        .setColumnCdata(METRO_GROUP_LOGISTIC)
                        .setColumnDdata(reisMain.getCarrier())
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(Constant.TextConstants.EMPTY)
                        .setColumnHdata(Constant.TextConstants.EMPTY)
                        .setColumnIdata(null)
                        .setColumnJdata(3000)
                        .setColumnKdata(0)
                        .setColumnLdata(window.getTemperatureMin())
                        .setColumnMdata(window.getTemperatureMax())
                        .setColumnNdata(null)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(fillS(isStart, window, reisMain))
                        .setColumnTdata(fillT(isStart, window, reisMain))
                        .setColumnUdata(isStart ? DC_NOGINSK : window.getAddressCode())
                        .setColumnVdata(isStart ? DC_NOGINSK_RUS : window.getAddressFull())
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(isStart ? 0 : numberUnloadingCounter)
                        .setColumnYdata(null)
                        .setColumnZdata(null)
                        .setColumnAaData(INN)
                        .setColumnAbData(Constant.TextConstants.EMPTY)
                        .setColumnAcData(reisMain.getCarNumber())
                        .setColumnAdData(reisMain.getCarTrailer())
                        .setColumnAeData(reisMain.getCarDriver())
                        .setColumnAfData(null)
                        .setColumnAgData(Constant.TextConstants.EMPTY)
                        .setColumnAhData(Constant.TextConstants.EMPTY)
                        .setColumnAiData(null)
                        .setColumnAjData(Constant.TextConstants.EMPTY)
                        .setColumnAkData(Constant.TextConstants.EMPTY)
                        .setColumnAlData(Constant.TextConstants.EMPTY)
                        .setColumnAmData(Constant.TextConstants.EMPTY)
                        .setColumnAnData(Constant.TextConstants.EMPTY)
                        .setColumnAoData(Constant.TextConstants.EMPTY)
                        .build();
                ++numberUnloadingCounter;
                results.add(dataLine);
                if (isStart) {
                    --i;
                    isStart = false;
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (подробно)", 0, e.getMessage()));//todo вывести номер строки
        }
        return results;
    }

    private Date fillT(boolean isStart, Windows window, ReisMain reisMain) throws ParseException {
        if (isStart) {
            return DateUtils.addHours(reisMain.getDateIncome(), 1);
        }
        return convertDateFormat(window.getTimeEnd(), TEMPLATE_DATE_TIME_DOT);
    }

    private Date fillS(boolean isStart, Windows window, ReisMain reisMain) throws ParseException {
        return isStart ? reisMain.getDateIncome() : convertDateFormat(window.getTimeStart(), TEMPLATE_DATE_TIME_DOT);
    }

    private Map<String, ReisMain> readMainList(XSSFWorkbook book, String listName, int row) {
        try {
            var sheetData = new HashMap<String, ReisMain>();
            var sheetMain = book.getSheet(listName);
            if (sheetMain == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            var dateDelivery = getCellValue(sheetMain, row, 1);
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 4)); ++row) {
                var dateIncome = getCellValue(sheetMain, row, 19);
                sheetData.put(
                        numberYr,
                        ReisMain.init()
                                .setDateDelivery(dateDelivery)
                                .setCarrier(getCellValue(sheetMain, row, 3))
                                .setNumberYr(numberYr)
                                .setCarNumber(getCellValue(sheetMain, row, 6))
                                .setCarDriver(getCellValue(sheetMain, row, 7))
                                .setCarTrailer(getCellValue(sheetMain, row, 8))
                                .setDateIncome(convertDateFormat(dateIncome, TEMPLATE_DATE_TIME_DOT))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private Map<String, List<Windows>> readWindowsList(XSSFWorkbook book, String listName, int row, Map<String, ReisMain> readMainList) {
        try {
            var sheetData = new HashMap<String, List<Windows>>();
            var sheetMain = book.getSheet(listName);
            if (sheetMain == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 2)); ++row) {
                if (!readMainList.containsKey(numberYr)) {
                    continue;
                }
                var windows = sheetData.getOrDefault(
                        numberYr, new ArrayList<>()
                );
                windows.add(
                        Windows.init()
                                .setNumberYr(numberYr)
                                .setAddressCode(getCellValue(sheetMain, row, 5))
                                .setAddressFull(getCellValue(sheetMain, row, 6))
                                .setTimeStart(getCellValue(sheetMain, row, 7))
                                .setTimeEnd(getCellValue(sheetMain, row, 8))
                                .setTemperatureMin(getIntegerValue(sheetMain, row, 14, 0))
                                .setTemperatureMax(getIntegerValue(sheetMain, row, 15, 0))
                                .build()
                );
                sheetData.put(numberYr, windows);
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
        private String dateDelivery;
        private String carrier;
        private String carNumber;
        private String carDriver;
        private String carTrailer;
        private Date dateIncome;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Windows {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String addressCode;
        private String addressFull;
        private String timeStart;
        private String timeEnd;
        private Integer temperatureMin;
        private Integer temperatureMax;
    }

}
