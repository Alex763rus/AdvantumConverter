package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.SberAddressNotFoundException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.jpa.sber.SberAddressDictionary;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SBER;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exception.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SBER;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplSber extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 2;
    private final CrmConfigProperties crmConfigProperties;
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private final static String YAROSLAVSKOE_HIGHWAY = "Ярославское шоссе 222";
    private final static String EXPECTED_TIME_FORMULA = "CHOOSE(1+(B2>=12)+(B2>=23)+(B2>=34)+(B2>=45)+(B2>=56)+(B2>=67)+(B2>=78)+(B2>=89),\"4:00\",\"5:00\",\"6:00\",\"7:00\",\"8:00\",\"9:00\",\"10:00\",\"11:00\")";
    private final static Map<String, String> TK_NAME_NUMBER_MAP = Map.of(
            BUSH_AUTOPROM_ORGANIZATION_NAME, "1",
            SBER_BUSH_AUTOPROM_ORGANIZATION_NAME_1, "1",
            SBER_BUSH_AUTOPROM_ORGANIZATION_NAME_2, "1",
            SBER_SQUIRREL_ORGANIZATION_NAME, "2"
    );
    private List<String> warnings = new ArrayList<>();

    public ConvertServiceImplSber(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_SBER;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SBER;
    }

    private String addressFromFile = EMPTY;
    private SberAddressDictionary cityFromDictionary = null;
    private Map<String, Integer> uniqReisNumbers;

    @Override
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataV2 dataLine = null;

        int row = START_ROW;
        boolean isStart = true;
        String reisNumber = EMPTY;
        String fio = EMPTY;
        String fullFio = EMPTY;
        String carNumber = EMPTY;
        String organization = EMPTY;
        uniqReisNumbers = new HashMap<>();

        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW, 1);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            addressFromFile = getCellValue(0, 0).trim();
            cityFromDictionary = dictionaryService.getSberCity(addressFromFile);
            if (cityFromDictionary == null) {
                throw new SberAddressNotFoundException();
            }
            for (; row <= LAST_ROW; ++row) {
                val dateFromFile = getDateFromFile(row);
                var dateFromFileForSt = dateFromFile;
                val dateString = convertDateFormat(dateFromFile, "ddMMyy");
                val numberUnloading = getIntegerValue(row, 8);
                isStart = numberUnloading == 1;
                if (isStart) {
                    carNumber = deleteSpace(getCellValue(row, 3).replace("RUS", EMPTY));
                    dateFromFileForSt = DateUtils.isSameDay(convertDateFormat(fillT(true, row, dateFromFile), TEMPLATE_DATE_TIME_DOT), dateFromFile) ?
                            dateFromFile : DateUtils.addDays(dateFromFile, -1);
                    fio = prepareFio(getCellValue(row, 4));
                    organization = deleteSpace(getCellValue(row, 10));
                    reisNumber = prepareReisNumber(carNumber, dateString);
                }

                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = ConvertedListDataV2.init()
                            .setColumnAdata(reisNumber)
                            .setColumnBdata(dateFromFile)
                            .setColumnCdata(cityFromDictionary.getCity().trim())
                            .setColumnDdata(organization)
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(5000)
                            .setColumnKdata(12)
                            .setColumnLdata(Integer.parseInt(fillL(row)))
                            .setColumnMdata(Integer.parseInt(fillM(row)))
                            .setColumnNdata(2)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(fillS(isStart, row, isStart ? dateFromFileForSt : dateFromFile))
                            .setColumnTdata(convertDateFormat(fillT(isStart, row, isStart ? dateFromFileForSt : dateFromFile), TEMPLATE_DATE_TIME_DOT))
                            .setColumnUdata(fillU(isStart, row))
                            .setColumnVdata(fillV(isStart, row))
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 0 : numberUnloading)
                            .setColumnYdata(null)
                            .setColumnZdata(null)
                            .setColumnAaData(TK_NAME_NUMBER_MAP.getOrDefault(organization, EMPTY))
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(carNumber)
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(fio)
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .setColumnAiData(EMPTY)
                            .setColumnAjData(EMPTY)
                            .setColumnAkData(EMPTY)
                            .setColumnAlData(EMPTY)
                            .setColumnAmData(EMPTY)
                            .setColumnAnData(EMPTY)
                            .setColumnAoData(EMPTY)
                            .setColumnApData(fio)
                            .setTechFullFio(fio)
                            .build();
                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                }

            }
        } catch (SberAddressNotFoundException | TemperatureNodValidException e) {
            throw e;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }

        return ConvertedBookV2.init()
                .setBookV2(List.of(
                        ConvertedListV2.init()
                                .setHeadersV2(Header.headersOutputClientV2)
                                .setExcelListName(EXPORT)
                                .setExcelListContentV2(data)
                                .build()
                ))
                .setMessage(DONE + warnings.stream().distinct().collect(Collectors.joining("")))
                .setBookName(getConverterName() + UNDERSCORE)
                .build();
    }

    private String prepareReisNumber(String carNumber, String dateString) {
        var reisNumberTmp = carNumber.toUpperCase() + UNDERSCORE + dateString;
        var reisCount = uniqReisNumbers.getOrDefault(reisNumberTmp, 0) + 1;
        uniqReisNumbers.put(reisNumberTmp, reisCount);
        return reisNumberTmp + UNDERSCORE + reisCount;
    }

    private String prepareFio(String value) {
        if (value == null) {
            return EMPTY;
        }
        return value.replace(" ", SPACE).split(" ВУ ")[0];
    }

    private String deleteSpace(String value) {
        return value == null ? EMPTY : value.trim();
    }

    private String prepareTemperature(int row) {
        return getCellValue(row, 15)
                .replace(SPACE, EMPTY)
                .replace("(", EMPTY)
                .replace(")", EMPTY)
                .replace("+", EMPTY);
    }

