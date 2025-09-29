package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SPAR;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_SPAR;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SPAR;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplSpar extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 4;
    private int LAST_ROW;


    private final CrmConfigProperties crmConfigProperties;
    private static final String BAZA_ADDRESS = "603058, Нижегородская обл, Нижний Новгород, улица Героя Попова ул, дом № 43в";
    private List<String> warnings = new ArrayList<>();

    public ConvertServiceImplSpar(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_SPAR;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SPAR;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_SPAR, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        ConvertedListDataV2 dataLine = null;
        List<ConvertedListDataV2> data = List.of();
        int row = START_ROW;
        Map<String, LinkedList<RowData>> rowsdata = new LinkedHashMap<>();
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW, 0);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            //==============================
            //начитываем для сортировки:
            for (; row <= LAST_ROW; ++row) {
                var carNumber = getCellValue(row, 6).replace(SPACE, EMPTY);
                var region = getCellValue(row, 7).replace(SPACE, EMPTY);
                var reisNumber = getCellValue(row, 9).replace(SPACE, EMPTY);
                if (ObjectUtils.isEmpty(carNumber) || ObjectUtils.isEmpty(region) || ObjectUtils.isEmpty(reisNumber)) {
                    warnings.add("Некорретная строка, №" + row + ", отсутствует номер или регион или номер рейса. Пропущена.");
                    continue;
                }
                var dateFromFile = convertDateFormat(getCellValue(row, 0), TEMPLATE_DATE_SLASH);
                var uniqNumber = convertDateFormat(dateFromFile, TEMPLATE_DATE) + "-" + carNumber + "-" + region + "-" + reisNumber;
                var rowData = RowData.init()
                        .setUniqNumber(uniqNumber)
                        .setCarNumber(carNumber)
                        .setRegion(region)
                        .setReisNumber(reisNumber)
                        .setCompanyName(getCellValue(row, 4))
                        .setAddress(getCellValue(row, 10))
                        .setFioDriver(getCellValue(row, 8))
                        .setDateFromFile(dateFromFile)
                        .setTimeFrom(getCellValue(row, 11).replace(SPACE, EMPTY))
                        .setTimeTo(getCellValue(row, 12).replace(SPACE, EMPTY))
                        .setBazaName(getCellValue(row, 1))
                        .build();

                LinkedList<RowData> rows = rowsdata.get(rowData.getUniqNumber());
                if (rows == null) {
                    rows = new LinkedList<>();
                    rowsdata.put(rowData.getUniqNumber(), rows);
                }
                rows.add(rowData);
            }
            data = rowsdata.entrySet().stream()
                    .map(reis -> {
                        try {
                            return prepareConvertedList(reis.getValue());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.flatMapping(List::stream, Collectors.toList()));
        } catch (TemperatureNodValidException e) {
            throw e;
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
                .setMessage(DONE + warnings.stream().distinct().collect(Collectors.joining(EMPTY)))
                .setBookName(getConverterName() + UNDERSCORE)
                .build();
    }

    private List<ConvertedListDataV2> prepareConvertedList(LinkedList<RowData> rowsData) throws ParseException {
        boolean isStart = true;
        int numberUnloading = 0;
        List<ConvertedListDataV2> result = new LinkedList<>();

        for (var rowData : rowsData) {
            if (isStart) {
                numberUnloading = 0;
            }
            for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                var dataLine = ConvertedListDataV2.init()
                        .setColumnAdata(rowData.getUniqNumber())
                        .setColumnBdata(convertDateFormat(convertDateFormat(new Date(), TEMPLATE_DATE_DOT), TEMPLATE_DATE_DOT))
                        .setColumnCdata(COMPANY_NAME_SPAR)
                        .setColumnDdata(COMPANY_NAME_SPAR)
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(EMPTY)
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(12)
                        .setColumnJdata(1500)
                        .setColumnKdata(8)
                        .setColumnLdata(2)
                        .setColumnMdata(6)
                        .setColumnNdata(null)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(fillS(isStart, rowData))
                        .setColumnTdata(fillT(isStart, rowData))
                        .setColumnUdata(isStart? "Склад" : rowData.getCompanyName())
                        .setColumnVdata(isStart ? BAZA_ADDRESS : rowData.getAddress())
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(numberUnloading)
                        .setColumnYdata(null)
                        .setColumnZdata(null)
                        .setColumnAaData(EMPTY)
                        .setColumnAbData(EMPTY)
                        .setColumnAcData(rowData.getCarNumber() + rowData.getRegion())
                        .setColumnAdData(EMPTY)
                        .setColumnAeData(rowData.getFioDriver())
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
                        .setTechFullFio(null)
                        .build();
                result.add(dataLine);

                ++numberUnloading;
                if (!isStart) {
                    break;
                }
                isStart = false;
            }
        }
        return result;

    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date fillS(boolean isStart, RowData rowData) {
        var dateT = fillT(isStart, rowData);
        String dateFromFileString = convertDateFormat(rowData.getDateFromFile(), TEMPLATE_DATE_DOT);
        String time = "08:00";
        if (isStart) {
            time = "00:01";
        } else {
            var sparDbWindows = dictionaryService.getSparWindows(rowData.companyName);
            if (sparDbWindows != null) {
                time = sparDbWindows.getTimeStart();
            } else if (!rowData.getTimeFrom().isEmpty() && !rowData.getTimeFrom().equals("#NULL!")
            ) {
                time = rowData.getTimeFrom();
            }
        }
        var dateS = convertDateFormat(dateFromFileString + SPACE + time, TEMPLATE_DATE_TIME_DOT);
        return dateS.after(dateT) ? DateUtils.addDays(dateS, -1) : dateS;
    }

    @SneakyThrows
    private Date fillT(boolean isStart, RowData rowData) {
        String dateFromFileString = convertDateFormat(rowData.getDateFromFile(), TEMPLATE_DATE_DOT);
        String time = "18:00";
        if (isStart) {
            time = "23:59";
        } else {
            var sparDbWindows = dictionaryService.getSparWindows(rowData.companyName);
            if (sparDbWindows != null) {
                time = sparDbWindows.getTimeEnd();
            }
            if (!rowData.getTimeTo().isEmpty() && !rowData.getTimeTo().equals("#NULL!")) {
                time = rowData.getTimeTo();
            }
        }
        return convertDateFormat(dateFromFileString + SPACE + time, TEMPLATE_DATE_TIME_DOT);
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getSpar();
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    private static class RowData {
        String uniqNumber;
        String carNumber;
        String region;
        String reisNumber;
        String companyName;
        String address;
        String fioDriver;
        Date dateFromFile;
        String timeFrom;
        String timeTo;
        String bazaName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowData that = (RowData) o;
            return Objects.equals(uniqNumber, that.uniqNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uniqNumber, uniqNumber);
        }
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
