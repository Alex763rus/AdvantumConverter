package com.example.advantumconverter.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constant {

    public static final String APP_NAME = "advantumconverter";

    @NoArgsConstructor(access = PRIVATE)
    public static final class Converter {

        public static final String REFRIGERATOR = "Рефрижератор";
        public static final String BUSH_AUTOPROM_ORGANIZATION_NAME = "ООО \"Буш-Автопром\"";
        public static final String SBER_BUSH_AUTOPROM_ORGANIZATION_NAME_1 = "ООО Буш-Автопром";
        public static final String SBER_BUSH_AUTOPROM_ORGANIZATION_NAME_2 = "ООО \"Буш-Автопром";
        public static final String SBER_SQUIRREL_ORGANIZATION_NAME = "ООО \"Белка Драйв\"";
        public static final String SHEET_RESULT_NAME = "ИМПОРТ";
        public static final String LOAD_THE_GOODS = "Погрузка";
        public static final String DC_NOGINSK = "DC Noginsk";
        public static final String RC_NOGINSK = "РЦ НОГИНСК";
        public static final String UNLOAD_THE_GOODS = "Разгрузка";
        public static final String COMPANY_OOO_LENTA_HIRING = "ООО ''ЛЕНТА'' найм";
        public static final String COMPANY_OZON_FRESH = "OZON FRESH";
        public static final String COMPANY_METRO = "Метро Групп Логистик";
        public static final String COMPANY_DEAL_AUTO_TRANS = "ООО ДиалАвтоТранс";
        public static final String COMPANY_DEAL_AUTO_TRANS_INN = "6324744734";
        public static final String COMPANY_DEAL_AUTO_TRANS_INCORRECT = "ООО \"Диалавтотранс\"";
        public static final String LEFT_FOR_A_FLIGHT = "Уехал";
        public static final String CONVERT_FILE_DESCRIPTION_TEMPLATE = "Сконвертировать файл %s";
    }

    //TODO вынести в библиотеку:
    @NoArgsConstructor(access = PRIVATE)
    public static final class Heap {
        public static final String MINUS = "-";
        public static final String TWO_SPACE = "  ";
        public static final String UNDERSCORE = "_";
        public static final String DONE = "Готово!";
        public static final String EXPORT = "Экспорт";

    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class FileOutputName {
        public static final String FILE_NAME_LENTA = "Лента";
        public static final String FILE_NAME_METRO = "METRO";
        public static final String FILE_NAME_OZON = "Озон";
        public static final String FILE_NAME_SIEL = "Сиэль";
        public static final String FILE_NAME_COFIX = "Рулог Кофикс";
        public static final String FILE_NAME_AGROPROM = "Х5 Агропром Торг";
        public static final String FILE_NAME_SBER = "СберЛогистика";
        public static final String FILE_NAME_ART_FRUIT = "Артфрут";
        public static final String FILE_NAME_BOOKER = "Бухгалтерия";
        public static final String FILE_NAME_AGROPROM_DETAIL = "Х5 Агропром Торг (Детал)";
        public static final String FILE_NAME_BOGORODSK = "Х5 Богородск";
        public static final String FILE_NAME_SAMOKAT = "Самокат";
        public static final String FILE_NAME_DOMINOS = "Рулог Доминос";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Company {
        public static final String COMPANY_NOT_FOUND = "Компания отсутствует";
        public static final String COMPANY_ADVANTUM = "Адвантум";
        public static final String COMPANY_NAME_LENTA = "Лента";
        public static final String COMPANY_NAME_BUSH_AVTOPROM = "Буш-Автопром";
        public static final String COMPANY_NAME_OZON = "Озон";
        public static final String COMPANY_NAME_METRO = "Метро";
        public static final String COMPANY_NAME_SBER = "СберЛогистика";
        public static final String COMPANY_NAME_ART_FRUIT = "ArtFruit";
        public static final String COMPANY_NAME_SIEL = "СИЭЛЬ";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class ExcelType {
        public static final String CLIENT = "Client";
        public static final String BOOKER = "Booker";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class BookerListName {
        public static final String BOOKER_X5 = "X5";
        public static final String BOOKER_OZON = "Ozon";
        public static final String BOOKER_ASHAN = "Ashan";
        public static final String BOOKER_AV = "av";
        public static final String BOOKER_METRO = "metro";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Command {
        public static final String COMMAND_DEFAULT = "/default";
        public static final String COMMAND_START = "/start";
        public static final String COMMAND_FAQ = "/faq";
        public static final String COMMAND_HISTORIC_ACTION = "/historic_action";
        public static final String COMMAND_CONVERT_BOGORODSK = "/convert_bogorodsk";
        public static final String COMMAND_CONVERT_COFIX = "/convert_cofix";
        public static final String COMMAND_CONVERT_LENTA = "/convert_lenta";
        public static final String COMMAND_CONVERT_OZON = "/convert_ozon";
        public static final String COMMAND_CONVERT_SIEL = "/convert_siel";
        public static final String COMMAND_CONVERT_METRO = "/convert_metro";
        public static final String COMMAND_CONVERT_SAMOKAT = "/convert_samokat";
        public static final String COMMAND_CONVERT_DOMINOS = "/convert_dominos";
        public static final String COMMAND_CONVERT_AGROPROM = "/convert_agroprom";
        public static final String COMMAND_CONVERT_SBER = "/convert_sber";
        public static final String COMMAND_CONVERT_ART_FRUIT = "/convert_art_fruit";
        public static final String COMMAND_CONVERT_BOOKER = "/convert_booker";
        public static final String COMMAND_CONVERT_AGROPROM_DETAIL = "/convert_agroprom_detail";
        public static final String COMMAND_SHOW_OPEN_TASK = "/show_open_task";
        public static final String COMMAND_SHOW_MY_TASK = "/show_my_task";
        public static final String COMMAND_RELOAD_DICTIONARY = "/reload_dictionary";
        public static final String COMMAND_SETTING_NEW_USER = "/setting_user";
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Exceptions {
        public static final String CAR_NOT_FOUND = "Машина не найдена в настройках: ";

        public static final String CONVERT_PROCESSING_ERROR = "Ошибка обработки файла: ";

        public static final String SBER_ADDRESS_NOT_FOUND = "Ошибка обработки файла: заказчик не найден";

        public static final String TEMPERATURE_NOT_VALID = "Oшибка данных в столбце Температурный режим, строка: ";

        public static final String DICTIONARY_ERROR = "Ошибка при обращении к справочнику: ";

        public static final String EXCEL_GENERATION_ERROR = "Ошибка формирования файла: ";

        public static final String EXCEL_LIST_NOT_FOUND_WARNING = "Не найден лист: %s";

        public static final String EXCEL_LIST_CONVERT_ERROR =
                "не удалось обработать лист: %s, строку: %d, после значения: %s. Ошибка: %s";

        public static final String EXCEL_LINE_CONVERT_ERROR =
                "не удалось обработать строку: %d, после значения: %s. Ошибка: %s";

        public static final String CRM_EXCEPTION =
                "Ошибка во время получения токена: ";

    }

}
