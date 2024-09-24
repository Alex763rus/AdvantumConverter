package utils;

import org.apache.commons.codec.Resources;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class ExcelReader {



    public static XSSFWorkbook read(String filename) throws IOException {
        return new XSSFWorkbook(Resources.getInputStream(filename));
    }
}
