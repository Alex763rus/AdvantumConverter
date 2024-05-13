package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.model.pojo.converter.BookerListKey;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import com.example.advantumconverter.service.excel.converter.booker.BookerListService;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_ASHAN;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LIST_CONVERT_ERROR;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component(BOOKER_ASHAN)
public class BookerListServiceAshan extends ConvertServiceBase implements BookerListService {

    private final int START_ROW = 5;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;

    private static Set excludeCounterParty =
            Set.of("7810071482" //Монополия
//                    , "ООО «МОНОПОЛИЯ ЛОГИСТИК»"
                    , "7736322345" //СБЕРПЕЛОГИСТИКА
                    , "5260168445" //ООО «ДЖИИКСО ЛОДЖИСТИКC
//                    , "ТЕРМОФЛИТ"
//                    , "ТЕСТОВЫЕ ТС"
//                    , "ВНЕ БИЛЛИНГА"
            );
    private static Set includeCounterParty =
            Set.of("7810071482" //Монополия
                    //"ООО «МОНОПОЛИЯ ЛОГИСТИК»"
                    , "7736322345" //СБЕРПЕЛОГИСТИКА
                    , "5260168445" //ООО «ДЖИИКСО ЛОДЖИСТИКC
//                    , "ООО \"МОНОПОЛИЯ ЛОГИСТИК\""
                    , "ООО «ДЖИИКСО ЛОДЖИСТИКC"
                    , "СБЕРПЕЛОГИСТИКА"
                    , "Монополия"
            );

    private static Set checkedInn = Set.of("7706644017", "7707733421");

    private static String OZON_INN = "7717655878";
    private static Double DEFAULT_RATE = 500.0;
    private static String DEFAULT_INN = "1";
    private static String TS_NO_SETUP = "ТС не оборудовано";
    private static String ASHAN = "A";

    private static String ASHAN_1 = "ashan1";
    private static String ASHAN_2 = "ashan2";

    @Override
    public List<BookerInputData> getConvertedList(XSSFWorkbook book, String listName) {
        val result = new ArrayList<BookerInputData>();
        result.addAll(prepareList1(book, ASHAN_1));
        result.addAll(prepareList2(book, ASHAN_2));
        return result;
    }


    private List<BookerInputData> prepareList1(XSSFWorkbook book, String listName) {
        int row = START_ROW;
        val data = new ArrayList<BookerInputData>();
        ArrayList<String> dataLine = new ArrayList();
        sheet = getExcelList(book, listName);
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val inn = prepareInn(row);
                val carNumber = getCellValue(row, 6);
                if (isEmpty(inn) || inn.equals("0") || isEmpty(carNumber) || excludeCounterParty.contains(inn)) {
                    continue;
                }
                data.add(
                        BookerInputData.init()
                                .setClient(ASHAN)
                                .setCounterparty(getCellValue(row, 1))
                                .setInn(inn)
                                .setCarNumber(carNumber)
                                .setRate(prepareRate(row, 9))

                                .setRnic(0)
                                .setX5(0)
                                .setAshan(1)
                                .setMetro(0)
                                .setOzon(0)
                                .setAv(0)
                                .setBilla(0)
                                .setMagnit(getIntegerValue(row, 16))
                                .setLoginet(getIntegerValue(row, 17))
                                .setOboz(getIntegerValue(row, 18))
                                .setRezident(getIntegerValue(row, 19))
                                .setVerniy(getIntegerValue(row, 20))
                                .setAtmc(0)
                                .build()
                );
            }
        } catch (Exception e) {
            throw ConvertProcessingException.of(EXCEL_LIST_CONVERT_ERROR, listName, row, dataLine, e.getMessage());
        }
        return data;
    }

    private List<BookerInputData> prepareList2(XSSFWorkbook book, String listName) {
        int row = START_ROW;
        val data = new ArrayList<BookerInputData>();
        ArrayList<String> dataLine = new ArrayList();
        Set<BookerListKey> listKeys = new HashSet<>();
        sheet = getExcelList(book, listName);
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val counterParty = getCellValue(row, 6);
                val gps = getCellValue(row, 6);
                if (gps.equals(EMPTY) || gps.equals(TS_NO_SETUP)) {
                    continue;
                }
                if (!includeCounterParty.contains(counterParty.toUpperCase())) {
                    continue;
                }
                val carNumber = getCellValue(row, 7);
                val listKey = BookerListKey.init()
                        .setCounterparty(counterParty)
                        .setCarNumber(carNumber)
                        .setInn(EMPTY)
                        .build();
                if (listKeys.contains(listKey)) {
                    continue;
                }
                listKeys.add(listKey);
                data.add(
                        BookerInputData.init()
                                .setClient(ASHAN)
                                .setCounterparty(counterParty)
                                .setInn(counterParty)//todo потом inn нужно будет с базы тянуть
                                .setCarNumber(carNumber)
                                .setRate(DEFAULT_RATE)

                                .setRnic(0)
                                .setX5(0)
                                .setAshan(1)
                                .setMetro(0)
                                .setOzon(0)
                                .setAv(0)
                                .setBilla(0)
                                .setMagnit(0)
                                .setLoginet(0)
                                .setOboz(0)
                                .setRezident(0)
                                .setVerniy(0)
                                .setAtmc(0)
                                .build()
                );
            }
        } catch (Exception e) {
            throw ConvertProcessingException.of(EXCEL_LIST_CONVERT_ERROR, listName, row, dataLine, e.getMessage());
        }
        return data;
    }

    private String getInn(int row) {
        return getCellValue(row, 2);
    }

    private String prepareInn(int row) {
        val inn = getInn(row);
        return checkedInn.contains(inn) ? OZON_INN : inn;
    }


    private Double prepareRate(int row, int col) {
        val inn = getInn(row);
        val rate = getDoubleValue(row, 9);
        if (rate == null && inn != null) {
            return DEFAULT_RATE;
        }
        return rate;
    }

}
