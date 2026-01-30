package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Heap.TWO_SPACE;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static org.example.tgcommons.utils.NumberConverter.convertToDoubleOrNull;

@Component
public class ConvertServiceImplArtFruit extends ConvertServiceBase implements ConvertService {

    private static final String SLASH_DELIMETER = " / ";
    private final int START_ROW = 4;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private List<String> warnings = new ArrayList<>();

    @Override
    public String getConverterName() {
        return FILE_NAME_ART_FRUIT;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_ART_FRUIT;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_ART_FRUIT, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        int row = START_ROW;
        Set<String> addressesAndPontNameInReis = new HashSet<>();
        Set<AddressInReis> uniqReisAndAddress = new HashSet<>();
        ConvertedListDataClientsV2 dataLine = null;
        boolean isStart = true;
        int numberUnloadingCounter = 0;
        String tonnage = EMPTY;

        String lastNumberOrderStart = EMPTY;
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            String startAddress = null;

            for (int rowTmp = row; rowTmp <= LAST_ROW; ++rowTmp) {
                val numberOrderStart = getCellValue(row, 0);
                isStart = !lastNumberOrderStart.equals(numberOrderStart);
                if (isStart) {
                    startAddress = getPointAddress(row);
                    lastNumberOrderStart = numberOrderStart;
                }
                var number = getOrderNumber(rowTmp);
                var taskNumber = getCellValue(rowTmp, 0);
                val numbersTmp = new ArrayList<String>();
                numbersTmp.add(number);
                val address = getPointAddress(rowTmp);
                val pointName = getPointName(rowTmp);
                val addressInReisTmp = AddressInReis.init()
                        .setTaskNumber(taskNumber)
                        .setPointName(pointName)
                        .setAddress(address)
                        .setStartAddress(startAddress)
                        .setNumbers(numbersTmp)
                        .setManager(getCellValue(rowTmp, 41))
                        .setOrderNumber(getCellValue(rowTmp, 42))
                        .build();
                var addressInReisTmpSearch = uniqReisAndAddress.stream()
                        .filter(e -> e.equals(addressInReisTmp))
                        .toList();
                if (addressInReisTmpSearch.isEmpty()) {
                    uniqReisAndAddress.add(addressInReisTmp);
                } else {
                    addressInReisTmpSearch.get(0).getNumbers().add(number);
                }
            }

            Map<String, List<String>> ordersInReis = new HashMap<>();
            for (int rowTmp = row; rowTmp <= LAST_ROW; ++rowTmp) {
                //заполнения поля комментарий рейс:
                val numberOrderStart = getCellValue(rowTmp, 0);
                var orderInReis = getOrderNumber(rowTmp);
                var savedOrderInReis = ordersInReis.get(numberOrderStart);
                if (savedOrderInReis == null) {
                    List<String> orders = new ArrayList<>();
                    orders.add(orderInReis);
                    ordersInReis.put(numberOrderStart, orders);
                } else {
                    savedOrderInReis.add(orderInReis);
                }
            }

            isStart = true;
            lastNumberOrderStart = EMPTY;
            String reisInOrder = EMPTY;
            AddressInReis addressInReisTmp = null;
            for (; row <= LAST_ROW; ++row) {
                val numberOrderStart = getCellValue(row, 0);
                isStart = !lastNumberOrderStart.equals(numberOrderStart);
                if (isStart) {
                    addressesAndPontNameInReis.clear();
                    lastNumberOrderStart = numberOrderStart;
                    numberUnloadingCounter = 0;
                    tonnage = getCellValue(row, 12).replaceAll(REGEX_NUMBER, EMPTY);
                    reisInOrder = ordersInReis.getOrDefault(numberOrderStart, new ArrayList<>()).stream()
                            .filter(e -> !e.equals(EMPTY))
                            .collect(Collectors.joining(", "));
                }
                val pointName = getPointName(row);
                val address = getPointAddress(row);
                var addressAndPointName = pointName + "_" + address;
                addressInReisTmp = AddressInReis.getAddressInReis(uniqReisAndAddress, numberOrderStart, pointName, address);
                if (!isStart && addressesAndPontNameInReis.contains(addressAndPointName)) {
                    //Если это не база, то пропускам:
                    if (!addressInReisTmp.getStartAddress().equals(address)) {
                        continue;
                    }
                }
                addressesAndPontNameInReis.add(addressAndPointName);

                dataLine = ConvertedListDataClientsV2.init()
                        .setColumnAdata(numberOrderStart)
                        .setColumnBdata(convertDateFormat(getDateFromFile(row), TEMPLATE_DATE_DOT))
                        .setColumnCdata(getCellValue(row, 3))
                        .setColumnDdata(getCellValue(row, 4))
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(getCellValue(row, 9))
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(getIntegerValue(row, 11))
                        .setColumnJdata(convertToIntegerOrNull(tonnage))
                        .setColumnKdata(convertToIntegerOrNull(
                                getCellValue(row, 13).replaceAll(REGEX_NUMBER, EMPTY)
                        ))
                        .setColumnLdata(getIntegerValue(row, 14))
                        .setColumnMdata(getIntegerValue(row, 15))
                        .setColumnNdata(getIntegerValue(row, 16))
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(getIntegerValue(row, 20))
                        .setColumnSdata(fillS(row))
                        .setColumnTdata(fillT(row))
                        .setColumnUdata(getPointName(row))
                        .setColumnVdata(address)
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(isStart ? 0 : numberUnloadingCounter)
                        .setColumnYdata(
                                isStart ? null : convertToDoubleOrNull(
                                        getCellValue(row, 29).replaceAll(",", "."))
                        )
                        .setColumnZdata(
                                isStart ? null : convertToDoubleOrNull(
                                        getCellValue(row, 30).replaceAll(",", "."))
                        )
                        .setColumnAaData(checkNull(row, 31))
                        .setColumnAbData(getCellValue(row, 32))
                        .setColumnAcData(getCellValue(row, 33))
                        .setColumnAdData(checkNull(row, 34))
                        .setColumnAeData(getCellValue(row, 35))
                        .setColumnAfData(null)
                        .setColumnAgData(EMPTY)
                        .setColumnAhData(getCellValue(row, 39))
                        .setColumnAiData(getIntegerValue(isStart ? row + 1 : row, 40))
                        .setColumnAjData(EMPTY)
                        .setColumnAkData(getCellValue(row, 24))
                        .setColumnAlData(getCellValue(row, 25))
                        .setColumnAmData(getCellValue(row, 36))
                        .setColumnAnData(fillAn(addressInReisTmp))
                        .setColumnAoData(reisInOrder)
                        .build();
                data.add(dataLine);
                ++numberUnloadingCounter;
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    private String getOrderNumber(int row) {
        return checkNull(row, 1);
    }

    private String fillAn(AddressInReis addressInReisTmp) {
        var numbers = addressInReisTmp.getNumbers().stream()
                .filter(number -> !number.equals(EMPTY))
                .collect(Collectors.joining(", "));
        return numbers
                + (addressInReisTmp.getOrderNumber().isEmpty() ? EMPTY : SLASH_DELIMETER + addressInReisTmp.getOrderNumber())
                + (addressInReisTmp.getManager().isEmpty() ? EMPTY : SLASH_DELIMETER + addressInReisTmp.getManager());
    }

    private String checkNull(int row, int col) {
        var value = getCellValue(row, col);
        return value.equals("#NULL!") ? EMPTY : value;
    }

    private String getDateFromFile(int row) {
        try {
            return convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date fillS(int row) throws ParseException {
        int colNumber = 21;
        if (getCellValue(row, colNumber).equals(EMPTY)) {
            warnings.add(String.format("\n- Строка: %d, столбец: %d, %s", row + 1, colNumber + 1, "отсутствует дата"));
            return null;
        }
        return convertDateFormat(
                convertDateFormat(getCellValue(row, colNumber), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT)
                , TEMPLATE_DATE_TIME_DOT
        );
    }

    private Date fillT(int row) throws ParseException {
        int colNumber = 22;
        if (getCellValue(row, colNumber).equals(EMPTY)) {
            warnings.add(String.format("\n- Строка: %d, столбец: %d, %s", row + 1, colNumber + 1, "отсутствует дата"));
            return null;
        }
        return convertDateFormat(
                convertDateFormat(getCellValue(row, colNumber), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT)
                , TEMPLATE_DATE_TIME_DOT);
    }

    private String getPointName(int row) {
        return clearDoubleSpace(getCellValue(row, 23));
    }

    private String getPointAddress(int row) {
        return clearDoubleSpace(getCellValue(row, 26));
    }

    private String clearDoubleSpace(String text) {
        while (text.indexOf(TWO_SPACE) > 0) {
            text = text.replace(TWO_SPACE, SPACE);
        }
        return text.trim();
    }


    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    private static class AddressInReis {
        String taskNumber;
        String pointName;
        String address;
        String startAddress;
        String manager;
        String orderNumber;
        List<String> numbers = new ArrayList<>();

        public static AddressInReis getAddressInReis(Set<AddressInReis> addressInReises, String taskNumber, String pointName, String address) {
            val tmpAddressInReis = AddressInReis.init()
                    .setPointName(pointName)
                    .setTaskNumber(taskNumber)
                    .setAddress(address)
                    .build();
            return addressInReises.stream()
                    .filter(data -> data.equals(tmpAddressInReis))
                    .findAny()
                    .get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AddressInReis that = (AddressInReis) o;
            return Objects.equals(taskNumber, that.taskNumber) && Objects.equals(address, that.address) && Objects.equals(pointName, that.pointName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskNumber, address, pointName);
        }
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getArtfruit();
    }
}
