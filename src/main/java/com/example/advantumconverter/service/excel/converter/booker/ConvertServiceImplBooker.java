package com.example.advantumconverter.service.excel.converter.booker;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ExcelListNotFoundException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.model.pojo.booker.BookerOutputData;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.BookerListName.*;
import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BOOKER;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOOKER;
import static com.example.advantumconverter.constant.Constant.Heap.DONE;
import static com.example.advantumconverter.constant.Constant.Heap.UNDERSCORE;
import static com.example.advantumconverter.enums.ExcelType.BOOKER;
import static java.util.Collections.emptyList;
import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplBooker extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final String UNION_KEY = "%s_%s";
    private StringBuilder message;
    @Autowired
    private Map<String, BookerListService> bookerListServiceMap;

    @Override
    public String getConverterName() {
        return FILE_NAME_BOOKER;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_BOOKER;
    }

    private List<BookerInputData> getConvertedData(XSSFWorkbook book, String converterName) {
        try {
            return bookerListServiceMap.get(converterName).getConvertedList(book, converterName);
        } catch (ExcelListNotFoundException ex) {
            message.append(ex.getMessage()).append(NEW_LINE);
            return emptyList();
        }
    }

    private ArrayList<List<String>> calculate(List<BookerInputData> loadedData) {
        //Группировка по инн и номеру машины:
        Map<String, BookerInputData> calculatedData = new HashMap<>();
        for (BookerInputData bookerInputData : loadedData) {
            val key = String.format(UNION_KEY, bookerInputData.getInn(), bookerInputData.getCarNumber());
            val bookerInputDataTmp = calculatedData.get(key);
            if (bookerInputDataTmp == null) {
                calculatedData.put(key, bookerInputData.toBuilder()
                        .setRef(bookerInputData.getClient().equals("_REF") ? 1 : 0).build());
            } else {
                calculatedData.put(key, BookerInputData.init()
                        .setClient(bookerInputDataTmp.getClient())
                        .setCounterparty(bookerInputDataTmp.getCounterparty())
                        .setInn(bookerInputDataTmp.getInn())
                        .setCarNumber(bookerInputDataTmp.getCarNumber())
                        .setRate(calculateRate(bookerInputData, bookerInputDataTmp))
                        .setRnic(Integer.max(bookerInputData.getRnic(), bookerInputDataTmp.getRnic()))
                        .setX5(Integer.max(bookerInputData.getX5(), bookerInputDataTmp.getX5()))
                        .setAshan(Integer.max(bookerInputData.getAshan(), bookerInputDataTmp.getAshan()))
                        .setMetro(Integer.max(bookerInputData.getMetro(), bookerInputDataTmp.getMetro()))
                        .setOzon(Integer.max(bookerInputData.getOzon(), bookerInputDataTmp.getOzon()))
                        .setAv(Integer.max(bookerInputData.getAv(), bookerInputDataTmp.getAv()))
                        .setBilla(Integer.max(bookerInputData.getBilla(), bookerInputDataTmp.getBilla()))
                        .setMagnit(Integer.max(bookerInputData.getMagnit(), bookerInputDataTmp.getMagnit()))
                        .setLoginet(Integer.max(bookerInputData.getLoginet(), bookerInputDataTmp.getLoginet()))
                        .setOboz(Integer.max(bookerInputData.getOboz(), bookerInputDataTmp.getOboz()))
                        .setRezident(Integer.max(bookerInputData.getRezident(), bookerInputDataTmp.getRezident()))
                        .setVerniy(Integer.max(bookerInputData.getVerniy(), bookerInputDataTmp.getVerniy()))
                        .setAtmc(Integer.max(bookerInputData.getAtmc(), bookerInputDataTmp.getAtmc()))
                        .setRef(bookerInputDataTmp.getClient().equals("_REF") ? 1 : 0)
                        .build());
            }
        }
        calculatedData.replaceAll(
                (k, bookerInputData) -> bookerInputData.toBuilder().setRateTc(calculateRateTc(bookerInputData)).build()
        );

        val bookerOutputDataMap = new HashMap<String, BookerOutputData>();
        //Группировка по инн:
        for (Map.Entry calculated : calculatedData.entrySet()) {
            val bookerOutputData = (BookerInputData) calculated.getValue();
            if (bookerOutputData.getRateTc() == 0) {
                continue;
            }
            val key = bookerOutputData.getInn();
            val bookerOutputDataTmp = bookerOutputDataMap.get(key);
            if (bookerOutputDataTmp == null) {
                bookerOutputDataMap.put(key, BookerOutputData.init()
                        .setInn(key)
                        .setCarNumberCount(1)
                        .setRateTcSum(bookerOutputData.getRateTc())
                        .build());
            } else {
                bookerOutputDataMap.put(key, bookerOutputDataTmp.toBuilder()
                        .setCarNumberCount(bookerOutputDataTmp.getCarNumberCount() + 1)
                        .setRateTcSum(bookerOutputDataTmp.getRateTcSum() + bookerOutputData.getRateTc())
                        .build());
            }
        }

        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutputBooker);
        for (BookerOutputData bookerOutputData : bookerOutputDataMap.values()) {
            data.add(List.of(String.valueOf(bookerOutputData.getInn())
                    , String.valueOf(bookerOutputData.getCarNumberCount())
                    , String.valueOf((long) (double) bookerOutputData.getRateTcSum()))
            );
        }
        return data;
    }

    @Override
    public ConvertedBook getConvertedBook(XSSFWorkbook book) {
        message = new StringBuilder();
        //сгруппировать по инн + номер машины. Взять максимальные значения разъездов
        val loadedData = new ArrayList<BookerInputData>();

        val loadedDataX5 = getConvertedData(book, BOOKER_X5);
        val loadedDataAshan = getConvertedData(book, BOOKER_ASHAN);
        val loadedDataAV = getConvertedData(book, BOOKER_AV);
        val loadedDataMetro = getConvertedData(book, BOOKER_METRO);
//        val loadedDataOzon = getConvertedData(book, BOOKER_OZON);

        loadedData.addAll(loadedDataX5);
        loadedData.addAll(loadedDataAshan);
        loadedData.addAll(loadedDataAV);
        loadedData.addAll(loadedDataMetro);
//        loadedData.addAll(loadedDataOzon);

        val dataAll = calculate(loadedData);

//        val data2 = new ArrayList<List<String>>();
//        data2.add(Header.headersOutputBooker);
//        data2.add(List.of("1", "2", "777"));
//        createConvertedList("Статистика клиенты", data2)

        val excelLists = List.of(
                createConvertedList("Итого для 1С", dataAll)
                , createConvertedList(BOOKER_X5, calculate(loadedDataX5))
                , createConvertedList(BOOKER_ASHAN, calculate(loadedDataAshan))
                , createConvertedList(BOOKER_AV, calculate(loadedDataAV))
                , createConvertedList(BOOKER_METRO, calculate(loadedDataMetro))
//                createConvertedList(BOOKER_OZON, calculate(loadedDataMetro))
        );
        if (message.isEmpty()) {
            message.append(DONE);
        }
        return createConvertedBook(getConverterName() + UNDERSCORE, excelLists, message.toString());
    }

    private Double calculateRateTc(BookerInputData bookerInputData) {
        if (bookerInputData.getRef() == 1) {
            return 0.0;
        }
        val summ1 = bookerInputData.getRnic()
                + bookerInputData.getX5()
                + bookerInputData.getAshan()
                + bookerInputData.getMetro()
                + bookerInputData.getAv()
                + bookerInputData.getMagnit()
                + bookerInputData.getLoginet()
                + bookerInputData.getOboz()
                + bookerInputData.getRezident()
                + bookerInputData.getVerniy()
                + bookerInputData.getAtmc();

        val ozonAndLenta = bookerInputData.getOzon() + bookerInputData.getBilla();
        if (summ1 >= 1.0) {
            return ozonAndLenta + bookerInputData.getRate() + ((summ1 - 1.0) * 200.0);
        }
        return Double.valueOf(ozonAndLenta);
    }

    private Double calculateRate(BookerInputData bookerInputData, BookerInputData bookerInputDataTmp) {
        val clientName = bookerInputDataTmp.getClient();
        if (clientName.equals("_REF")) {
            return 0.0;
        }
        if ((clientName.equals("X5") || clientName.equals("A"))
                && bookerInputDataTmp.getRate() > 0.0) {
            return bookerInputDataTmp.getRate();
        }
        return Double.max(bookerInputData.getRate(), bookerInputDataTmp.getRate());
    }

    @Override
    public ExcelType getExcelType() {
        return BOOKER;
    }

    private String fillS(boolean isStart, int row) throws ParseException {
        if (isStart) {
            val timeK = convertDateFormat(getCellValue(row, 10), TEMPLATE_TIME, TEMPLATE_TIME_SECOND);
            val dateResult = getCellValue(row, 9) + SPACE + (isStart ? timeK : "TODO");
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_SLASH, TEMPLATE_DATE_TIME_DOT);
        } else {
            val dateText = fillT(isStart, row);
            val date = convertDateFormat(dateText, TEMPLATE_DATE_TIME_DOT);
            return convertDateFormat(DateUtils.addHours(date, -2), TEMPLATE_DATE_TIME_DOT);
        }
    }

    private String fillT(boolean isStart, int row) throws ParseException {
        if (isStart) {
            val dateText = fillS(isStart, row);
            val date = convertDateFormat(dateText, TEMPLATE_DATE_TIME_DOT);
            return convertDateFormat(DateUtils.addHours(date, 2), TEMPLATE_DATE_TIME_DOT);
        } else {
            val timeN = convertDateFormat(getCellValue(row, 13), TEMPLATE_TIME, TEMPLATE_TIME_SECOND);
            val dateResult = getCellValue(row, 12) + SPACE + timeN;
            return convertDateFormat(dateResult, TEMPLATE_DATE_TIME_SLASH, TEMPLATE_DATE_TIME_DOT);
        }
    }

}
