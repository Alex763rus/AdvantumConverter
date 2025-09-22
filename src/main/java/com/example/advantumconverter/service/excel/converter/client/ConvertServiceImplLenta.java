package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.jpa.lenta.LentaDictionary;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_LENTA;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_LENTA;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplLenta extends ConvertServiceBase implements ConvertService {
    private int START_ROW;

    private int LAST_ROW;

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_LENTA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_LENTA;
    }

    private String START_ROW_TEXT = "ТК/РЦ";


    private Date stockIn = null; //из файла, ожидаемая дата прибытия, дата въезда на погрузку
    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2", unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataV2 dataLine = null;

        int row = 0;
        boolean isStart = true;
        String fio = EMPTY;
        try {
            sheet = book.getSheetAt(0);
            START_ROW = getStartRow(START_ROW_TEXT);
            row = START_ROW;
            int counterCopy = 1;
            String columnC = EMPTY;

            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                isStart = isStart(row);
                if (isStart) {
                    stockIn = getExpectedTimeIncome(row);
                    counterCopy = 1;
                    columnC = fillC(row);
                }
                dataLine = ConvertedListDataV2.init()
                        .setColumnAdata(getCellValue(row, 1))
                        .setColumnBdata(new Date())
                        .setColumnCdata(columnC)
                        .setColumnDdata(fillD(row))
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(EMPTY)
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(null)
                        .setColumnJdata(Integer.parseInt(fillJ(row)))
                        .setColumnKdata(1)
                        .setColumnLdata(Integer.parseInt(fillL(row)))
                        .setColumnMdata(Integer.parseInt(fillM(row)))
                        .setColumnNdata(2)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(convertDateFormat(fillS(row, isStart), TEMPLATE_DATE_TIME_DOT))
                        .setColumnTdata(convertDateFormat(fillT(row, isStart), TEMPLATE_DATE_TIME_DOT))
                        .setColumnUdata(String.valueOf(getCode(row)))
                        .setColumnVdata(fillU(row))
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(counterCopy)
                        .setColumnYdata(null)
                        .setColumnZdata(null)
                        .setColumnAaData(EMPTY)
                        .setColumnAbData(EMPTY)
                        .setColumnAcData(getCarNumber(row))
                        .setColumnAdData(EMPTY)
                        .setColumnAeData(fillAE(row))
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
                        .setColumnApData(fio)
                        .setTechFullFio(fio)
                        .build();
                data.add(dataLine);
                ++counterCopy;
            }
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
                .setMessage(DONE + warnings.stream().distinct().collect(Collectors.joining(EMPTY)))
                .setBookName(getConverterName() + UNDERSCORE)
                .build();
    }

    private String fillS(int row, boolean isStart) throws ParseException {
        if (isStart) {
            return convertDateFormat(stockIn, TEMPLATE_DATE_TIME_DOT);
        }
        val date = convertDateFormat(stockIn, TEMPLATE_DATE_DOT);
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        if (lentaDictionary == null) {
            return convertDateFormat(date, TEMPLATE_DATE_DOT, TEMPLATE_DATE_DOT) + " 10:00";
        }
        val timeShop = convertDateFormat(lentaDictionary.getTimeShop(), TEMPLATE_TIME);
        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME);
        val dateTimeS = timeShop.after(timeStock) ? DateUtils.addDays(stockIn, -1) : stockIn;
        //ЕСЛИ РАСЧИТАННОЕ ВРЕМЯ УБЫТИЯ меньше чем дата прибытия на погрузку, то плюсуем 1 день и в S и в T
        val dateTimeT = calculateDateTimeT(row, lentaDictionary);
        val result = dateTimeT.before(stockIn) ? DateUtils.addDays(dateTimeS, 1) : dateTimeS;
        //Если второе время меньше первого, то у первого времени отнимать 1 день
        val dateTimeResultT = convertDateFormat(fillT(row, isStart), TEMPLATE_DATE_TIME_DOT);
        val answer = dateTimeResultT.before(result) ? DateUtils.addDays(result, -1) : result;

        return convertDateFormat(answer, TEMPLATE_DATE_TIME_DOT);
    }

    private String fillT(int row, boolean isStart) throws ParseException {
        if (isStart) {
            val result = DateUtils.addHours(stockIn, 3);
            return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
        }
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        if (lentaDictionary == null) {
            return convertDateFormat(stockIn, TEMPLATE_DATE_DOT) + " 22:00";
        }
        //ЕСЛИ РАСЧИТАННОЕ ВРЕМЯ УБЫТИЯ меньше чем дата прибытия на погрузку, то плюсуем 1 день и в S и в T
        val dateTimeT = calculateDateTimeT(row, lentaDictionary);
        val result = dateTimeT.before(stockIn) ? DateUtils.addDays(dateTimeT, 1) : dateTimeT;
        return convertDateFormat(result, TEMPLATE_DATE_TIME_DOT);
    }

    private Date calculateDateTimeT(int row, LentaDictionary lentaDictionary) throws ParseException {
        val timeStock = convertDateFormat(lentaDictionary.getTimeStock(), TEMPLATE_TIME, TEMPLATE_TIME);
        val dateResultString = convertDateFormat(stockIn, TEMPLATE_DATE_DOT);
        val dateTimeResult = dateResultString + SPACE + (isLentaCarAndTemperate(row) ? "18:00" : timeStock);
        return convertDateFormat(dateTimeResult, TEMPLATE_DATE_TIME_DOT);
    }

    private boolean isLentaCarAndTemperate(int row) {
        val carName = getCarName(row);
        val car = dictionaryService.getCarOrElse(carName, null);
        return carName.length() > 5 && car != null && car.getTemperatureMin() <= -18;
    }

    private String getCarNumber(int row) {
        return getCellValue(row, 5).replaceAll(SPACE, EMPTY);
    }

    private String fillD(int row) {
        val companyName = getCellValue(row, 8);
        val carNumber = getCarNumber(row);
        if (companyName.toUpperCase().contains("ЛЕНТА")
                && dictionaryService.getTsCityBrief(carNumber) == null
                && (dictionaryService.getCarNumberOrElse(carNumber, null) == null)) {
            return COMPANY_OOO_LENTA_HIRING;
        }
        return companyName;
    }

    private String fillL(int row) {
        val carName = getCarName(row);
        val car = dictionaryService.getCarOrElse(carName, null);
        if (carName.length() < 5 || car == null) {
            return "2";
        }
        return String.valueOf(car.getTemperatureMin());
    }

    private String fillM(int row) {
        val carName = getCarName(row);
        val car = dictionaryService.getCarOrElse(carName, null);
        if (carName.length() < 5 || car == null) {
            return "6";
        }
        return String.valueOf(car.getTemperatureMax());
    }

    private String fillJ(int row) {
        val carNumber = getCellValue(row, 5).replaceAll(SPACE, EMPTY);
        val lentaCar = dictionaryService.getLentaCarOrElse(carNumber, null);
        if (lentaCar != null) {
            return String.valueOf(lentaCar.getTonnage());
        }
        val carName = getCarName(row);
        if (carName.length() < 5) {
            val doubleValue = Double.parseDouble(carName.replaceAll(",", ".")) * 1000;
            return String.valueOf((int) doubleValue);
        }
        val car = dictionaryService.getCar(carName);
        return String.valueOf(car.getTonnage());
    }

    private String fillC(int row) {
        /*
        В базе ищем по номеру машины. Если нашли, берем бриф - название компании
        Если в базе не нашли,берем код из первого столбца, ищем в справочнике.
        Если нашли, то Лента + название из справочника. Если нет, то "Нет региона"
         */
        val tsCityBrief = dictionaryService.getTsCityBrief(getCarNumber(row));
        if (tsCityBrief != null) {
            return tsCityBrief;
        }
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        return lentaDictionary == null ? "Нет региона" : "Лента (" + lentaDictionary.getRegion() + ")";
    }

    private String fillAE(int row) {
        return WordUtils.capitalize(getCellValue(row, 2).toLowerCase());
    }


    private Date getExpectedTimeIncome(int row) throws ParseException {
        val date = getCellValue(row, 9);
        return convertDateFormat(date, date.contains(".") ? TEMPLATE_DATE_TIME_DOT : TEMPLATE_DATE_TIME_SLASH);
    }

    private String getCarName(int row) {
        return getCellValue(row, 3).replaceAll("\\\\", EMPTY);
    }

    private Long getCode(int row) {
        val cellValue = getCellValue(row, 0);
        val indexDash = cellValue.indexOf(MINUS);
        val longValue = indexDash != -1 ? cellValue.substring(0, indexDash) : cellValue;
        return Long.parseLong(longValue);
    }

    private String fillU(int row) {
        val code = getCode(row);
        val lentaDictionary = dictionaryService.getDictionary(code.longValue());
        return lentaDictionary == null ? "Код адреса не найден в справочнике: " + code : lentaDictionary.getAddressName();
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

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 1);
        val prev1 = getValueOrDefault(row, -1, 1);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(EMPTY));
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getLenta();
    }
}
