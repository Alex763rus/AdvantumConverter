package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ConvertService {

    /**
     * @return название конвертера
     * Используется:
     * - в главном меню конвертеров
     * - при создании задач на сопровождение
     * - в названии сформированных файлов
     */
    String getConverterName();

    /**
     * @return ссылка конвертера
     */
    String getConverterCommand();

    /**
     * @param book - вложенный в сообщение входящий excel документ
     * @return ConvertedBook - сформированный документ
     */
    ConvertedBook getConvertedBook(XSSFWorkbook book);


    /**
     * @return тип возвращаемого эксель файла из перечисления ExcelType
     */
    ExcelType getExcelType();
}
