package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.exception.SberAddressNotFoundException;
import com.example.advantumconverter.exception.TemperatureNodValidException;
import com.example.advantumconverter.model.dictionary.excel.Header;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_SIEL;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_SIEL;
import static com.example.advantumconverter.constant.Constant.Heap.*;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplSiel extends ConvertServiceBase implements ConvertService {

    private final int START_ROW = 6;
    private final CrmConfigProperties crmConfigProperties;
    private final String COMPANY_NAME = "ООО СИЭЛЬ";
    private final String S_TIME_START_2H = "2:00:00";
    private final String S_TIME_END_9H = "9:00:00";
    private final String T_TIME_START_7H = "7:00:00";
    private final String T_TIME_END_14H = "14:00:00";
    private final String AREA_NAME = "Склад РЦ Тула Хлеб";
    private int LAST_ROW;
    private int LAST_COLUMN_NUMBER;
    private List<String> warnings = new ArrayList<>();

    public ConvertServiceImplSiel(CrmConfigProperties crmConfigProperties) {
        super();
        this.crmConfigProperties = crmConfigProperties;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_SIEL;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_SIEL;
    }

    @Override
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataV2 dataLine = null;

        int row = START_ROW;
        boolean isStart = true;
        int numberUnloading = 0;

        try {
            sheet = book.getSheetAt(0);
            LAST_ROW = getLastRow(START_ROW, 0);
            if (getCellValue(LAST_ROW, 0).equalsIgnoreCase("итого")) {
                LAST_ROW = LAST_ROW - 1; //-1 делается только в этом конвертере потому что они заканчивают строчкой "ИТОГО"
            }
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();

            String reisNumber = EMPTY;
            String carNumber = EMPTY;
            val dateFromFile = convertDateFormat(getCellValue(row, 3), TEMPLATE_DATE_DOT);
            var dateFromFileString = convertDateFormat(dateFromFile, TEMPLATE_DATE_DOT);
            for (; row <= LAST_ROW; ++row) {
//В перевозчика из вкладки вечикалс в сводный по номеру машины.
                isStart = !reisNumber.equals(getReisNumber(row));
                reisNumber = getReisNumber(row);
                if (isStart) {
                    carNumber = getCarNumber(row);
                    numberUnloading = 0;
                }
                for (int iRepeat = 0; iRepeat < 2; ++iRepeat) {
                    dataLine = ConvertedListDataV2.init()
                            .setColumnAdata(reisNumber)
                            .setColumnBdata(dateFromFile)
                            .setColumnCdata(COMPANY_NAME)
                            .setColumnDdata(dictionaryService.getSielCarrierName(carNumber))
                            .setColumnEdata(null)
                            .setColumnFdata(REFRIGERATOR)
                            .setColumnGdata(EMPTY)
                            .setColumnHdata(EMPTY)
                            .setColumnIdata(null)
                            .setColumnJdata(5000)
                            .setColumnKdata(12)
                            .setColumnLdata(2)
                            .setColumnMdata(4)
                            .setColumnNdata(2)
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
                            .setColumnAeData(getCellValue(row, 6))
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

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    private String getReisNumber(int row) {
        return getCellValue(row, 2);
    }

    private String getCarNumber(int row) {
        return getCellValue(row, 5).replaceAll(" ", EMPTY).replaceAll(SPACE, EMPTY);
    }

    private Date fillS(boolean isStart, int row, String dateFromFileString) throws ParseException {
        return convertDateFormat(dateFromFileString + SPACE + getSTime(isStart, row), TEMPLATE_DATE_TIME_DOT);
    }

    private String getSTime(boolean isStart, int row) {
        if (isStart) {
            return S_TIME_START_2H;
        }
        var sielPoint = dictionaryService.getSielPoint(getPointName(row));
        if (sielPoint == null) {
            return S_TIME_END_9H;
        }
        return sielPoint.getTimeStart();
    }

    private String getTTime(boolean isStart, int row) {
        if (isStart) {
            return T_TIME_START_7H;
        }
        var sielPoint = dictionaryService.getSielPoint(getPointName(row));
        if (sielPoint == null) {
            return T_TIME_END_14H;
        }
        return sielPoint.getTimeEnd();
    }

    private Date fillT(boolean isStart, int row, String dateFromFileString) throws ParseException {
        return convertDateFormat(dateFromFileString + SPACE + getTTime(isStart, row), TEMPLATE_DATE_TIME_DOT);
    }


    @Override
    public CrmConfigProperties.CrmCreds getCrmCreds() {
        return crmConfigProperties.getSiel();
    }

    private String getPointName(int row) {
        return getCellValue(row, 8);
    }

    private String getPointAddress(int row) {
        return getCellValue(row, 9);
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
