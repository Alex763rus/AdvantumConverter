package com.example.advantumconverter.config;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.converter.booker.ConvertServiceImplBooker;
import com.example.advantumconverter.service.excel.converter.client.*;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsInnerLenta;
import com.example.advantumconverter.service.excel.converter.rs.ConvertServiceImplRsLenta;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static com.example.advantumconverter.constant.Constant.Company.*;
import static com.example.advantumconverter.enums.UserRole.*;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.version}")
    String botVersion;

    @Value("${bot.username}")
    String botUserName;

    @Value("${bot.token}")
    String botToken;

    @Value("${admin.chatid}")
    String adminChatId;

    @Value("${service.file_info.uri}")
    String fileInfoUri;

    @Value("${service.file_storage.uri}")
    String fileStorageUri;

    @Value("${input.file.path}")
    String inputFilePath;

    @Bean
    public CompanySetting companySetting(
            ConvertServiceImplBogorodsk convertServiceImplBogorodsk,
            ConvertServiceImplCofix convertServiceImplCofix,
            ConvertServiceImplLenta convertServiceImplLenta,
            ConvertServiceImplSamokat convertServiceImplSamokat,
            ConvertServiceImplDominos convertServiceImplDominos,
            ConvertServiceImplAgroprom convertServiceImplAgroprom,
            ConvertServiceImplAgropromDetail convertServiceImplAgropromDetail,
            ConvertServiceImplOzon convertServiceImplOzon,
            ConvertServiceImplMetro convertServiceImplMetro,
            ConvertServiceImplBooker convertServiceImplBooker,
            ConvertServiceImplSber convertServiceImplSber,
            ConvertServiceImplArtFruit convertServiceImplArtFruit,
            ConvertServiceImplSiel convertServiceImplSiel,
            ConvertServiceImplSpar convertServiceImplSpar,
            ConvertServiceImplNika convertServiceImplNika,
            ConvertServiceImplFragrantWorld convertServiceImplFragrantWorld,
            CompanyRepository companyRepository,
            ConvertServiceImplRsLenta convertServiceImplRsLenta,
            ConvertServiceImplRsInnerLenta convertServiceImplRsInnerLenta
    ) {
        val companySetting = new CompanySetting();
        val companyConverter = new HashMap<Company, List<? extends ConvertService>>();
        val lentaConverter = List.of(convertServiceImplLenta, convertServiceImplRsLenta, convertServiceImplRsInnerLenta);
        val bushConverter = List.of(convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos, convertServiceImplAgroprom, convertServiceImplAgropromDetail);
        val advantumConverter = List.of(convertServiceImplLenta, convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos, convertServiceImplAgroprom, convertServiceImplAgropromDetail, convertServiceImplOzon, convertServiceImplMetro, convertServiceImplSber, convertServiceImplArtFruit
                , convertServiceImplBooker, convertServiceImplSiel, convertServiceImplSpar, convertServiceImplNika, convertServiceImplFragrantWorld, convertServiceImplRsLenta, convertServiceImplRsInnerLenta);
        val ozonConverter = List.of(convertServiceImplOzon);
        val metroConverter = List.of(convertServiceImplMetro);
        val sberConverter = List.of(convertServiceImplSber);
        val artFruitConverter = List.of(convertServiceImplArtFruit);
        val sielConverter = List.of(convertServiceImplSiel);
        val sparConverter = List.of(convertServiceImplSpar);
        val snikaConverter = List.of(convertServiceImplNika);
        val fragrantWorldrConverter = List.of(convertServiceImplFragrantWorld);

        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM), advantumConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA), lentaConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_BUSH_AVTOPROM), bushConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_OZON), ozonConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_METRO), metroConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SBER), sberConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_ART_FRUIT), artFruitConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SIEL), sielConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SPAR), sparConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_NIKA), snikaConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_FRAGRANT_WORLD), fragrantWorldrConverter);
        companySetting.setCompanyConverter(companyConverter);
        return companySetting;
    }

    @Bean
    public Map<UserRole, List<String>> roleAccess() {
        var defaultCommands = List.of(COMMAND_DEFAULT, COMMAND_START);

        var rsConverters = new ArrayList<String>(defaultCommands);
        rsConverters.add(COMMAND_CONVERT_RS_LENTA);
        rsConverters.add(COMMAND_CONVERT_RS_INNER_LENTA);

        var allConverters = new ArrayList<String>(defaultCommands);
        allConverters.add(COMMAND_FAQ);
        allConverters.add(COMMAND_CONVERT_BOGORODSK);
        allConverters.add(COMMAND_CONVERT_COFIX);
        allConverters.add(COMMAND_CONVERT_LENTA);
        allConverters.add(COMMAND_CONVERT_SAMOKAT);
        allConverters.add(COMMAND_CONVERT_DOMINOS);
        allConverters.add(COMMAND_CONVERT_AGROPROM);
        allConverters.add(COMMAND_CONVERT_AGROPROM_DETAIL);
        allConverters.add(COMMAND_CONVERT_OZON);
        allConverters.add(COMMAND_CONVERT_SIEL);
        allConverters.add(COMMAND_CONVERT_SPAR);
        allConverters.add(COMMAND_CONVERT_FRAGRANT_WORLD);
        allConverters.add(COMMAND_CONVERT_NIKA);
        allConverters.add(COMMAND_CONVERT_METRO);
        allConverters.add(COMMAND_CONVERT_BOOKER);
        allConverters.add(COMMAND_CONVERT_SBER);
        allConverters.add(COMMAND_CONVERT_ART_FRUIT);

        var employeeCommands = new ArrayList<>(allConverters);
        employeeCommands.addAll(rsConverters);

        var mainEmployeeCommands = new ArrayList<String>(allConverters);
        mainEmployeeCommands.addAll(rsConverters);
        mainEmployeeCommands.add(COMMAND_HISTORIC_ACTION);

        var supportCommands = new ArrayList<String>(mainEmployeeCommands);
        supportCommands.addAll(rsConverters);
        supportCommands.add(COMMAND_SHOW_OPEN_TASK);
        supportCommands.add(COMMAND_SHOW_MY_TASK);
        supportCommands.add(COMMAND_RELOAD_DICTIONARY);

        var adminCommands = new ArrayList<String>(mainEmployeeCommands);
        adminCommands.addAll(rsConverters);
        adminCommands.add(COMMAND_SETTING_NEW_USER);
        adminCommands.add(COMMAND_RELOAD_DICTIONARY);
        adminCommands.add(COMMAND_HISTORIC_ACTION);

        var employeeApiCommands = new ArrayList<String>(mainEmployeeCommands);
        employeeApiCommands.addAll(rsConverters);
        employeeApiCommands.add(COMMAND_SHOW_OPEN_TASK);
        employeeApiCommands.add(COMMAND_SHOW_MY_TASK);
        employeeApiCommands.add(COMMAND_RELOAD_DICTIONARY);

        var employeeRsCommands = new ArrayList<>(rsConverters);

        // Настройка команд по ролям:
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(NEED_SETTING, defaultCommands);
        roleAccess.put(BLOCKED, defaultCommands);
        roleAccess.put(EMPLOYEE, employeeCommands);
        roleAccess.put(MAIN_EMPLOYEE, mainEmployeeCommands);
        roleAccess.put(SUPPORT, supportCommands);
        roleAccess.put(ADMIN, adminCommands);
        roleAccess.put(EMPLOYEE_API, employeeApiCommands);
        roleAccess.put(EMPLOYEE_RS, employeeRsCommands);

        return roleAccess;
    }

    @Bean
    public Map<Company, List<String>> companyAccessList(CompanyRepository companyRepository) {
        // Настройка доступов по компаниям:
        val commandAccessList = new HashMap<Company, List<String>>();
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM)
                , List.of(/*Общие:*/COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        /*Буш автопром:*/, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL
                        /*Лента:*/, COMMAND_CONVERT_LENTA
                        /*RS Лента:*/, COMMAND_CONVERT_RS_LENTA
                        /*RS Лента внутренний:*/, COMMAND_CONVERT_RS_INNER_LENTA
                        /*Озон:*/, COMMAND_CONVERT_OZON
                        /*Метро:*/, COMMAND_CONVERT_METRO
                        /*Сбер логистик:*/, COMMAND_CONVERT_SBER
                        /*Арт Фрут:*/, COMMAND_CONVERT_ART_FRUIT
                        /*Сиэль:*/, COMMAND_CONVERT_SIEL
                        /*Спар:*/, COMMAND_CONVERT_SPAR
                        /*Ника Транс:*/, COMMAND_CONVERT_NIKA
                        /*Ароматный мир:*/, COMMAND_CONVERT_FRAGRANT_WORLD
                        /*Бухгалтерия*/, COMMAND_CONVERT_BOOKER
                        /*Саппорт:*/, COMMAND_SHOW_OPEN_TASK, COMMAND_SHOW_MY_TASK, COMMAND_RELOAD_DICTIONARY
                        /*админ:*/, COMMAND_SETTING_NEW_USER, COMMAND_RELOAD_DICTIONARY
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_LENTA, COMMAND_CONVERT_RS_LENTA, COMMAND_CONVERT_RS_INNER_LENTA
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_BUSH_AVTOPROM)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_OZON)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_OZON
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_METRO)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_METRO
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SBER)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_SBER
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_ART_FRUIT)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_ART_FRUIT
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SIEL)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_SIEL
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_SPAR)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_SPAR
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_NIKA)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMPANY_NAME_NIKA
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_FRAGRANT_WORLD)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_FRAGRANT_WORLD
                )
        );
        return commandAccessList;
    }

}
