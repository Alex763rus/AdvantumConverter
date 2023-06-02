package com.example.advantumconverter.config;

import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.dictionary.security.Security;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.advantumconverter.constant.Constant.*;
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
    private CompanyRepository companyRepository;

    @Bean
    public CompanySetting companySetting() {
        val companySetting = new CompanySetting();
        val companyConverter = new HashMap<Company, List<? extends ConvertService>>();
        val lentaConverter = List.of(convertServiceImplLenta);
        val bushConverter = List.of(convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos);
        val advantumConverter = List.of(convertServiceImplLenta, convertServiceImplBogorodsk, convertServiceImplCofix
                , convertServiceImplSamokat, convertServiceImplDominos);

        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM), advantumConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA), lentaConverter);
        companyConverter.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_BUSH_AVTOPROM), bushConverter);
        companySetting.setCompanyConverter(companyConverter);
        return companySetting;
    }

    @Bean
    public Security security() {
        val roleSecurity = new Security();
        val accessList = new HashMap<UserRole, List<String>>();
        accessList.put(NEED_SETTING, List.of(COMMAND_DEFAULT, COMMAND_START));
        accessList.put(BLOCKED, List.of(COMMAND_DEFAULT, COMMAND_START));
        accessList.put(EMPLOYEE, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS));
        accessList.put(MAIN_EMPLOYEE, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS));
        accessList.put(SUPPORT, List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_LENTA, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS));
        accessList.put(ADMIN, List.of(COMMAND_DEFAULT, COMMAND_START, COMMAND_SETTING_NEW_USER));
        roleSecurity.setRoleAccessList(accessList);

        val commandAccessList = new HashMap<Company, List<String>>();
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_LENTA)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START, COMMAND_CONVERT_LENTA
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_NAME_BUSH_AVTOPROM)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START
                        , COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS
                )
        );
        commandAccessList.put(companyRepository.getCompaniesByCompanyName(COMPANY_ADVANTUM)
                , List.of(COMMAND_FAQ, COMMAND_DEFAULT, COMMAND_START
                        , COMMAND_CONVERT_BOGORODSK, COMMAND_CONVERT_COFIX, COMMAND_CONVERT_SAMOKAT, COMMAND_CONVERT_DOMINOS
                        , COMMAND_CONVERT_LENTA
                        , COMMAND_SETTING_NEW_USER
                )
        );
        roleSecurity.setCompanyAccessList(commandAccessList);
        return roleSecurity;
    }

}
