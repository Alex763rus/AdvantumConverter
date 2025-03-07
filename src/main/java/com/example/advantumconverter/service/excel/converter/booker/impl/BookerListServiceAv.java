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

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_AV;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LIST_CONVERT_ERROR;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component(BOOKER_AV)
public class BookerListServiceAv extends ConvertServiceBase implements BookerListService {

    private final int START_ROW = 5;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;

    private static Set checkedInn = Set.of("7706644017", "7707733421");
    private static String DEFAULT_INN = "7717655878";
    private static Double DEFAULT_RATE = 500.0;
    private static String AV = "АВ";


    @Override
    public List<BookerInputData> getConvertedList(XSSFWorkbook book, String listName) {
        int row = START_ROW;
        val data = new ArrayList<BookerInputData>();
        ArrayList<String> dataLine = new ArrayList();
        Set<BookerListKey> listKeys = new HashSet<>();
        sheet = getExcelList(book, listName);
        try {
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val inn = prepareInn(row);
                val carNumber = getCellValue(row, 1);
                if (isEmpty(inn) || inn.equals("0") || isEmpty(carNumber) || carNumber.equals("0")) {
                    continue;
                }
                val listKey = BookerListKey.init()
                        .setCounterparty(getCellValue(row, 2))
                        .setCarNumber(carNumber)
                        .setInn(inn)
                        .build();
                if (listKeys.contains(listKey)) {
                    continue;
                }
                listKeys.add(listKey);
                data.add(
                        BookerInputData.init()
                                .setClient(AV)
                                .setCounterparty(listKey.getCounterparty())
                                .setInn(listKey.getInn())
                                .setCarNumber(listKey.getCarNumber())
                                .setRate(DEFAULT_RATE)

                                .setRnic(0)
                                .setX5(0)
                                .setAshan(0)
                                .setMetro(0)
                                .setOzon(0)
                                .setAv(1)
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
        return getCellValue(row, 3);
    }

    private String prepareInn(int row) {
        val inn = getInn(row);
        return checkedInn.contains(inn) ? DEFAULT_INN : inn;
    }
}
