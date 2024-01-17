package com.example.advantumconverter.config;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.service.SecurityService;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.CompanyRepository;
import com.example.advantumconverter.service.excel.converter.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static com.example.advantumconverter.constant.Constant.Company.*;
import static com.example.advantumconverter.enums.UserRole.*;
import static org.example.tgcommons.constant.Constant.FileOperation.USER_DIR;
import static org.example.tgcommons.constant.Constant.TextConstants.SHIELD;

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
    @Autowired
    ObjectMapper objectMapper;

    @Value("${input.file.path}")
    String inputFilePath;

    private String getCurrentPath() {
        return System.getProperty(USER_DIR) + SHIELD;
    }


    @Autowired
    private ConvertServiceImplBogorodsk convertServiceImplBogorodsk;
    @Autowired
    private ConvertServiceImplCofix convertServiceImplCofix;
    @Autowired
    private ConvertServiceImplLenta convertServiceImplLenta;
    @Autowired
    private ConvertServiceImplSamokat convertServiceImplSamokat;
    @Autowired
    private ConvertServiceImplDominos convertServiceImplDominos;
    @Autowired
    private ConvertServiceImplAgroprom convertServiceImplAgroprom;
    @Autowired
    private ConvertServiceImplAgropromDetail convertServiceImplAgropromDetail;
    @Autowired
    private ConvertServiceImplOzon convertServiceImplOzon;
    @Autowired
    private CompanyRepository companyRepository;

    @Bean
    public CompanySetting companySetting() {
        val companySetting = new CompanySetting();
        val companyConverter = new HashMap<Company, List<? extends ConvertService>>();
        val lentaConverter = List.of(convertServiceImplLenta);
        val bushConverter = List.of(convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos, convertServiceImplAgroprom, convertServiceImplAgropromDetail);
        val advantumConverter = List.of(convertServiceImplLenta, convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos, convertServiceImplAgroprom, convertServiceImplAgropromDetail, convertServiceImplOzon);
        val ozonConverter = List.of(convertServiceImplOzon);

        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM), advantumConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA), lentaConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_BUSH_AVTOPROM), bushConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_OZON), ozonConverter);
        companySetting.setCompanyConverter(companyConverter);
        return companySetting;
    }

    @Bean
    public Map<UserRole, List<String>> roleAccess() {
        // Настройка команд по ролям:
        val roleAccess = new HashMap<UserRole, List<String>>();
        roleAccess.put(NEED_SETTING, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        roleAccess.put(EMPLOYEE, List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL, COMMAND_CONVERT_OZON));
        roleAccess.put(MAIN_EMPLOYEE, List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS, COMMAND_HISTORIC_ACTION, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL, COMMAND_CONVERT_OZON));
        roleAccess.put(SUPPORT, List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT,
                COMMAND_CONVERT_DOMINOS, COMMAND_SHOW_OPEN_TASK, COMMAND_SHOW_MY_TASK, COMMAND_HISTORIC_ACTION, COMMAND_RELOAD_DICTIONARY, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL, COMMAND_CONVERT_OZON));
        roleAccess.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_SETTING_NEW_USER, COMMAND_RELOAD_DICTIONARY));
        return roleAccess;
    }

    @Bean
    public Map<Company, List<String>> companyAccessList() {
        // Настройка доступов по компаниям:
        val commandAccessList = new HashMap<Company, List<String>>();
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM)
                , List.of(/*Общие:*/COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        /*Буш автопром:*/, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS, COMMAND_CONVERT_AGROPROM, COMMAND_CONVERT_AGROPROM_DETAIL
                        /*Лента:*/, COMMAND_CONVERT_LENTA
                        /*Озон:*/, COMMAND_CONVERT_OZON
                        /*Саппорт:*/, COMMAND_SHOW_OPEN_TASK, COMMAND_SHOW_MY_TASK, COMMAND_RELOAD_DICTIONARY
                        /*админ:*/, COMMAND_SETTING_NEW_USER, COMMAND_RELOAD_DICTIONARY
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_HISTORIC_ACTION
                        , COMMAND_CONVERT_LENTA
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
        return commandAccessList;
    }

}
