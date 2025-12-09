package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import com.example.advantumconverter.service.excel.converter.rs.AbstractConvertServiceImplRsLentaCity;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_FRAGRANT_WORLD_SPB;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_FRAGRANT_WORLD_MSK;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_FRAGRANT_WORLD_SPB;
import static com.example.advantumconverter.constant.Constant.Heap.MINUS;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplFragrantWorldSpb extends ConvertServiceBase implements ConvertService {

    @Override
    public String getConverterName() {
        return FILE_NAME_FRAGRANT_WORLD_SPB;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_FRAGRANT_WORLD_SPB;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    private final int START_ROW = 1;
    private List<String> warnings = new ArrayList<>();

    private static final String COLUMN_C_8023 = "8023";
    private static final String PALLETA = "Паллета";
    private static final String ROLIKS = "ролики";
    private static final String ALKO = "Алко";
    private static final String DOUBLE_DOT = ":";
    private static final String DOUBLE_ZERO = "00";
    private static final String ZERO = "0";
    private static final String TEMPLATE_DATE_AND_H_M = "ddMMyyH:mm";

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_FRAGRANT_WORLD_MSK, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var addresses = readAddresses(book, "Адреса магазинов", 1);
        var dominoData = readDomino(book, "ДЛЯ ДОМИНО", 1);
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;
        boolean isStart = true;
        var startRowMain = 1;
        int rowMain = startRowMain;
        String reisNumber;
        try {
            Date dateOrder = null;
            String dateOrderString = null;
            var reisNumberTmp = EMPTY;
            var numberUnloading = 0;
            for (var rowData : dominoData.entrySet()) {
                var rowDataDomino = rowData.getValue();
                if (dateOrderString == null) { //берем один раз для всего файла
                    dateOrder = rowDataDomino.getDateOrder();
                    dateOrderString = convertDateFormat(rowDataDomino.getDateOrder(), TEMPLATE_DATE);
                }
                reisNumber = rowDataDomino.getKey().getReisNumber();
                isStart = !reisNumber.equals(reisNumberTmp);
                if (isStart) {
                    reisNumberTmp = reisNumber;
                    numberUnloading = 0;
                }
                ++numberUnloading;
                var address = addresses.get(rowData.getValue().getPointName());
                if (address == null) {
                    throw new ValidationException(String.format("Не смогли подобрать адрес с листа Адреса магазинов для для значения: %s",
                            rowData.getValue().getPointName()));
                }
                var reisNumberResult = reisNumber + "_" + dateOrderString;
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {

                    Date dateSResult = null;
                    String dateSString = null;
                    Date dateTResult = null;
                    String dateTString = null;

                    if (isStart) {
                        //S:
                        var timeStart = !EMPTY.equals(rowDataDomino.getTimeStart()) ? rowDataDomino.getTimeStart() : "10:00";
                        dateSString = dateOrderString + timeStart;
                        dateSResult = DateUtils.addMinutes(convertDateFormat(dateSString, TEMPLATE_DATE_AND_H_M), -20);
                        //T:
                        var timeEnd = !EMPTY.equals(rowDataDomino.getTimeEnd()) ? rowDataDomino.getTimeEnd() : "18:00";
                        dateTString = dateOrderString + timeEnd;
                        dateTResult = DateUtils.addHours(convertDateFormat(dateTString, TEMPLATE_DATE_AND_H_M), 2);
                    } else {
                        //S:
                        dateSString = dateOrderString + getTime(reisNumber, address.getTime(), 0);
                        dateSResult = convertDateFormat(dateSString, TEMPLATE_DATE_AND_H_M);
                        //T:
                        dateTString = dateOrderString + getTime(reisNumber, address.getTime(), 1);
                        dateTResult = convertDateFormat(dateTString, TEMPLATE_DATE_AND_H_M);
                    }

                    var needTemperage = reisNumber.contains("_РЕФ");
                    dataLine = ConvertedListDataClientsV2.init()
                            .setColumnAdata(reisNumberResult)
                            .setColumnBdata(dateOrder)
                            .setColumnCdata(COMPANY_NAME_FRAGRANT_WORLD_MSK)
                            .setColumnDdata(rowDataDomino.getOrganization())
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(rowDataDomino.getTonnage())
                            .setColumnKdata(rowDataDomino.getPackageCount())
                            .setColumnLdata(needTemperage ? 2 : null)
                            .setColumnMdata(needTemperage ? 5 : null)
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(dateSResult)
                            .setColumnTdata(dateTResult)
                            .setColumnUdata(isStart ? "Осиновая Роща" : rowDataDomino.getPointName())
                            .setColumnVdata(isStart ? "Осиновая Роща" : address.getCity() + SPACE + address.getAddress())
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 0 : numberUnloading)
                            .setColumnYdata(null)
                            .setColumnZdata(null)
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(rowDataDomino.getCarNumber())
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(rowDataDomino.getFio())
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .setColumnAiData(null)
                            .setColumnAjData(EMPTY)
                            .setColumnAkData(EMPTY)
                            .setColumnAlData(EMPTY)
                            .setColumnAmData(EMPTY)
                            .setColumnAnData(EMPTY)
                            .setColumnAoData(EMPTY)
                            .setColumnApData(null)
                            .setColumnAqData(null)
                            .setColumnArData(null)
                            .setTechFullFio(null)
                            .build();
                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, rowMain, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    private String getTime(String numberYr, String timesFromFile, int index) {
        if (StringUtils.EMPTY.equals(timesFromFile) || timesFromFile.equals("-")) {
            return StringUtils.EMPTY;
        }
        try {
            var times = timesFromFile.split(MINUS);
            var time = times[index].split(DOUBLE_DOT);
            var timeH = time[0].length() == 2 ? time[0] : time[0].replace(DOUBLE_ZERO, ZERO);
            return timeH + ":" + time[1];
        } catch (Exception ex) {
            warnings.add("Не обработать время для рейса: " + numberYr + ", время: " + timesFromFile);
            return StringUtils.EMPTY;
        }
    }


    private Map<String, AddressData> readAddresses(XSSFWorkbook book, String listName, int row) {
        Map<String, AddressData> data = new HashMap<>();
        try {
            String shopName;
            var sheet = book.getSheet(listName);
            if (sheet == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            for (; !StringUtils.EMPTY.equals(shopName = getCellValue(sheet, row, 0)); ++row) {
                data.put(shopName,
                        AddressData.init()
                                .setShopName(shopName)
                                .setAddress(getCellValue(sheet, row, 1))
                                .setCity(getCellValue(sheet, row, 2))
                                .setTime(getCellValue(sheet, row, 7))
                                .build()
                );
            }
            return data;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private Map<DominoDataKey, DominoData> readDomino(XSSFWorkbook book, String listName, int row) {
        Map<DominoDataKey, DominoData> data = new HashMap<>();
        try {
            String reisNumber;
            var sheet = book.getSheet(listName);
            if (sheet == null) {
                throw new ValidationException(String.format("Не найден лист с названием: [%s], обработка невозможна.", listName));
            }
            for (; !StringUtils.EMPTY.equals(reisNumber = getCellValue(sheet, row, 10)); ++row) {
                var dateOrder = convertDateFormat(getCellValue(sheet, row, 2)
                        .replaceAll("\"", "")
                        .replaceAll("/", "."), TEMPLATE_DATE_DOT);
                var dominoDataKey = DominoDataKey.init()
                        .setReisNumber(reisNumber)
                        .setShopNumber(getCellValue(sheet, row, 9))
                        .build();
                if (data.containsKey(dominoDataKey)) {
                    continue;
                }
                data.putIfAbsent(dominoDataKey,
                        DominoData.init()
                                .setKey(dominoDataKey)
                                .setDateOrder(dateOrder)
                                .setTonnage(fillInteger(getCellValue(sheet, row, 23)) * 1000)
                                .setPackageCount(getIntegerValue(sheet, row, 28, 1))
                                .setPointName(getCellValue(sheet, row, 9))
                                .setCarNumber(getCellValue(sheet, row, 20).replaceAll(SPACE, EMPTY))
                                .setFio(getCellValue(sheet, row, 16))
                                .setTimeStart(getCellValue(sheet, row, 13))
                                .setTimeEnd(getCellValue(sheet, row, 14))
                                .setOrganization(getCellValue(sheet, row, 15))
                                .build()
                );
            }
            return data;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private Integer fillInteger(String value) {
        try {
            return (int) Double.parseDouble(value.replaceAll("[^\\d.]+", EMPTY));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }


    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class DominoDataKey {
        @EqualsAndHashCode.Include
        String reisNumber;
        @EqualsAndHashCode.Include
        String shopNumber;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class DominoData {
        @EqualsAndHashCode.Include
        DominoDataKey key;
        String organization;
        Date dateOrder;
        Integer tonnage;
        Integer packageCount;
        String pointName;
        String carNumber;
        String fio;
        String timeStart;
        String timeEnd;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class AddressData {
        @EqualsAndHashCode.Include
        String shopName;
        String city;
        String address;
        String time;
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }
}
