package com.example.advantumconverter.service.excel.converter.booker;

import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.booker.BookerInputData;
import com.example.advantumconverter.model.pojo.booker.BookerOutputData;
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
import static com.example.advantumconverter.constant.Constant.ExcelType.BOOKER;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BOOKER;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplBooker extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final String UNION_KEY = "%s_%s";

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

    @Override
    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        Map<String, BookerInputData> calculatedData = new HashMap<>();
        //сгруппировать по инн + номер машины. Взять максимальные значения разъездов
        val loadedData = new ArrayList<BookerInputData>();

        loadedData.addAll(bookerListServiceMap.get(BOOKER_X5).getConvertedList(book, BOOKER_X5));
        loadedData.addAll(bookerListServiceMap.get(BOOKER_ASHAN).getConvertedList(book, BOOKER_ASHAN));
        loadedData.addAll(bookerListServiceMap.get(BOOKER_AV).getConvertedList(book, BOOKER_AV));
        loadedData.addAll(bookerListServiceMap.get(BOOKER_METRO).getConvertedList(book, BOOKER_METRO));

        //Группировка по инн и номеру машины:
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
    public String getFileNamePrefix() {
        return getConverterName() + "_";
    }

    @Override
    public String getExcelType() {
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

    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > LAST_ROW) {
            return EMPTY;
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
            return EMPTY;
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }
/*
 int row = START_ROW;
//        ArrayList<String> dataLine = new ArrayList();
//        try {
//            sheet = book.getSheetAt(0);
//            LAST_ROW = getLastRow(START_ROW);
//            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
//            for (; row <= LAST_ROW; ++row) {
//                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
//                    dataLine = new ArrayList<String>();
//                    dataLine.add(getCellValue(row, 0));
//                    dataLine.add(convertDateFormat(getCellValue(row, 9), TEMPLATE_DATE_SLASH, TEMPLATE_DATE_DOT));
//                    dataLine.add("Х5 ТЦ Новая Рига");
//                    dataLine.add(BUSH_AUTOPROM_ORGANIZATION_NAME);
//                    dataLine.add(EMPTY);
//                    dataLine.add(REFRIGERATOR);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(getCellValue(row, 5));
//                    dataLine.add(getCellValue(row, 6));
//                    dataLine.add("3");
//                    dataLine.add("5");
//                    dataLine.add("3");
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(fillS(iRepeat == 0, row));
//                    dataLine.add(fillT(iRepeat == 0, row));
//                    dataLine.add(getCellValue(row, 8));
//                    dataLine.add(getCellValue(row, 8));
//                    dataLine.add(iRepeat == 0 ? LOAD_THE_GOODS : UNLOAD_THE_GOODS);
//                    dataLine.add(String.valueOf(iRepeat));
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(getCellValue(row, 2));
//                    dataLine.add(EMPTY);
//                    dataLine.add(getCellValue(row, 3));
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//                    dataLine.add(EMPTY);
//
//                    data.add(dataLine);
//                }
//            }
//        } catch (Exception e) {
//            throw new ConvertProcessingException("не удалось обработать строку:" + row
//                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
//        }
 */
}
