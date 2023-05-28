package com.example.advantumconverter.service.excel;

import com.example.advantumconverter.model.security.User;
import lombok.val;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.SHEET_RESULT_NAME;
import static com.example.advantumconverter.enums.State.FREE;
import static com.example.advantumconverter.utils.DateConverter.TEMPLATE_DATE;
import static com.example.advantumconverter.utils.DateConverter.convertDateFormat;


@Component
public class ConvertServiceBase {

    protected XSSFSheet sheet;

    protected int LAST_COLUMN_NUMBER;

    protected String getCellValue(int row, int col) {
        if (sheet.getRow(row) == null) {
            return "";
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }

    protected Date getCellDate(int row, int col) {
        if (sheet.getRow(row) == null) {
            return null;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return null;
        }
        return sheet.getRow(row).getCell(col).getDateCellValue();
    }

    protected int getLastRow(int startRow) {
        int i = startRow;
        for (; ; i++) {
            if (getCellValue(i, 0).equals("")) {
                if (getCellValue(i + 1, 0).equals("")) {
                    break;
                }
            }
        }
        return i - 1;
    }

    protected String getCurrentDate(String format) throws ParseException {
        return convertDateFormat(new Date(), format);
    }
}
