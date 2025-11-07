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
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_NIKA;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_NIKA;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_NIKA;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplNika extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 1;
    private int LAST_ROW;
    private final CrmConfigProperties crmConfigProperties;
    //    private static final String BAZA_ADDRESS = "603058, Нижегородская обл, Нижний Новгород, улица Героя Попова ул, дом № 43в";
    private List<String> warnings = new ArrayList<>();

    public ConvertServiceImplNika(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_NIKA;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_NIKA;
    }

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_NIKA, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;

        int row = START_ROW;
        boolean isStart = true;
        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW, 1);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            var currentDate = convertDateFormat(new Date(), TEMPLATE_DATE);
            var dateFromFile = convertDateFormat(getCellValue(row, 6), TEMPLATE_DATE_SLASH);
            var dateFromFileString = convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT);
            var numberUnloading = 0;
            String comment = EMPTY;
            for (; row <= LAST_ROW; ++row) {
                isStart = isStart(row, currentDate);
                if (isStart) {
                    numberUnloading = 0;
                    comment = getCellValue(row, 0);
                }
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = ConvertedListDataClientsV2.init()
                            .setColumnAdata(getUniqNumber(row, currentDate))
                            .setColumnBdata(dateFromFile)
                            .setColumnCdata(COMPANY_NAME_NIKA)
                            .setColumnDdata(COMPANY_NAME_NIKA)
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(getIntegerValue(row, 13, 0) * 1000)
                            .setColumnKdata(getIntegerValue(row, 14, 0))
                            .setColumnLdata(2)
                            .setColumnMdata(4)
                            .setColumnNdata(null)
                            .setColumnOdata(null)
                            .setColumnPdata(null)
                            .setColumnQdata(null)
                            .setColumnRdata(null)
                            .setColumnSdata(fillS(isStart, row, dateFromFileString))
                            .setColumnTdata(fillT(isStart, row, dateFromFileString))
                            .setColumnUdata(getCellValue(row, 3))
                            .setColumnVdata(getCellValue(row, 4))
                            .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                            .setColumnXdata(isStart ? 0 : numberUnloading)
                            .setColumnYdata(fillY(row, isStart))
                            .setColumnZdata(fillZ(row, isStart))
                            .setColumnAaData(EMPTY)
                            .setColumnAbData(EMPTY)
                            .setColumnAcData(getCellValue(row, 19))
                            .setColumnAdData(EMPTY)
                            .setColumnAeData(getCellValue(row, 20))
                            .setColumnAfData(null)
                            .setColumnAgData(EMPTY)
                            .setColumnAhData(EMPTY)
                            .setColumnAiData(null)
                            .setColumnAjData(EMPTY)
                            .setColumnAkData(EMPTY)
                            .setColumnAlData(EMPTY)
                            .setColumnAmData(EMPTY)
                            .setColumnAnData(EMPTY)
                            .setColumnAoData(comment)
                            .setColumnApData(null)
                            .setTechFullFio(null)
                            .build();
                    data.add(dataLine);
                    if (!isStart) {
                        break;
                    }
                    isStart = false;
                    ++numberUnloading;
                }

            }
        } catch (SberAddressNotFoundException | TemperatureNodValidException e) {
            throw e;
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }

    private Double fillZ(int row, boolean isStart) {
        String coordinate =  getCellValue(row, isStart ? 5 : 9);
        var spaceIndex = coordinate.indexOf(SPACE);
        if(spaceIndex == -1){
            return 0.0;
        }
        return Double.parseDouble(coordinate.substring(spaceIndex, coordinate.length()-1));
    }

    private Double fillY(int row, boolean isStart) {
        String coordinate =  getCellValue(row, isStart ? 5 : 9);
        var spaceIndex = coordinate.indexOf(SPACE);
        if(spaceIndex == -1){
            return 0.0;
        }
        return Double.parseDouble(coordinate.substring(0, spaceIndex));
    }

    private String getUniqNumber(int row, String currentDate) {
        return currentDate + MINUS + getCellValue(row, 19) + MINUS + getCellValue(row, 2);
    }

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @SneakyThrows
    private Date fillS(boolean isStart, int row, String dateFromFile) {
        String dateTime = isStart ? dateFromFile + SPACE + getCellValue(row, 7)
                : convertDateFormat(convertDateFormat(getCellValue(row, 10), TEMPLATE_DATE_SLASH), TEMPLATE_DATE_DOT)
                + SPACE + getCellValue(row, 11);
        return convertDateFormat(dateTime, TEMPLATE_DATE_TIME_DOT);
    }

    @SneakyThrows
    private Date fillT(boolean isStart, int row, String dateFromFile) {
        var dateS = fillS(isStart, row, dateFromFile);
        return DateUtils.addMinutes(DateUtils.addHours(dateS, 1), 50);
    }

    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getNika();
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

    private boolean isStart(int row, String currentDate) throws ParseException {
        if (row == START_ROW) {
            return true;
        }
        val cur = getUniqNumber(row, currentDate);
        val prev1 = getUniqNumber(row - 1, currentDate);
        return !(cur.equals(prev1) || prev1.equals(EMPTY));
    }
}
