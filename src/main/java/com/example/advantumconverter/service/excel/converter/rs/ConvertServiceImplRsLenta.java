package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_LENTA;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_LENTA;
import static com.example.advantumconverter.enums.ExcelType.RS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.TEMPLATE_DATE_DOT;
import static org.example.tgcommons.utils.DateConverterUtils.convertDateFormat;

@Component
public class ConvertServiceImplRsLenta extends ConvertServiceBase implements ConvertService {
    private int START_ROW;

    private int LAST_ROW;

    @Override
    public ExcelType getExcelType() {
        return RS;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_RS_LENTA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_LENTA;
    }

    private String START_ROW_TEXT = "ТК/РЦ";
    private Date stockIn = null; //из файла, ожидаемая дата прибытия, дата въезда на погрузку
    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    private static final String POINT_VALISHEVO = "Склад Валищево 3";
    private static final String POINT_VALISHEVO_REAL = "УР8123Л";
    private static final String STANDART_PALLET = "STANDART_PALLET";
    private static final String TEMPLATE_DATE_TIME_DOT_SMALL_YEAR = "dd.MM.yy H:mm";
    private static final String FD = "FD";

    @Override
    @LogExecutionTime(value = "Конвертация v2 RS" + COMPANY_NAME_LENTA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        var startRowMain = 1;
        var startRowFull = 1;
        int rowMain = startRowMain;
        int rowFull = startRowFull;
        String reisId;
        Set<ReisMain> reisMains = new LinkedHashSet<>();
        try {
            var sheetMain = book.getSheetAt(0);


            //Идем по краткому листу:
            for (; !EMPTY.equals(reisId = getCellValue(sheetMain, rowMain, 0)); ++rowMain) {
                reisMains.add(ReisMain.init()
                        .setReisId(reisId)
                        .setStartDelivery(convertDateFormat(getCellValue(sheetMain, rowMain, 3), TEMPLATE_DATE_DOT))
                        .setStartPlan(convertDateFormat(getCellValue(sheetMain, rowMain, 6), TEMPLATE_DATE_TIME_DOT_SMALL_YEAR))
                        .setGroup(getCellValue(sheetMain, rowMain, 10))
                        .setCargoSpace(getIntegerValue(sheetMain, rowMain, 14, 0))
                        .setCargoWeight(getDoubleValue(sheetMain, rowMain, 15))
                        .setCargoVolume(getDoubleValue(sheetMain, rowMain, 16))
                        .build()
                );
            }

        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (кратко)", rowMain, e.getMessage()));
        }
        try {
            //Идем по полному листу:

            var sheetFull = book.getSheetAt(1);
            Set<ReisFull> reisFulls = new LinkedHashSet<>();

            for (; !EMPTY.equals(reisId = getCellValue(sheetFull, rowFull, 0)); ++rowFull) {
                var pointNameFromFile = getCellValue(sheetFull, rowFull, 0);
                var pointOperationType = getCellValue(sheetFull, rowFull, 8);
                var pointType =
                        RETURN_CONTAINERS.equals(pointOperationType) ? "D" :
                                LOAD_THE_GOODS.equals(pointOperationType) ? "P" :
                                        UNLOAD_THE_GOODS.equals(pointOperationType) ? "PD" : EMPTY;
                reisFulls.add(
                        ReisFull.init()
                                .setReisId(reisId)
                                .setPointNumber(getIntegerValue(sheetFull, rowFull, 7, 0))
                                .setPointName(POINT_VALISHEVO.equals(pointNameFromFile) ? POINT_VALISHEVO_REAL : pointNameFromFile)
                                .setPointType(pointType)
                                .build()
                );
            }
            reisMains.forEach(
                    reisMain -> data.addAll(reisFulls.stream()
                            .filter(e -> e.getReisId().equals(reisMain.getReisId()))
                            .sorted(Comparator.comparingInt(ReisFull::getPointNumber))
                            .map(reisFull -> prepare(reisMain, reisFull))
                            .collect(Collectors.toList()))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (подробно)", rowFull, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsClientV2, "Шаблон для Лента");
    }

    private ConvertedListDataRsClientsV2 prepare(ReisMain reisMain, ReisFull reisFull) {
        var isLoad = reisFull.getPointType().equals("PD");
        return ConvertedListDataRsClientsV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisFull.getPointNumber())
                .setColumnCdata(reisFull.getPointName())
                .setColumnDdata(reisFull.getPointType())
                .setColumnEdata(reisMain.getStartDelivery())
                .setColumnFdata(reisMain.getStartPlan())
                .setColumnGdata(reisMain.getGroup())
                .setColumnHdata(STANDART_PALLET)
                .setColumnIdata(isLoad ? reisMain.getCargoSpace() : 1)
                .setColumnJdata(isLoad ? reisMain.getCargoWeight() : 1)
                .setColumnKdata(isLoad ? reisMain.getCargoVolume() : 1)
                .setColumnLdata(FD)
                .setColumnMdata(EMPTY)
                .setColumnNdata(reisMain.getReisId())
                .build();
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    private static class ReisMain {
        private String reisId;
        private Date startDelivery;
        private Date startPlan;
        private String group;
        private Integer cargoSpace; //грузомест
        private Double cargoWeight; //вес груза
        private Double cargoVolume; //объем груза

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReisMain that = (ReisMain) o;
            return Objects.equals(reisId, that.reisId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reisId);
        }
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    private static class ReisFull {
        private String reisId;
        private Integer pointNumber;
        private String pointName;
        private String pointType;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReisFull that = (ReisFull) o;
            return Objects.equals(reisId, that.reisId) && Objects.equals(pointNumber, that.pointNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reisId, pointNumber);
        }
    }
//    private String fillS(int row, boolean isStart, Long code) throws ParseException {
//        if (isStart) {
//            return convertDateFormat(stockIn, TEMPLATE_DATE_TIME_DOT);
//        }
//        val date = convertDateFormat(stockIn, TEMPLATE_DATE_DOT);
//        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
//        if (lentaDictionary == null) {
//            return convertDateFormat(date, TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT) + " 10:00";
//        }
//        val timeShop = convertDateFormat(lentaDictionary.getTimeShop(), TEMPLATE_TIME);
//        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME);
//        val dateTimeS = timeShop.after(timeStock) ? DateUtils.addDays(stockIn, -1) : stockIn;
//        //ЕСЛИ РАСЧИТАННОЕ ВРЕМЯ УБЫТИЯ меньше чем дата прибытия на погрузку, то плюсуем 1 день и в S и в T
//        val dateTimeT = calculateDateTimeT(row, lentaDictionary);
//        val result = dateTimeT.before(stockIn) ? DateUtils.addDays(dateTimeS, 1) : dateTimeS;
//        //Если второе время меньше первого, то у первого времени отнимать 1 день
//        val dateTimeResultT = convertDateFormat(fillT(row, isStart, code), TEMPLATE_DATE_TIME_DOT);
//        val answer = dateTimeResultT.before(result) ? DateUtils.addDays(result, -1) : result;
//
//        return convertDateFormat(answer, TEMPLATE_DATE_TIME_DOT);
//    }
//
//    private String fillT(int row, boolean isStart, Long code) throws ParseException {
//        if (isStart) {
//            val result = DateUtils.addHours(stockIn, 3);
//            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
//        }
//        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
//        if (lentaDictionary == null) {
//            return convertDateFormat(stockIn, TEMPLATE_DATE_DOT) + " 22:00";
//        }
//        //ЕСЛИ РАСЧИТАННОЕ ВРЕМЯ УБЫТИЯ меньше чем дата прибытия на погрузку, то плюсуем 1 день и в S и в T
//        val dateTimeT = calculateDateTimeT(row, lentaDictionary);
//        val result = dateTimeT.before(stockIn) ? DateUtils.addDays(dateTimeT, 1) : dateTimeT;
//        return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
//    }
//
//    private Date calculateDateTimeT(int row, LentaDictionary lentaDictionary) throws ParseException {
//        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME, TEMPLATE_TIME);
//        val dateResultString = convertDateFormat(stockIn, TEMPLATE_DATE_DOT);
//        val dateTimeResult = dateResultString + SPACE + (isLentaCarAndTemperate(row) ? "18:00" : timeStock);
//        return convertDateFormat(dateTimeResult, TEMPLATE_DATE_TIME_DOT);
//    }
//
//    private boolean isLentaCarAndTemperate(int row) {
//        val carName = getCarName(row);
//        val car = dictionaryService.getCarOrElse(carName, null);
//        return carName.length() > 5 && car != null && car.getTemperatureMin() <= -18;
//    }
//
//    private String getCarNumber(int row) {
//        return getCellValue(row, 9).replaceAll(SPACE, EMPTY);
//    }
//
//    private String fillD(int row) {
//        val companyName = getCellValue(row, 13);
//        val carNumber = getCarNumber(row);
//        if (companyName.toUpperCase().contains("ЛЕНТА")
//                && dictionaryService.getTsCityBrief(carNumber) == null
//                && (dictionaryService.getCarNumberOrElse(carNumber, null) == null)) {
//            return COMPANY_OOO_LENTA_HIRING;
//        }
//        return companyName;
//    }
//
//    private String fillL(int row) {
//        val carName = getCarName(row);
//        val car = dictionaryService.getCarOrElse(carName, null);
//        if (carName.length() < 5 || car == null) {
//            return "2";
//        }
//        return String.valueOf(car.getTemperatureMin());
//    }
//
//    private String fillM(int row) {
//        val carName = getCarName(row);
//        val car = dictionaryService.getCarOrElse(carName, null);
//        if (carName.length() < 5 || car == null) {
//            return "6";
//        }
//        return String.valueOf(car.getTemperatureMax());
//    }
//
//    private String fillJ(int row) {
//        val carNumber = getCellValue(row, 9).replaceAll(SPACE, EMPTY);
//        val lentaCar = dictionaryService.getLentaCarOrElse(carNumber, null);
//        if (lentaCar != null) {
//            return String.valueOf(lentaCar.getTonnage());
//        }
//        val carName = getCarName(row);
//        if (carName.length() < 5) {
//            val doubleValue = Double.parseDouble(carName.replaceAll(",", ".")) * 1000;
//            return String.valueOf((int) doubleValue);
//        }
//        val car = dictionaryService.getCar(carName);
//        return String.valueOf(car.getTonnage());
//    }
//
//    private String fillC(int row, Long code) {
//        /*
//        В базе ищем по номеру машины. Если нашли, берем бриф - название компании
//        Если в базе не нашли,берем код из первого столбца, ищем в справочнике.
//        Если нашли, то Лента + название региона из справочника. Если нет, то "Нет региона"
//         */
//        val tsCityBrief = dictionaryService.getTsCityBrief(getCarNumber(row));
//        if (tsCityBrief != null) {
//            return tsCityBrief;
//        }
//        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
//        return lentaDictionary == null ? "Нет региона" : "Лента (" + lentaDictionary.getRegion() + ")";
//    }
//
//    private String fillAE(int row) {
//        return WordUtils.capitalize(getCellValue(row, 6).toLowerCase());
//    }
//
//
//    private Date getExpectedTimeIncome(int row) throws ParseException {
//        val date = getCellValue(row, 14);
//        return convertDateFormat(date, date.contains(".") ? TEMPLATE_DATE_TIME_DOT : TEMPLATE_DATE_TIME_SLASH);
//    }
//
//    private String getCarName(int row) {
//        return getCellValue(row, 7).replaceAll("\\\\", EMPTY);
//    }
//
//    private Long getCode(int row) {
//        String resultString;
//        val cellValue = getCellValue(row, 0).replaceAll(SPACE, EMPTY).replaceFirst("^\\D*", EMPTY);
//        val indexDash = cellValue.indexOf(MINUS);
//        if (indexDash != -1) {
//            resultString = indexDash != -1 ? cellValue.substring(0, indexDash) : cellValue;
//        } else {
//            val indexUnderscore = cellValue.indexOf(UNDERSCORE);
//            resultString = indexUnderscore != -1 ? cellValue.substring(0, indexUnderscore) : cellValue;
//        }
//        try {
//            return Long.parseLong(resultString);
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    private String fillU(int row, Long code) {
//        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
//        return lentaDictionary == null ? "Код адреса не найден в справочнике: " + code : lentaDictionary.getAddressName();
//    }
//
//    private String getValueOrDefault(int row, int slippage, int col) {
//        row = row + slippage;
//        if (row < START_ROW || row > LAST_ROW) {
//            return EMPTY;
//        }
//        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
//            return EMPTY;
//        }
//        return getCellValue(sheet.getRow(row).getCell(col));
//    }


}
