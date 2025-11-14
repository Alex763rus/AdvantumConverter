package com.example.advantumconverter.service.excel.converter.rs;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsInnerLentaV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_RS_INNER_LENTA;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_RS_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_RS_INNER_LENTA;
import static com.example.advantumconverter.enums.ExcelType.RS_INNER_LENTA;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
public class ConvertServiceImplRsInnerLenta extends ConvertServiceBase implements ConvertService {

    @Override
    public ExcelType getExcelType() {
        return RS_INNER_LENTA;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_RS_INNER_LENTA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_RS_INNER_LENTA;
    }

    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    private static final String PALLETA = "Паллета";
    private static final String ROLIKS = "ролики";

    @Override
    @LogExecutionTime(value = "Конвертация v2 RS внутренний" + COMPANY_NAME_LENTA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        var mainListData = readMainList(book);
        var spWindowsData = readSpWindowsList(book);
        var spParamsData = readSpParamsList(book);
        try {
            mainListData.forEach(
                    reisMain -> data.add(prepareData(reisMain, spWindowsData, spParamsData))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Обработка всех листов", -1, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsLentaClientV2, "Шаблон для Лента");
    }

    private ConvertedListDataRsInnerLentaV2 prepareData(
            ReisMain reisMain,
            Map<String, SpWindows> spWindowsData,
            Map<String, SpParams> spParamsData) {
        var window = spWindowsData.get(reisMain.getNumberYr());
        var params = spParamsData.get(reisMain.getNumberYr());
        return ConvertedListDataRsInnerLentaV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisMain.getDateDelivery())
                .setColumnCdata("8023")
                .setColumnDdata(EMPTY)
                .setColumnEdata(reisMain.getNumberYr())
                .setColumnFdata(reisMain.getAddress())
                .setColumnGdata(EMPTY)
                .setColumnHdata(EMPTY)
                .setColumnIdata(window.getTimeStart())
                .setColumnJdata(window.getTimeEnd())
                .setColumnKdata(1)
                .setColumnLdata(params.getTypeGm())
                .setColumnMdata(
                        PALLETA.equalsIgnoreCase(params.getTypeGm()) ? 300 :
                                ROLIKS.equalsIgnoreCase(params.getTypeGm()) ? 150 : 0
                )
                .setColumnNdata(params.getTonnageMax())
                .setTechCountRepeat(reisMain.getPalletCount())
                .build();
    }

    private List<ReisMain> readMainList(XSSFWorkbook book) {
        int row = 3;
        String listName = "Исходные данные заказов";
        try {
            var sheetData = new ArrayList<ReisMain>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 5)); ++row) {
                sheetData.add(
                        ReisMain.init()
                                .setDateDelivery(getCellDate(sheetMain, row, 3))
                                .setNumberYr(numberYr)
                                .setAddress(getCellValue(sheetMain, row, 8))
                                .setPalletCount(getIntegerValue(sheetMain, row, 10, 0))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }


    private Map<String, SpWindows> readSpWindowsList(XSSFWorkbook book) {
        int row = 3;
        String listName = "Сп-к ТТ-окна";
        try {
            var sheetData = new HashMap<String, SpWindows>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 3)); ++row) {
                var times = getCellValue(sheetMain, row, 5).split("-");
                var timeStart = times[0].split(":");
                var timeEnd = times[0].split(":");

                var timeStartH = timeStart[0].length() == 2 ?
                        timeStart[0] :
                        timeStart[0].replace("00", "0");

                var timeEndH = timeEnd[0].length() == 2 ?
                        timeEnd[0] :
                        timeEnd[0].replace("00", "0");
                sheetData.put(
                        numberYr,
                        SpWindows.init()
                                .setNumberYr(numberYr)
                                .setTimeStart(timeStartH + ":" + timeStart[1])
                                .setTimeEnd(timeEndH + ":" + timeEnd[1])
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    private Map<String, SpParams> readSpParamsList(XSSFWorkbook book) {
        int row = 2;
        String listName = "СП -параметры ТК";
        try {
            var sheetData = new HashMap<String, SpParams>();
            var sheetMain = book.getSheet(listName);
            String numberYr;
            for (; !EMPTY.equals(numberYr = getCellValue(sheetMain, row, 0)); ++row) {
                var times = getCellValue(sheetMain, row, 5).split("-");
                sheetData.put(
                        numberYr,
                        SpParams.init()
                                .setNumberYr(numberYr)
                                .setTypeGm(getCellValue(sheetMain, row, 2))
                                .setTonnageMax(getCellValue(sheetMain, row, 3))
                                .build()
                );
            }
            return sheetData;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, listName, row, e.getMessage()));
        }
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisMain {
        @EqualsAndHashCode.Include
        private String numberYr;
        private Date dateDelivery;
        private String address;
        private Integer palletCount;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class SpWindows {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String timeStart;
        private String timeEnd;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
//    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class SpParams {
        @EqualsAndHashCode.Include
        private String numberYr;
        private String typeGm;
        private String tonnageMax;
//        private Date dateDelivery;
//        private String numberYr;
//        private String address;
//        private Integer palletCount;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisFull {
        //        @EqualsAndHashCode.Include
//        private String reisId;
//        @EqualsAndHashCode.Include
        private Integer pointNumber;
        private String pointName;
        private String pointType;
        private Integer cargoSpace; //грузомест
        private Double cargoWeight; //вес груза
        private Double cargoVolume; //объем груза
    }

}
