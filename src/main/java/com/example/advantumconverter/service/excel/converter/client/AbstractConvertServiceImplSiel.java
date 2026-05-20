package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.SberAddressNotFoundException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_SIEL;
import static com.example.advantumconverter.constant.Constant.Converter.LOAD_THE_GOODS;
import static com.example.advantumconverter.constant.Constant.Converter.UNLOAD_THE_GOODS;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;
import static org.springdoc.core.utils.Constants.DOT;

public abstract class AbstractConvertServiceImplSiel extends ConvertServiceBase implements ConvertService {

    public static final String SIEL_COMPANY_NAME = "ООО СИЭЛЬ";
    private final String AREA_NAME = "Склад РЦ Тула Хлеб";

    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private List<String> warnings = new ArrayList<>();

    public AbstractConvertServiceImplSiel() {
        super();
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_SIEL, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;

        int row = getStartRow();
        boolean isStart = true;
        int numberUnloading = 0;
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(getStartRow(), 0);
            if (getCellValue(LAST_ROW, 0).equalsIgnoreCase("итого")) {
                LAST_ROW = LAST_ROW - 1; //-1 делается только в этом конвертере потому что они заканчивают строчкой "ИТОГО"
            }
            LAST_COLUMN_NUMBER = sheet.getRow(getStartRow()).getLastCellNum();

            String reisNumber = EMPTY;
            String carNumber = EMPTY;
            val dateFromFile = convertDateFormat(getCellValue(row, 1), TEMPLATE_DATE_DOT);
            var dateFromFileString = convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT);
            var dateFromFileNumberOnly = dateFromFileString.replace(DOT, EMPTY);
            int reisCounter = 0;
            String uniqReisCounter = EMPTY;
            for (; row <= LAST_ROW; ++row) {
                var reisNumberFromFile = getReisNumber(row);
                isStart = !reisNumber.equals(reisNumberFromFile);
                reisNumber = reisNumberFromFile;
                if (isStart) {
                    carNumber = getCarNumber(row);
                    numberUnloading = 0;
                    ++reisCounter;
                    uniqReisCounter = dateFromFileNumberOnly + reisCounter;
                } //
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = ConvertedListDataClientsV2.init()
                            .setColumnAdata(uniqReisCounter)
                            .setColumnBdata(dateFromFile)
                            .setColumnCdata(getColumnCdata())
                            .setColumnDdata(dictionaryService.getSielCarrierName(carNumber))
                            .setColumnEdata(null)
                            .setColumnFdata(getColumnFdata())
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(getColumnJdata())
                            .setColumnKdata(getColumnKdata())
                            .setColumnLdata(getColumnLdata())
                            .setColumnMdata(getColumnMdata())
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(fillS(isStart, row, dateFromFileString))
                            .setColumnTdata(fillT(isStart, row, dateFromFileString))
                            .setColumnUdata(fillU(isStart, row))
                            .setColumnVdata(fillV(isStart, row))
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(numberUnloading)
                            .setColumnYdata(null)
                            .setColumnZdata(null)
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(carNumber)
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(getCellValue(row, 3))
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
                            .setTechFullFio(null)
                            .build();
                    data.add(dataLine);

                    ++numberUnloading;
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
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    private String getReisNumber(int row) {
        return getCellValue(row, 0);
    }

    private String getCarNumber(int row) {
        return getCellValue(row, 2).replaceAll(REGEX_NUMBER_AND_SIMBOL, EMPTY);
    }

    private Date fillS(boolean isStart, int row, String dateFromFileString) throws ParseException {
        return convertDateFormat(dateFromFileString + SPACE + getSTime(isStart, row), TEMPLATE_DATE_TIME_DOT);
    }

    protected abstract Integer getStartRow();

    protected abstract String getColumnCdata();

    protected abstract String getColumnFdata();

    protected abstract Integer getColumnJdata();

    protected abstract Integer getColumnKdata();

    protected abstract Integer getColumnLdata();

    protected abstract Integer getColumnMdata();

    protected abstract String getSTime(boolean isStart, int row);

    protected abstract String getTTime(boolean isStart, int row);

    private Date fillT(boolean isStart, int row, String dateFromFileString) throws ParseException {
        return convertDateFormat(dateFromFileString + SPACE + getTTime(isStart, row), TEMPLATE_DATE_TIME_DOT);
    }

    protected String getPointName(int row) {
        return getCellValue(row, 5);
    }

    private String getPointAddress(int row) {
        return getCellValue(row, 6);
    }

    private String fillU(boolean isStart, int row) {
        return isStart ? AREA_NAME : getPointName(row);
    }

    private String fillV(boolean isStart, int row) {
        return isStart ? AREA_NAME : getPointAddress(row);
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
