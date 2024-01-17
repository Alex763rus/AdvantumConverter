package com.example.advantumconverter.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {

    public static String APP_NAME = "advantumconverter";

    @NoArgsConstructor(access = PRIVATE)
    public final class Converter {

        public static final String REFRIGERATOR = "Рефрижератор";
        public static final String BUSH_AUTOPROM_ORGANIZATION_NAME = "ООО \"Буш-Автопром\"";
        public static String SHEET_RESULT_NAME = "ИМПОРТ";
        public static String LOAD_THE_GOODS = "Погрузка";
        public static String UNLOAD_THE_GOODS = "Разгрузка";
        public static String COMPANY_OOO_LENTA = "ООО ''ЛЕНТА''";
        public static String COMPANY_OOO_LENTA_HIRING = COMPANY_OOO_LENTA + " найм";

    }

    @NoArgsConstructor(access = PRIVATE)
    public final class FileOutputName {
        public static final String FILE_NAME_LENTA = "Лента";
        public static final String FILE_NAME_OZON = "Озон";
        public static final String FILE_NAME_COFIX = "Рулог Кофикс";
        public static final String FILE_NAME_AGROPROM = "Х5 Агропром Торг";
        public static final String FILE_NAME_AGROPROM_DETAIL = "Х5 Агропром Торг (Детал)";
        public static final String FILE_NAME_BOGORODSK = "Х5 Богородск";
        public static final String FILE_NAME_SAMOKAT = "Самокат";
        public static final String FILE_NAME_DOMINOS = "Рулог Доминос";
    }

    @NoArgsConstructor(access = PRIVATE)
    public final class Company {
        public static final String COMPANY_NOT_FOUND = "Компания отсутствует";
        public static final String COMPANY_ADVANTUM = "Адвантум";
        public static final String COMPANY_NAME_LENTA = "Лента";
        public static final String COMPANY_NAME_BUSH_AVTOPROM = "Буш-Автопром";
        public static final String COMPANY_NAME_OZON = "Озон";
    }

    @NoArgsConstructor(access = PRIVATE)
    public final class Command {
        public static final String COMMAND_DEFAULT = "/default";
        public static final String COMMAND_START = "/start";
        public static final String COMMAND_FAQ = "/faq";
        public static final String COMMAND_HISTORIC_ACTION = "/historic_action";
        public static final String COMMAND_CONVERT_BOGORODSK = "/convert_bogorodsk";
        public static final String COMMAND_CONVERT_COFIX = "/convert_cofix";
        public static final String COMMAND_CONVERT_LENTA = "/convert_lenta";
        public static final String COMMAND_CONVERT_OZON = "/convert_ozon";
        public static final String COMMAND_CONVERT_SAMOKAT = "/convert_samokat";
        public static final String COMMAND_CONVERT_DOMINOS = "/convert_dominos";
        public static final String COMMAND_CONVERT_AGROPROM = "/convert_agroprom";
        public static final String COMMAND_CONVERT_AGROPROM_DETAIL = "/convert_agroprom_detail";
        public static final String COMMAND_SHOW_OPEN_TASK = "/show_open_task";
        public static final String COMMAND_SHOW_MY_TASK = "/show_my_task";
        public static final String COMMAND_RELOAD_DICTIONARY = "/reload_dictionary";
        public static final String COMMAND_SETTING_NEW_USER = "/setting_user";
    }

    @NoArgsConstructor(access = PRIVATE)
    public final class Exception {
        public static final String CAR_NOT_FOUND = "Машина не найдена в настройках: ";

        public static final String CONVERT_PROCESSING_ERROR = "Ошибка обработки файла: ";

        public static final String EXCEL_GENERATION_ERROR = "Ошибка формирования файла: ";
    }

}