//    private String generateId() {
//        return String.valueOf((int) (11111 + Math.random() * (99999 - 11111 + 1)));
//    }

    private String fillL(int row) {
        var temperature = prepareTemperature(row).split(",");
        if (temperature.length < 2) {
            throw new TemperatureNodValidException(String.valueOf(row + 1));
        }
        return temperature[0];
    }

    private String fillM(int row) {
        var temperature = prepareTemperature(row).split(",");
        if (temperature.length < 2) {
            throw new TemperatureNodValidException(String.valueOf(row + 1));
        }
        return temperature[1];
    }


    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date getDateFromFile(int row) {
        return convertDateFormat(getCellValue(row, 16), TEMPLATE_DATE_SLASH);
    }

    private Date fillS(boolean isStart, int row, Date dateFromFile) throws ParseException {
        //для погрузки лдя времени первого столбца точки. убытие 6 часов. Обытие Прибытие минус 2 от прибытия.
        //разгрузки:
        val timeIsStart = getCellValue(row, 0);
        val timeIsNotStart = getCellValue(row, 14).split(MINUS)[0];
        var dateStart = convertDateFormat(convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + timeIsStart, TEMPLATE_DATE_TIME_DOT);
        var dateNotStart = convertDateFormat(convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + timeIsNotStart, TEMPLATE_DATE_TIME_DOT);
        if (isStart) {
            if (dateNotStart.before(dateStart)) {
                return DateUtils.addDays(dateStart, -1);
            }
            return dateStart;
        }
        return dateNotStart;
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getSber();
    }

    private String fillT(boolean isStart, int row, Date dateFromFile) throws ParseException {
        if (isStart) {
            val dateT = fillS(isStart, row, dateFromFile);
            val result = DateUtils.addHours(dateT, 2);
            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
        } else {
            val time = getCellValue(row, 14).split(MINUS)[1];
            return convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT) + SPACE + time;
        }
    }

    private String fillU(boolean isStart, int row) {
        return isStart ? cityFromDictionary.getCityAndRegion().trim() : getCellValue(row, 5)
                .replace(TWO_SPACE, SPACE)
                .trim();
    }

    private String fillV(boolean isStart, int row) {
        return isStart ? addressFromFile : getCellValue(row, 5)
                .replace(TWO_SPACE, SPACE)
                .trim();
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
