package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import com.example.advantumconverter.service.excel.converter.booker.BookerListService;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_OZON;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LIST_CONVERT_ERROR;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component(BOOKER_OZON)
public class BookerListServiceOzon extends ConvertServiceBase implements BookerListService {

    private final int START_ROW = 5;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;

    private static Set excludeCounterParty = Set.of("АГРО-АВТО НАЙМ (МФП+КДК)", "ВНЕ БИЛЛИНГА", "ТЕСТ");
    private static Set checkedInn = Set.of("7706644017", "7707733421");
    private static String OZON_INN = "7717655878";
    private static Double DEFAULT_RATE = 500.0;
    private static String X5 = "X5";


    @Override
    public List<BookerInputData> getConvertedList(XSSFWorkbook book, String listName) {
        if (true) {
            throw new ConvertProcessingException("Требуется доработка!!! TODO");
        }
        int row = START_ROW;
        val data = new ArrayList<BookerInputData>();
        ArrayList<String> dataLine = new ArrayList();
        sheet = getExcelList(book, listName);
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val counterParty = getCellValue(row, 1);
                if (excludeCounterParty.contains(counterParty.toUpperCase())) {
                    continue;
                }
                val inn = prepareInn(row);
                val carNumber = getCellValue(row, 6);
                if (isEmpty(inn) || inn.equals("0")) {
                    continue;
                }
                data.add(
                        BookerInputData.init()
                                .setClient(X5)
                                .setCounterparty(counterParty)
                                .setInn(inn)
                                .setCarNumber(carNumber)
                                .setRate(prepareRate(row, 9))

                                .setRnic(getIntegerValue(row, 10))
                                .setX5(getIntegerValue(row, 11))
                                .setAshan(getIntegerValue(row, 12))
                                .setMetro(getIntegerValue(row, 13))
                                .setOzon(getIntegerValue(row, 14))
                                .setAv(getIntegerValue(row, 15))
                                .setBilla(getIntegerValue(row, 16))
                                .setMagnit(getIntegerValue(row, 17))
                                .setLoginet(getIntegerValue(row, 18))
                                .setOboz(getIntegerValue(row, 19))
                                .setRezident(getIntegerValue(row, 20))
                                .setVerniy(getIntegerValue(row, 21))
                                .setAtmc(getIntegerValue(row, 22))
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
