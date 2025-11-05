package com.example.advantumconverter.service.excel.converter;

import com.example.advantumconverter.config.properties.ConverterProperties;
import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
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
    @Deprecated
    default ConvertedBook getConvertedBook(XSSFWorkbook book) {
        return null;
    }

    /**
     * @param book - вложенный в сообщение входящий excel документ
     * @return ConvertedBook - сформированный документ
     * обновленный DTO
     */
    default ConvertedBookV2 getConvertedBookV2(XSSFWorkbook book) {
        return null;
    }

    /**
     * @return тип возвращаемого эксель файла из перечисления ExcelType
     */
    ExcelType getExcelType();

    /**
     * @return логин пароль для получения токена в CRM
     */
    default CrmConfigProperties.CrmCreds getCrmCreds() {
        return null;
    }

    /**
     * @return признак, что конвертер второй версии апи.
     * версия v1 является @Deprecated, в web интерфейсе поддерживаться не будет, только в ТГ.
     */
    default boolean isV2() {
        return false;
    }

    /**
     * @return настройки конвертера
     */
    default ConverterProperties.ConverterSettings converterSettings() {
        return null;
    }
}
