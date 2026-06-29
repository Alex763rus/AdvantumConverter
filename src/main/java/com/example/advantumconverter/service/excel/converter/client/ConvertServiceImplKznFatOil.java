package com.example.advantumconverter.service.excel.converter.client;

import com.example.advantumconverter.aspect.LogExecutionTime;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.exception.ConvertProcessingException;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataV2;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.ConvertServiceBase;
import lombok.val;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.Command.COMMAND_CONVERT_KZN_FAT_OIL;
import static com.example.advantumconverter.constant.Constant.Company.COMPANY_NAME_KZN_FAT_OIL;
import static com.example.advantumconverter.constant.Constant.Converter.*;
import static com.example.advantumconverter.constant.Constant.Exceptions.EXCEL_LINE_CONVERT_ERROR;
import static com.example.advantumconverter.constant.Constant.FileOutputName.FILE_NAME_KZN_FAT_OIL;
import static com.example.advantumconverter.enums.ExcelType.CLIENT;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;
import static org.example.tgcommons.constant.Constant.TextConstants.SPACE;
import static org.example.tgcommons.utils.DateConverterUtils.*;

@Component
public class ConvertServiceImplKznFatOil extends ConvertServiceBase implements ConvertService {
    private int START_ROW;

    private int LAST_ROW;

    @Override
    public ExcelType getExcelType() {
        return CLIENT;
    }

    @Override
    public String getConverterName() {
        return FILE_NAME_KZN_FAT_OIL;
    }

    @Override
    public String getConverterCommand() {
        return COMMAND_CONVERT_KZN_FAT_OIL;
    }

    private List<String> warnings = new ArrayList<>();

    @Override
    public boolean isV2() {
        return true;
    }

    @Override
    @LogExecutionTime(value = "Конвертация v2 " + COMPANY_NAME_KZN_FAT_OIL, unit = LogExecutionTime.TimeUnit.SECONDS)
    public ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        warnings = new ArrayList<>();
        val data = new ArrayList<ConvertedListDataV2>();
        ConvertedListDataClientsV2 dataLine = null;

        START_ROW = 1;
        int row = START_ROW;
        boolean isStart = true;
        try {
            sheet = book.getSheetAt(0);
            int counterCopy = 0;
            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                isStart = isStart(row);
                if (isStart) {
                    counterCopy = 0;
                }
                var address = getCellValue(row, 5);
                dataLine = ConvertedListDataClientsV2.init()
                        .setColumnAdata(getCellValue(row, 0))
                        .setColumnBdata(convertDateFormat(getCellValue(row, 1), TEMPLATE_DATE_DOT))
                        .setColumnCdata(address)
                        .setColumnDdata(address)
                        .setColumnEdata(null)
                        .setColumnFdata(REFRIGERATOR)
                        .setColumnGdata(EMPTY)
                        .setColumnHdata(EMPTY)
                        .setColumnIdata(null)
                        .setColumnJdata(5)
                        .setColumnKdata(12)
                        .setColumnLdata(null)
                        .setColumnMdata(null)
                        .setColumnNdata(null)
                        .setColumnOdata(null)
                        .setColumnPdata(null)
                        .setColumnQdata(null)
                        .setColumnRdata(null)
                        .setColumnSdata(fillS(row, isStart))
                        .setColumnTdata(fillT(row))
                        .setColumnUdata(getCellValue(row, 12))
                        .setColumnVdata(getCellValue(row, 14))
                        .setColumnWdata(isStart ? LOAD_THE_GOODS : UNLOAD_THE_GOODS)
                        .setColumnXdata(counterCopy)
                        .setColumnYdata(null)
                        .setColumnZdata(null)
                        .setColumnAaData(EMPTY)
                        .setColumnAbData(EMPTY)
                        .setColumnAcData(getCarNumber(row, 15) + "716")
                        .setColumnAdData(getCarNumber(row, 17) + "16")
                        .setColumnAeData(getCellValue(row, 16))
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
                        .setColumnApData(EMPTY)
                        .setTechFullFio(EMPTY)
                        .build();
                data.add(dataLine);
                ++counterCopy;
            }
        } catch (Exception e) {
            throw new ConvertProcessingException(String.format(EXCEL_LINE_CONVERT_ERROR, row, dataLine, e.getMessage()));
        }
        return createDefaultBookV2(data, warnings, getConverterName());
    }


    private Date fillS(int row, boolean isStart) throws ParseException {
        if (isStart) {
            var dateT = fillT(row);
            return DateUtils.addHours(dateT, -4);
        }
        return convertDateFormat(getCellValue(row, 8), TEMPLATE_DATE_TIME_SLASH);
    }

    private Date fillT(int row) throws ParseException {
        return convertDateFormat(getCellValue(row, 9), TEMPLATE_DATE_TIME_SLASH);
    }


    private String getCarNumber(int row, int col) {
        return getCellValue(row, col).replaceAll(SPACE, EMPTY);
    }

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 0);
        val prev1 = getValueOrDefault(row, -1, 0);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(EMPTY));
    }

    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > LAST_ROW) {
            return EMPTY;
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
            return EMPTY;
        }
        return getCellValue(sheet, row, col);
    }
}
