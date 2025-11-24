package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_FRAGRANT_WORLD;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_FRAGRANT_WORLD;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_FRAGRANT_WORLD;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplFragrantWorld extends ConvertServiceBase implements ConvertService {

    @Override
    public String getConverterName() {
        return FILE_NAME_FRAGRANT_WORLD;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_FRAGRANT_WORLD;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    private final int START_ROW = 1;
    private List<String> warnings = new ArrayList<>();

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_FRAGRANT_WORLD, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;
        //========================================================
        var startRowOrders = 3;
        int rowOrder = startRowOrders;
        Map<String, RowOrdersData> rowOrdersData = new HashMap<>();
        try {
            String shopNumber;
            String lastTimePointStart = EMPTY;
            var ordersSheet = book.getSheet("Orders");
            for (; !StringUtils.EMPTY.equals(shopNumber = getCellValue(ordersSheet, rowOrder, 3)); ++rowOrder) {
                var pointStartFromFile = getCellValue(ordersSheet, rowOrder, 9);
                lastTimePointStart = EMPTY.equals(pointStartFromFile) ? lastTimePointStart : pointStartFromFile;

                rowOrdersData.put(shopNumber,
                        RowOrdersData.init()
                                .setShopNumber(shopNumber)
                                .setTimeStart(getCellValue(ordersSheet, rowOrder, 35))
                                .setTimeEnd(getCellValue(ordersSheet, rowOrder, 36))
                                .setCity(getCellValue(ordersSheet, rowOrder, 15))
                                .setAddress(getCellValue(ordersSheet, rowOrder, 16))
                                .setTimePointStart(lastTimePointStart)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Orders", rowOrder, e.getMessage()));
        }
        //==================================================
        boolean isStart = true;
        var startRowMain = 1;
        int rowMain = startRowMain;
        Set<RowDominoData> rowDominoData = new LinkedHashSet<>();
        String reisNumber;
        try {
            var dominoSheet = book.getSheet("ДЛЯ ДОМИНО");
            //берем первое значение для всего файла:
            var dateOrder = convertDateFormat(getCellValue(dominoSheet, rowMain, 2)
                    .replaceAll("\"", "")
                    .replaceAll("/", "."), TEMPLATE_DATE_DOT);
            var dateOrderString = "_" + convertDateFormat(dateOrder, TEMPLATE_DATE);
            for (; !StringUtils.EMPTY.equals(reisNumber = prepareReisNumer(dominoSheet, rowMain, dateOrderString)); ++rowMain) {
                var shopNumber = getCellValue(dominoSheet, rowMain, 7);
                //происходит склеивание по номеру рейса (reisNumber) + номер магазина (ShopNumber):
                rowDominoData.add(RowDominoData.init()
                                .setReisNumber(reisNumber)
                                .setShopNumber(shopNumber)
                                .setOrganization(getCellValue(dominoSheet, rowMain, 15))
                                .setDateOrder(dateOrder)
                                .setTonnage(fillInteger(getCellValue(dominoSheet, rowMain, 23)) * 1000)
                                .setPackageCount(fillInteger(getCellValue(dominoSheet, rowMain, 28)))
                                .setPointName(getCellValue(dominoSheet, rowMain, 9))
                                .setCarNumber(getCellValue(dominoSheet, rowMain, 20).replaceAll(SPACE, EMPTY))
                                .setFio(getCellValue(dominoSheet, rowMain, 16))
//                        .setTimeStart(getCellValue(dominoSheet, rowMain, 13))
//                        .setTimeEnd(getCellValue(dominoSheet, rowMain, 14))
                                .build()
                );
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (кратко)", rowMain, e.getMessage()));
        }

        try {
            String dateOrderString = null;
            var reisNumberTmp = EMPTY;
            var numberUnloading = 0;
            for (RowDominoData row : rowDominoData) {
                isStart = !row.getReisNumber().equals(reisNumberTmp);
                if (isStart) {
                    reisNumberTmp = row.getReisNumber();
                    numberUnloading = 0;
                }
                ++numberUnloading;
                var rowOrderData = rowOrdersData.get(row.getShopNumber());
                if (dateOrderString == null) {
                    //берем первое значение для всего файла:
                    dateOrderString = convertDateFormat(row.getDateOrder(), TEMPLATE_DATE_DOT) + SPACE;
                }
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {

                    Date dateSResult = null;
                    String dateSString = null;
                    Date dateTResult = null;
                    String dateTString = null;
                    if (isStart) {
                        //S:
                        dateSString = dateOrderString + rowOrderData.getTimePointStart();
                        dateSResult = DateUtils.addMinutes(convertDateFormat(dateSString, TEMPLATE_DATE_TIME_DOT), -20);
                        //T:
                        dateTString = dateOrderString + rowOrderData.getTimePointStart();
                        dateTResult = DateUtils.addHours(convertDateFormat(dateTString, TEMPLATE_DATE_TIME_DOT), 2);
                    } else {
                        //S:
                        dateSString = dateOrderString + rowOrderData.getTimeStart();
                        dateSResult = convertDateFormat(dateSString, TEMPLATE_DATE_TIME_DOT);
                        //T:
                        dateTString = dateOrderString + rowOrderData.getTimeEnd();
                        dateTResult = convertDateFormat(dateTString, TEMPLATE_DATE_TIME_DOT);
                    }

                    var needTemperage = !row.getReisNumber().contains("Т_"); //если нет T_, то заполняем
                    dataLine = ConvertedListDataClientsV2.init()
                            .setColumnAdata(row.getReisNumber())
                            .setColumnBdata(row.getDateOrder())
                            .setColumnCdata(COMPANY_NAME_FRAGRANT_WORLD)
                            .setColumnDdata(row.getOrganization())
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(row.getTonnage())
                            .setColumnKdata(row.getPackageCount())
                            .setColumnLdata(needTemperage ? 4 : null)
                            .setColumnMdata(needTemperage ? 6 : null)
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(dateSResult)
                            .setColumnTdata(dateTResult)
                            .setColumnUdata(isStart ? "309" : row.getPointName())
                            .setColumnVdata(isStart ? "Внуково" : rowOrderData.getCity() + SPACE + rowOrderData.getAddress())
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 0 : numberUnloading)
                            .setColumnYdata(null)
                            .setColumnZdata(null)
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(row.getCarNumber())
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(row.getFio())
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .setColumnAiData(null)
                            .setColumnAjData(EMPTY)
                            .setColumnAkData(EMPTY)
                            .setColumnAlData(EMPTY)
                            .setColumnAmData(EMPTY)
                            .setColumnAnData(EMPTY)
                            .setColumnAoData(EMPTY)
                            .setColumnApData(null)
                            .setColumnAqData(null)
                            .setColumnArData(null)
                            .setTechFullFio(null)
                            .build();
                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, rowMain, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    private String prepareReisNumer(XSSFSheet sheetOrders, int rowMain, String currentDate) {
        var reisNumber = getCellValue(sheetOrders, rowMain, 10);
        if (EMPTY.equals(reisNumber)) {
            return EMPTY;
        }
        return reisNumber + currentDate;
    }

    private Integer fillInteger(String value) {
        try {
            return (int) Double.parseDouble(value.replaceAll("[^\\d.]+", EMPTY));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class RowDominoData {
        @EqualsAndHashCode.Include
        String reisNumber;
        @EqualsAndHashCode.Include
        String shopNumber;
        String organization;
        Date dateOrder;
        Integer tonnage;
        Integer packageCount;
        String pointName;
        String carNumber;
        String fio;
//        String timeEnd;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class RowOrdersData {
        @EqualsAndHashCode.Include
        String shopNumber;
        String timeStart;
        String timeEnd;
        String city;
        String address;
        String timePointStart;
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

}
