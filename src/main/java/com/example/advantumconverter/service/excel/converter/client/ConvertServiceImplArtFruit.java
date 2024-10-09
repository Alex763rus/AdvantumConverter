package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
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

    private final int START_ROW = 7;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final static String STOCK_ART_FRUIT = "Склад Артфрут";
    private final static String STOCK_ART_FRUIT_ADDRESS = "г. Москва,поселение марушкинское вн.тер.г. 63 кв-л, д.1Б стр 8, скл б/н";
    private final static String VEHICLES_SHEET_NAME = "Vehicles";
    private final static String TIME_10_00_STRING = "10:00";
    private final static String TIME_5_00_STRING = "5:00";
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
            for (; row <= LAST_ROW; ++row) {
                val numberOrderStart = getCellValue(row, 0);

                isStart = !lastNumberOrderStart.equals(numberOrderStart);
                if (isStart) {
                    addressesInReis.clear();
                    lastNumberOrderStart = numberOrderStart;
                    numberUnloadingCounter = 0;
                    repeat = 2;
                    tonnage = getCellValue(row, 12).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY);
                } else {
                    repeat = 1;
                }
                for (int iRepeat = 0; iRepeat < repeat; ++iRepeat) {
                    val address = fillV(isStart, row);
                    if (addressesInReis.contains(address)) {
                        continue;
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
                                    getCellValue(row, 13).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY)
                            ))
                            .setColumnLdata(null)
                            .setColumnMdata(null)
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(fillS(isStart, row))
                            .setColumnTdata(fillT(isStart, row))
                            .setColumnUdata(fillU(isStart, row))
                            .setColumnVdata(address)
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 0 : numberUnloadingCounter)
                            .setColumnYdata(
                                    isStart ? null : convertToDoubleOrNull(
                                            getCellValue(row, 27).replaceAll(",", "."))
                            )
                            .setColumnZdata(
                                    isStart ? null : convertToDoubleOrNull(
                                            getCellValue(row, 28).replaceAll(",", "."))
                            )
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(getCellValue(row, 31))
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(getCellValue(row, 33))
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .build();
                    data.add(dataLine);
                    ++numberUnloadingCounter;
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                }

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
            return convertDateFormat(getCellValue(row, 1), TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date fillS(boolean isStart, int row) throws ParseException {
        if (getCellValue(row, 21).equals(EMPTY)) {
            warnings.add(String.format("\n- Строка: %d, столбец: %d, %s", row + 1, 21 + 1, "отсутствует дата"));
            return null;
        }
        return convertDateFormat(isStart ?
                        getDateFromFile(row) + SPACE + TIME_5_00_STRING :
                        convertDateFormat(getCellValue(row, 21), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT)
                , TEMPLATE_DATE_TIME_DOT
        );
    }

    private Date fillT(boolean isStart, int row) throws ParseException {
        if (getCellValue(row, 22).equals(EMPTY)) {
            warnings.add(String.format("\n- Строка: %d, столбец: %d, %s", row + 1, 22 + 1, "отсутствует дата"));
            return null;
        }
        return convertDateFormat(isStart ?
                        getDateFromFile(row) + SPACE + TIME_10_00_STRING :
                        convertDateFormat(getCellValue(row, 22), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT)
                , TEMPLATE_DATE_TIME_DOT);
    }

    private String fillU(boolean isStart, int row) {
        return isStart ? STOCK_ART_FRUIT : clearDoubleSpace(getCellValue(row, 23));
    }

    private String fillV(boolean isStart, int row) {
        return isStart ? STOCK_ART_FRUIT_ADDRESS : clearDoubleSpace(getCellValue(row, 24));
    }

    private String clearDoubleSpace(String text) {
        while (text.indexOf(TWO_SPACE) > 0) {
            text = text.replace(TWO_SPACE, SPACE);
        }
        return text.trim();
    }

}
