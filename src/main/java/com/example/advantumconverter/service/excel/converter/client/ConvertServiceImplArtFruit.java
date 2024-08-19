package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_ART_FRUIT;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplArtFruit extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 6;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final static String STOCK_ART_FRUIT = "Склад Артфрут";
    private final static String STOCK_ART_FRUIT_ADDRESS = "г. Москва,поселение марушкинское вн.тер.г. 63 кв-л, д.1Б стр 8, скл б/н";
    private final static String VEHICLES_SHEET_NAME = "Vehicles";
    private final static String IVANOV_IVAN_IVANOVICH = "Иванов Иван Иванович";


    @Override
    public String getConverterName() {
        return FILE_NAME_ART_FRUIT;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_ART_FRUIT;
    }


    private Map<String, String> readCarNumbers(XSSFWorkbook book) {
        sheet = book.getSheet(VEHICLES_SHEET_NAME);
        LAST_ROW = getLastRow(START_ROW);
        val carNumbers = new HashMap<String, String>();
        for (int row = 2; row <= LAST_ROW; ++row) {
            val keyM = getCellValue(row, 12);
            val valueB = getCellValue(row, 1);
            val carNumberStartIndex = valueB.lastIndexOf(SPACE);
            if (carNumberStartIndex > 0) {
                carNumbers.put(keyM, valueB.substring(carNumberStartIndex));
            }
        }
        return carNumbers;
    }

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputClient);
        int row = START_ROW;
        ArrayList<String> dataLine = new ArrayList();
        Set<String> addressesInReis = new HashSet<>();

//        val carNumbers = readCarNumbers(book);
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
                    tonnage = getCellValue(row, 11).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY);
                } else {
                    repeat = 1;
                }
                for (int iRepeat = 0; iRepeat < repeat; ++iRepeat) {
                    val address = fillV(isStart, row);
                    if (addressesInReis.contains(address)) {
                        continue;
                    }
                    addressesInReis.add(address);

                    dataLine = new ArrayList<String>();
                    dataLine.add(numberOrderStart);
                    dataLine.add(getDateFromFile(row));
                    dataLine.add(FILE_NAME_ART_FRUIT);
                    dataLine.add(FILE_NAME_ART_FRUIT);
                    dataLine.add(EMPTY);
                    dataLine.add(REFRIGERATOR);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(tonnage);
                    dataLine.add(getCellValue(row, 12).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(fillS(isStart, row));
                    dataLine.add(fillT(isStart, row));
                    dataLine.add(fillU(isStart, row));
                    dataLine.add(address);
                    dataLine.add(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
                    dataLine.add(String.valueOf(isStart ? 0 : numberUnloadingCounter));
                    dataLine.add(isStart ? EMPTY : getCellValue(row, 26).replaceAll(",", "."));
                    dataLine.add(isStart ? EMPTY : getCellValue(row, 27).replaceAll(",", "."));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 30));
                    dataLine.add(EMPTY);
                    dataLine.add(getCellValue(row, 32));
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);
                    dataLine.add(EMPTY);

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
        return createDefaultBook(getConverterName() + UNDERSCORE, EXPORT, data, DONE);
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    private double calculateTonnage(int row) {
        double tonnage = 0.0;
        val key = getCellValue(row, 12);
        for (; row <= LAST_ROW; ++row) {
            val colM = getCellValue(row, 12);
            if (!colM.equals(key)) {
                break;
            }
            tonnage += Double.parseDouble(getCellValue(row, 10).replace(",", "."));
        }
        return tonnage;
    }

    private String getDateFromFile(int row) {
        try {
            return convertDateFormat(getCellValue(row, 1), TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String fillS(boolean isStart, int row) throws ParseException {
        if (isStart) {
            return getDateFromFile(row) + SPACE + "5:00";
        } else {
            return convertDateFormat(getCellValue(row, 20), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT);
        }
    }

    private String fillT(boolean isStart, int row) throws ParseException {
        if (isStart) {
            return getDateFromFile(row) + SPACE + "10:00";
        } else {
            return convertDateFormat(getCellValue(row, 21), TEMPLATE_DATE_TIME_DOT, TEMPLATE_DATE_TIME_DOT);
        }
    }

    private String fillU(boolean isStart, int row) {
        return isStart ? STOCK_ART_FRUIT : clearDoubleSpace(getCellValue(row, 22));
    }

    private String fillV(boolean isStart, int row) {
        return isStart ? STOCK_ART_FRUIT_ADDRESS : clearDoubleSpace(getCellValue(row, 23));
    }

    private String clearDoubleSpace(String text) {
        while (text.indexOf(TWO_SPACE) > 0) {
            text = text.replace(TWO_SPACE, SPACE);
        }
        return text.trim();
    }

}
