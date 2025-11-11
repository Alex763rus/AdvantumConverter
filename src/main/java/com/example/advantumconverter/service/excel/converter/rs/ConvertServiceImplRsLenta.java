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
import lombok.*;
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

    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    private static final String POINTTYPE_D = "D";
    private static final String POINTTYPE_P = "P";
    private static final String POINTTYPE_PD = "PD";
    private static final String FD = "FD";
    private static final String POINT_VALISHEVO = "Склад Валищево 3";
    private static final String POINT_VALISHEVO_REAL = "УР8123Л";
    private static final String STANDART_PALLET = "STANDART_PALLET";
    private static final String TEMPLATE_DATE_TIME_DOT_SMALL_YEAR = "dd.MM.yy H:mm";


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
                var pointNameFromFile = getCellValue(sheetFull, rowFull, 9);
                var pointOperationType = getCellValue(sheetFull, rowFull, 8);
                var pointType = switch (pointOperationType) {
                    case RETURN_CONTAINERS -> POINTTYPE_D;
                    case LOAD_THE_GOODS -> POINTTYPE_P;
                    case UNLOAD_THE_GOODS -> POINTTYPE_PD;
                    default -> EMPTY;
                };
                reisFulls.add(
                        ReisFull.init()
                                .setReisId(reisId)
                                .setPointNumber(getIntegerValue(sheetFull, rowFull, 7, 0))
                                .setPointName(POINT_VALISHEVO.equals(pointNameFromFile) ? POINT_VALISHEVO_REAL : pointNameFromFile)
                                .setPointType(pointType)
                                .setCargoSpace(getIntegerValue(sheetFull, rowFull, 25, 0))
                                .setCargoWeight(getDoubleValue(sheetFull, rowFull, 26))
                                .setCargoVolume(getDoubleValue(sheetFull, rowFull, 27))
                                .build()
                );
            }
            reisMains.forEach(
                    reisMain -> data.addAll(reisFulls.stream()
                            .filter(e -> e.getReisId().equals(reisMain.getReisId()))
                            .sorted(Comparator.comparingInt(ReisFull::getPointNumber))
                            .map(reisFull -> prepareData(reisMain, reisFull))
                            .collect(Collectors.toList()))
            );
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_RS_ERROR, "Маршруты (подробно)", rowFull, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName(), Header.headersOutputRsClientV2, "Шаблон для Лента");
    }

    private ConvertedListDataRsClientsV2 prepareData(ReisMain reisMain, ReisFull reisFull) {
        var isLoad = POINTTYPE_PD.equals(reisFull.getPointType());
        return ConvertedListDataRsClientsV2.init()
                .setColumnAdata(EMPTY)
                .setColumnBdata(reisFull.getPointNumber())
                .setColumnCdata(reisFull.getPointName())
                .setColumnDdata(reisFull.getPointType())
                .setColumnEdata(reisMain.getStartDelivery())
                .setColumnFdata(reisMain.getStartPlan())
                .setColumnGdata(reisMain.getGroup())
                .setColumnHdata(STANDART_PALLET)
                .setColumnIdata(isLoad ? reisFull.getCargoSpace() : 1)
                .setColumnJdata(isLoad ? reisFull.getCargoWeight() : 1)
                .setColumnKdata(isLoad ? reisFull.getCargoVolume() : 1)
                .setColumnLdata(FD)
                .setColumnMdata(EMPTY)
                .setColumnNdata(reisMain.getReisId())
                .build();
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisMain {
        @EqualsAndHashCode.Include
        private String reisId;
        private Date startDelivery;
        private Date startPlan;
        private String group;
    }

    @Getter
    @Setter
    @Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class ReisFull {
        @EqualsAndHashCode.Include
        private String reisId;
        @EqualsAndHashCode.Include
        private Integer pointNumber;
        private String pointName;
        private String pointType;
        private Integer cargoSpace; //грузомест
        private Double cargoWeight; //вес груза
        private Double cargoVolume; //объем груза
    }

}
