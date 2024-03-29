package com.example.advantumconverter.service.excel.converter.booker.impl;

import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import com.example.advantumconverter.service.excel.converter.booker.BookerListService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.BookerListName.BOOKER_METRO;
import static org.apache.commons.lang3.StringUtils.isEmpty;


@Component(BOOKER_METRO)
public class BookerListServiceMetro extends ConvertServiceBase implements BookerListService {

    private final int START_ROW = 5;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;

    private static Set expectedCounterParty = Set.of("АВТОКОМБИНАТ №7", "ДЖИИКСО ЛОДЖИСТИКС", "ТК БОНН АВТО", "ФМ ВОСТОК");
    private static Set checkedInn = Set.of("7706644017", "7707733421");
    private static String DEFAULT_INN = "7717655878";
    private static Double DEFAULT_RATE = 500.0;
    private static String METRO = "М";

    Set<ListKey> listKeys;

    @Override
    public List<BookerInputData> getConvertedList(XSSFWorkbook book, String listName) {
        listKeys = new HashSet<>();
        val answer = new ArrayList<BookerInputData>();
        answer.addAll(convert(book, "metro1"));
        answer.addAll(convert(book, "metro2"));
        return answer;
    }

    public List<BookerInputData> convert(XSSFWorkbook book, String listName) {
        int row = START_ROW;
        val data = new ArrayList<BookerInputData>();
        ArrayList<String> dataLine = new ArrayList();
        try {
            sheet = book.getSheet(listName);
            if(sheet == null){
                return data;
            }
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val owner = getCellValue(row, 6);
                if (owner.equals("Отсутствует")) {
                    continue;
                }
                val counterPartyTmp = getCellValue(row, 2);
                val innReplace = expectedCounterParty.contains(counterPartyTmp) && !counterPartyTmp.equals(owner);

                val listKey = ListKey.init()
                        .setCounterparty(innReplace ? owner : counterPartyTmp)
                        .setCarNumber(getCellValue(row, 4))
                        .setInn(getCellValue(row, innReplace ? 7 : 3))
                        .build();
                if (isEmpty(listKey.getInn()) || listKey.getInn().equals("0") || isEmpty(listKey.getCarNumber()) || listKey.getCarNumber().equals("0")) {
                    continue;
                }
                if (listKeys.contains(listKey)) {
                    continue;
                }
                listKeys.add(listKey);

                data.add(
                        BookerInputData.init()
                                .setClient(METRO)
                                .setCounterparty(listKey.getCounterparty())
                                .setInn(listKey.getInn())
                                .setCarNumber(listKey.getCarNumber())
                                .setRate(DEFAULT_RATE)

                                .setRnic(0)
                                .setX5(0)
                                .setAshan(0)
                                .setMetro(1)
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
            throw new ConvertProcessingException("не удалось обработать лист: " + listName + " строку:" + row
                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private String getInn(int row) {
        return getCellValue(row, 2);
    }

    private String prepareInn(int row) {
        val inn = getInn(row);
        return checkedInn.contains(inn) ? DEFAULT_INN : inn;
    }


    private Double prepareRate(int row, int col) {
        val inn = getInn(row);
        val rate = getDoubleValue(row, 9);
        if (rate == null && inn != null) {
            return DEFAULT_RATE;
        }
        return rate;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    private static class ListKey {
        private String counterparty; //Контрагент
        private String carNumber; //Гос.номер
        private String inn; //ИНН

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListKey listKey = (ListKey) o;
            return counterparty.equals(listKey.counterparty) && carNumber.equals(listKey.carNumber) && inn.equals(listKey.inn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(counterparty, carNumber, inn);
        }
    }
}
