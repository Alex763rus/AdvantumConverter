package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.tgcommons.utils.DateConverterUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BASKET_FAYZABAD;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_BASKET_FAYZABAD;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BASKET_FAYZABAD;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplBasketFayzabad extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private final String ORGANISATION = "OOO ANGLESEY FOOD";
    private final String REGION_BASE = "R003";
    private final String ADDRESS_BASE = "R003 - XK KUYLUK";
    private final CrmConfigProperties crmConfigProperties;
    private int LAST_ROW;
    private List<String> warnings = new ArrayList<>();


    public ConvertServiceImplBasketFayzabad(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_BASKET_FAYZABAD;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_BASKET_FAYZABAD;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }


    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_BASKET_FAYZABAD, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;

        boolean isStart = true;
        int row = START_ROW;
        int numberUnloading = 0;
        Reis currentReis = null;
        try {
            sheet = book.getSheetAt(0);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            LAST_ROW = getLastRow(START_ROW, 0);

            for (; row <= LAST_ROW; ++row) {
                var aCell = getCellValue(sheet, row, 0);
                if (aCell.startsWith("$")) {
                    isStart = false;
                    continue;
                }
                if (isInteger(aCell)) {
                    //новый рейс:
                    currentReis = new Reis(
                            aCell,
                            getCellValue(sheet, row, 1),
                            convertDateFormat(getCellValue(sheet, row, 30), DateConverterUtils.TEMPLATE_DATE_SLASH)
                    );
                    isStart = true;
                    numberUnloading = 0;
                    continue;
                }
                //разгрузка:
                var transportInfo = extractFirstTwoNumbers(currentReis.transportType());
                var planDateString = convertDateFormat(currentReis.planDate(), TEMPLATE_DATE_DOT);

                dataLine = ConvertedListDataClientsV2.init()
                        .setColumnAdata(currentReis.reisNumber())
                        .setColumnBdata(currentReis.planDate())
                        .setColumnCdata(ORGANISATION)
                        .setColumnDdata(ORGANISATION)
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(EMPTY)
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(null)
                        .setColumnJdata(Optional.ofNullable(transportInfo.first).orElse(5) * 1000)
                        .setColumnKdata(Optional.ofNullable(transportInfo.second).orElse(12))
                        .setColumnLdata(2)
                        .setColumnMdata(8)
                        .setColumnNdata(null)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(fillS(sheet, row, planDateString))
                        .setColumnTdata(fillT(sheet, row, planDateString))
                        .setColumnUdata(aCell)
                        .setColumnVdata(getCellValue(sheet, row, 2))
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(numberUnloading)
                        .setColumnYdata(null)
                        .setColumnZdata(null)
                        .setColumnAaData(EMPTY)
                        .setColumnAbData(EMPTY)
                        .setColumnAcData(null)
                        .setColumnAdData(EMPTY)
                        .setColumnAeData(null)
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
                        .build();
                data.add(dataLine);

                isStart = false;
                ++numberUnloading;
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    record Pair<A, B>(A first, B second) {
    }

    private Pair<Integer, Integer> extractFirstTwoNumbers(String input) {
        // Если строка пустая, сразу возвращаем пару с нулами
        if (input == null || input.isEmpty()) {
            return new Pair<>(null, null);
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        Integer first = null;
        Integer second = null;

        if (matcher.find()) {
            first = Integer.parseInt(matcher.group());
        }

        if (matcher.find()) {
            second = Integer.parseInt(matcher.group());
        }

        // Возвращаем пару в любом случае (значения внутри будут Integer или null)
        return new Pair<>(first, second);
    }

    private Date fillS(XSSFSheet sheet, int row, String dateFromFileString) throws ParseException {
        return calculateDate(sheet, row, 13, dateFromFileString);
    }

    private Date fillT(XSSFSheet sheet, int row, String dateFromFileString) throws ParseException {
        return calculateDate(sheet, row, 14, dateFromFileString);
    }

    private Date calculateDate(XSSFSheet sheet, int row, int col, String dateFromFileString) throws ParseException {
        var time = convertDateFormat(getCellDate(sheet, row, col), "HH:mm:ss");
        var dateTimeString = dateFromFileString + " " + time;
        return convertDateFormat(dateTimeString, TEMPLATE_DATE_TIME_DOT);
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getSber();
    }

    record Reis(String reisNumber, String transportType, Date planDate) {
    }

    protected int getLastRow(int startRow, int col) {
        int i = startRow;
        for (; ; i++) {
            if (getCellValue(i, col).equals(EMPTY)) {
                if (getCellValue(i + 1, col).equals(EMPTY)) {
                    break;
                }
            }
        }
        return i - 1;
    }
}
