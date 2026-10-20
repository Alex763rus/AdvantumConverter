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
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_BASKET;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_BASKET;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_BASKET;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplBasket extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 4;
    private final int COLUMN_STEP = 5;
    private final String ORGANISATION = "OOO ANGLESEY FOOD";
    private final String REGION_BASE = "R003";
    private final String ADDRESS_BASE = "R003 - XK KUYLUK";
    private final CrmConfigProperties crmConfigProperties;
    private int LAST_ROW;
    private List<String> warnings = new ArrayList<>();


    public ConvertServiceImplBasket(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_BASKET;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_BASKET;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_BASKET, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        var data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;

        boolean isStart = true;
        int row = START_ROW;
        int col = 0;
        int lastRow = 0;
        int countReis = 0;
        try {
            sheet = book.getSheetAt(0);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            var datefromfile = getDateFromFile();
            var datefromfileString = convertDateFormat(datefromfile, TEMPLATE_DATE);

            for (col = 0; col <= LAST_COLUMN_NUMBER; col = col + COLUMN_STEP) {
                isStart = true;
                ++countReis;
                lastRow = getLastRow(START_ROW, col);
                var numberUnloading = 0;
                for (row = START_ROW; row <= lastRow; ++row) {
                    var region = getRegion(row, col + 2);
                    if (EMPTY.equals(region)) {
                        break;
                    }
                    for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                        dataLine = ConvertedListDataClientsV2.init()
                                .setColumnAdata(getColumnA(countReis, datefromfileString))
                                .setColumnBdata(datefromfile)
                                .setColumnCdata(ORGANISATION)
                                .setColumnDdata(ORGANISATION)
                                .setColumnEdata(null)
                                .setColumnFdata(REFRIGERATOR)
                                .setColumnGdata(EMPTY)
                                .setColumnHdata(EMPTY)
                                .setColumnIdata(34)
                                .setColumnJdata(10000)
                                .setColumnKdata(12)
                                .setColumnLdata(2)
                                .setColumnMdata(8)
                                .setColumnNdata(null)
                                .setColumnOdata(null)
                                .setColumnPdata(null)
                                .setColumnQdata(null)
                                .setColumnRdata(null)
                                .setColumnSdata(fillS(row, col, datefromfile))
                                .setColumnTdata(fillT(row, col, datefromfile))
                                .setColumnUdata(fillU(isStart, region))
                                .setColumnVdata(fillV(isStart, region))
                                .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                                .setColumnXdata(numberUnloading)
                                .setColumnYdata(null)
                                .setColumnZdata(null)
                                .setColumnAaData(EMPTY)
                                .setColumnAbData(EMPTY)
                                .setColumnAcData(getCellValue(2, col + 1))
                                .setColumnAdData(EMPTY)
                                .setColumnAeData(getCellValue(2, col + 2))
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
                        if (!isStart) {
                            break;
                        }
                        isStart = false;
                        ++numberUnloading;
                    }
                    ++numberUnloading;
                }
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    private String getColumnA(int reisNumber, String dateFromFile) {
        return String.format("Маршрут_%d_%s", reisNumber, dateFromFile);
    }

    private Date fillS(int row, int col, Date dateFromFileString) throws ParseException {
        return calculateDate(row, col + 3, dateFromFileString);
    }

    private Date fillT(int row, int col, Date dateFromFileString) throws ParseException {
        return calculateDate(row, col + 4, dateFromFileString);
    }

    private Date calculateDate(int row, int dateCol, Date dateFromFileString) throws ParseException {
        var time = convertTo24HourFormat(getCellValue(row, dateCol));
        var resultString = convertDateFormat(dateFromFileString, TEMPLATE_DATE_DOT) + SPACE + time;
        return convertDateFormat(resultString, TEMPLATE_DATE_TIME_DOT);
    }

    private static final DateTimeFormatter FLEXIBLE_INPUT_FORMATTER = new DateTimeFormatterBuilder()
            // Вариант 1: 12-часовой формат с секундами (например, "1:00:00 PM", "11:00:00 AM")
            .appendOptional(DateTimeFormatter.ofPattern("h:mm:ss a", Locale.ENGLISH))
            // Вариант 2: 12-часовой формат без секунд (например, "1:00 PM")
            .appendOptional(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
            // Вариант 3: 24-часовой формат с секундами (например, "15:45:12")
            .appendOptional(DateTimeFormatter.ofPattern("H:mm:ss"))
            // Вариант 4: 24-часовой формат без секунд (например, "10:20", "8:05")
            .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
            .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private String convertTo24HourFormat(String inputTime) {
        if (inputTime == null || inputTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Входная строка времени не может быть пустой");
        }

        try {
            // Удаляем лишние пробелы и приводим PM/AM к верхнему регистру для надежности
            String cleanedInput = inputTime.trim().toUpperCase();

            LocalTime time = LocalTime.parse(cleanedInput, FLEXIBLE_INPUT_FORMATTER);
            return time.format(OUTPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат времени: " + inputTime, e);
        }
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date getDateFromFile() {
        var dateFromfile = getCellDate(sheet, 1, 0);
        return Objects.requireNonNullElseGet(dateFromfile, Date::new);
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getSber();
    }

    private String getRegion(int row, int col) {
        return getCellValue(row, col);
    }

    private String fillU(boolean isStart, String region) {
        return isStart ? REGION_BASE : region;
    }

    private String fillV(boolean isStart, String region) {
        return isStart ? ADDRESS_BASE : dictionaryService.getBasketAddress(region);
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
