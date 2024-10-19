package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static org.example.tgcommons.utils.NumberConverter.convertToDoubleOrNull;

@Component
public class ConvertServiceImplArtFruit extends ConvertServiceBase implements ConvertService {

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
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        int row = START_ROW;
        Set<String> addressesInReis = new HashSet<>();
        Set<AddressInReis> uniqReisAndAddress = new HashSet<>();
        ConvertedListDataV2 dataLine = null;
        boolean isStart = true;
        int lastNumberUnloading = -1;
        int numberUnloadingCounter = 0;
        int repeat = 0;
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
                    startAddress = fillV(row);
                    lastNumberOrderStart = numberOrderStart;
                }
                var number = getCellValue(rowTmp, 1);
                var taskNumber = getCellValue(rowTmp, 0);
                val numbersTmp = new ArrayList<String>();
                numbersTmp.add(number);
                val address = fillV(rowTmp);
                val addressInReisTmp = AddressInReis.init()
                        .setTaskNumber(taskNumber)
                        .setAddress(address)
                        .setStartAddress(startAddress)
                        .setNumbers(numbersTmp)
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

            AddressInReis addressInReisTmp = null;
            for (; row <= LAST_ROW; ++row) {
                val numberOrderStart = getCellValue(row, 0);
                isStart = !lastNumberOrderStart.equals(numberOrderStart);
                if (isStart) {
                    addressesInReis.clear();
                    lastNumberOrderStart = numberOrderStart;
                    numberUnloadingCounter = 0;
                    tonnage = getCellValue(row, 10).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY);
                }
                val address = fillV(row);
                addressInReisTmp = AddressInReis.getAddressInReis(uniqReisAndAddress, numberOrderStart, address);
                if (!isStart && addressesInReis.contains(address)) {
                    if (!addressInReisTmp.getStartAddress().equals(address)) {
                        continue;
                    }
                }
                addressesInReis.add(address);

                dataLine = ConvertedListDataV2.init()
                        .setColumnAdata(numberOrderStart)
                        .setColumnBdata(convertDateFormat(getDateFromFile(row), TEMPLATE_DATE_DOT))
                        .setColumnCdata(getCellValue(row, 3))
                        .setColumnDdata(getCellValue(row, 4))
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(EMPTY)
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(null)
                        .setColumnJdata(convertToIntegerOrNull(tonnage))
                        .setColumnKdata(convertToIntegerOrNull(
                                getCellValue(row, 11).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY)
                        ))
                        .setColumnLdata(null)
                        .setColumnMdata(null)
                        .setColumnNdata(null)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(fillS(row))
                        .setColumnTdata(fillT(row))
                        .setColumnUdata(fillU(row))
                        .setColumnVdata(address)
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(isStart ? 0 : numberUnloadingCounter)
                        .setColumnYdata(
                                isStart ? null : convertToDoubleOrNull(
                                        getCellValue(row, 25).replaceAll(",", "."))
                        )
                        .setColumnZdata(
                                isStart ? null : convertToDoubleOrNull(
                                        getCellValue(row, 26).replaceAll(",", "."))
                        )
                        .setColumnAaData(AddressInReis.getNumbers(addressInReisTmp))
                        .setColumnAbData(EMPTY)
                        .setColumnAcData(getCellValue(row, 29))
                        .setColumnAdData(EMPTY)
                        .setColumnAeData(getCellValue(row, 31))
                        .setColumnAfData(null)
                        .setColumnAgData(EMPTY)
                        .setColumnAhData(EMPTY)
                        .build();
                data.add(dataLine);
                ++numberUnloadingCounter;
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return ConvertedBookV2.init()
                .setBookV2(List.of(
                        ConvertedListV2.init()
                                .setHeadersV2(Header.headersOutputClient)
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

    private String getDateFromFile(int row) {
        try {
            return convertDateFormat(getCellValue(row, 2), TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date fillS(int row) throws ParseException {
        int colNumber = 19;
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
        int colNumber = 20;
        if (getCellValue(row, colNumber).equals(EMPTY)) {
            warnings.add(String.format("\n- Строка: %d, столбец: %d, %s", row + 1, colNumber + 1, "отсутствует дата"));
            return null;
        }
        return convertDateFormat(
                convertDateFormat(getCellValue(row, colNumber), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT)
                , TEMPLATE_DATE_TIME_DOT);
    }

    private String fillU(int row) {
        return clearDoubleSpace(getCellValue(row, 21));
    }

    private String fillV(int row) {
        return clearDoubleSpace(getCellValue(row, 22));
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
        String address;
        String startAddress;
        List<String> numbers = new ArrayList<>();

        public static AddressInReis getAddressInReis(Set<AddressInReis> addressInReises, String taskNumber, String address) {
            val tmpAddressInReis = AddressInReis.init()
                    .setTaskNumber(taskNumber)
                    .setAddress(address)
                    .build();
            return addressInReises.stream()
                    .filter(data -> data.equals(tmpAddressInReis))
                    .findAny()
                    .get();
        }

        public static String getNumbers(AddressInReis addressInReise) {
            return addressInReise.getNumbers().stream()
                    .filter(number -> !number.equals(EMPTY))
                    .collect(Collectors.joining(", "));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AddressInReis that = (AddressInReis) o;
            return Objects.equals(taskNumber, that.taskNumber) && Objects.equals(address, that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskNumber, address);
        }
    }

}
