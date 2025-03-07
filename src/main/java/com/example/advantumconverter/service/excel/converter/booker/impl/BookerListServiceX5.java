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

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_X5;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LIST_CONVERT_ERROR;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component(BOOKER_X5)
public class BookerListServiceX5 extends ConvertServiceBase implements BookerListService {

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
                if (isEmpty(inn) || inn.equals("0") || isEmpty(carNumber) || carNumber.equals("0")) {
                    continue;
                }
                data.add(
                        BookerInputData.init()
                                .setClient(X5)
                                .setCounterparty(counterParty)
                                .setInn(inn)
                                .setCarNumber(carNumber)
                                .setRate(prepareRate(row, 9))

                                .setRnic(prepareRnic(row, 10))
                                .setX5(getIntegerValue(row, 11))
                                .setAshan(0)
                                .setMetro(0)
                                .setOzon(0)
                                .setAv(0)
                                .setBilla(0)
                                .setMagnit(getIntegerValue(row, 17))
                                .setLoginet(getIntegerValue(row, 18))
                                .setOboz(getIntegerValue(row, 19))
                                .setRezident(getIntegerValue(row, 20))
                                .setVerniy(getIntegerValue(row, 21))
                                .setAtmc(0)
                                .build()
                );
            }
        } catch (Exception e) {
            throw ConvertProcessingException.of(EXCEL_LIST_CONVERT_ERROR, listName, row, dataLine, e.getMessage());
        }
        return data;
    }

    private Integer prepareRnic(int row, int col) {
        val res = getIntegerValue(row, col);
        return res == null ? 0 : res;
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
        val rate = getDoubleValue(row, col);
        if (rate == null && inn != null) {
            return DEFAULT_RATE;
        }
        return rate;
    }
}
