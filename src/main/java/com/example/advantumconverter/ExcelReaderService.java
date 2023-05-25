package com.example.advantumconverter;

import jakarta.annotation.PostConstruct;
import lombok.val;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

@Service
public class ExcelReaderService {

    String path = "C:\\Users\\grigorevap\\Desktop\\excel\\cofix.xlsx";

    @PostConstruct
    public void readFromExcel() throws Exception {
        try {
            val file = new File(path);
            val book = (XSSFWorkbook) WorkbookFactory.create(file);

            XSSFSheet sheet = book.getSheet("Исходник");

            Iterator<Row> ri = sheet.rowIterator();
            while (ri.hasNext()) {
                XSSFRow row = (XSSFRow) ri.next();
                Iterator<Cell> ci = row.cellIterator();
                while (ci.hasNext()) {
                    XSSFCell cell = (XSSFCell) ci.next();
                    System.out.println(cell);
                }
            }
            int i = 0;
//      InputStream is = new FileInputStream(FILE);
//      book = (XSSFWorkbook) WorkbookFactory.create(is);
//      is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
