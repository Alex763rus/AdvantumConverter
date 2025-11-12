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
            var sheetDomino = book.getSheet("Orders");
            for (; !StringUtils.EMPTY.equals(shopNumber = getCellValue(sheetDomino, rowOrder, 3)); ++rowOrder) {
                rowOrdersData.put(shopNumber,
                        RowOrdersData.init()
                                .setShopNumber(shopNumber)
                                .setTimeStart(getCellValue(sheetDomino, rowOrder, 35))
                                .setTimeEnd(getCellValue(sheetDomino, rowOrder, 36))
                                .setAddress(getCellValue(sheetDomino, rowOrder, 16))
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
        Set<RowData> rowData = new LinkedHashSet<>();
        String reisId;
        try {
            var sheetOrders = book.getSheet("ДЛЯ ДОМИНО");
            for (; !StringUtils.EMPTY.equals(reisId = getCellValue(sheetOrders, rowMain, 10)); ++rowMain) {
                var shopNumber = getCellValue(sheetOrders, rowMain, 7);
                //происходит склеивание по номеру рейса (ReisNumber) + номер магазина (ShopNumber):
                rowData.add(RowData.init()
                        .setReisNumber(reisId)
                        .setShopNumber(shopNumber)
                        .setOrganization(getCellValue(sheetOrders, rowMain, 15))
                        .setDateOrder(convertDateFormat(getCellValue(sheetOrders, rowMain, 2)
                                .replaceAll("\"", "")
                                .replaceAll("/", "."), TEMPLATE_DATE_DOT))
                        .setTonnage(fillInteger(getCellValue(sheetOrders, rowMain, 23)) * 1000)
                        .setPackageCount(fillInteger(getCellValue(sheetOrders, rowMain, 28)))
                        .setPointName(getCellValue(sheetOrders, rowMain, 9))
                        .setCarNumber(getCellValue(sheetOrders, rowMain, 20).replaceAll(SPACE, EMPTY))
                        .setFio(getCellValue(sheetOrders, rowMain, 16))
                        .build()
                );
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (кратко)", rowMain, e.getMessage()));
        }

        try {
            var reisNumberTmp = EMPTY;
            var numberUnloading = 0;
            for (RowData row : rowData) {
                isStart = !row.getReisNumber().equals(reisNumberTmp);
                if (isStart) {
                    reisNumberTmp = row.getReisNumber();
                    numberUnloading = 0;
                }
                ++numberUnloading;
                var rowOrderData = rowOrdersData.get(row.getShopNumber());
                if (rowOrderData == null) {
                    int qqq = 0;
                    // исключение
                }

                var dateOrderString = convertDateFormat(row.getDateOrder(), TEMPLATE_DATE_DOT) + SPACE;
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    var dateS = isStart ?
                            dateOrderString + "04:00" :
                            dateOrderString + rowOrderData.getTimeStart();

                    var dateT = isStart ?
                            dateOrderString + "18:00" :
                            dateOrderString + rowOrderData.getTimeEnd();

                    var needTemperage = row.getReisNumber().contains("_T");
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
                            .setColumnLdata(needTemperage ? 2 : null)
                            .setColumnMdata(needTemperage ? 4 : null)
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(convertDateFormat(dateS, TEMPLATE_DATE_TIME_DOT))
                            .setColumnTdata(convertDateFormat(dateT, TEMPLATE_DATE_TIME_DOT))
                            .setColumnUdata(isStart ? "309" : row.getPointName())
                            .setColumnVdata(isStart ? "Внуково" : rowOrderData.getAddress())
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
    private static class RowData {
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
        String address;
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

}
