package com.example.advantumconverter.model.menu;

import com.example.advantumconverter.config.properties.ConverterProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.advantumconverter.constant.Constant.Command.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MenuStartTest {

    private static final String COMMAND_NO_ACCESS = "/cmd_no_access";

    private MenuStart menuStart;
    private Company company;

    private static Update update(long chatId) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(COMMAND_START);
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    private static User user(long chatId, UserRole role, Company company) {
        var u = new User();
        u.setChatId(chatId);
        u.setUserRole(role);
        u.setCompany(company);
        u.setFirstName("Иван");
        return u;
    }

    private static ConvertService converter(String command, String name, ExcelType type, Boolean enabled) {
        var service = mock(ConvertService.class);
        when(service.getConverterCommand()).thenReturn(command);
        when(service.getConverterName()).thenReturn(name);
        when(service.getExcelType()).thenReturn(type);
        if (enabled != null) {
            var settings = new ConverterProperties.ConverterSettings();
            settings.setEnabled(enabled);
            when(service.converterSettings()).thenReturn(settings);
        }
        return service;
    }

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setCompanyId(2L);
        company.setCompanyName("Адвантум");

        var converters = List.of(
                converter(COMMAND_CONVERT_ART_FRUIT, "АртФрут", ExcelType.CLIENT, null),
                converter(COMMAND_CONVERT_LENTA, "Лента", ExcelType.CLIENT, false),
                converter(COMMAND_CONVERT_BOOKER, "Бухгалтер", ExcelType.BOOKER, null),
                converter(COMMAND_CONVERT_RS_LENTA, "RS", ExcelType.RS, null),
                converter(COMMAND_CONVERT_RS_LENTA_SPB, "RS Спб", ExcelType.RS_LENTA_SPB, null),
                converter(COMMAND_NO_ACCESS, "Нет", ExcelType.CLIENT, null),
                converter(COMMAND_CONVERT_AGROPROM, "Агро", ExcelType.CLIENT, true)
        );

        var companySetting = new CompanySetting();
        companySetting.setCompanyConverter(Map.of(company, converters));

        var granted = List.of(COMMAND_START, COMMAND_DEFAULT, COMMAND_CONVERT_ART_FRUIT,
                COMMAND_CONVERT_RS_LENTA, COMMAND_CONVERT_RS_LENTA_SPB, COMMAND_CONVERT_AGROPROM);

        var roleAccess = new HashMap<UserRole, List<String>>();
        for (UserRole role : UserRole.values()) {
            roleAccess.put(role, granted);
        }
        var companyAccess = new HashMap<Company, List<String>>();
        companyAccess.put(company, granted);

        menuStart = new MenuStart(companySetting, roleAccess, companyAccess);
    }

    @Test
    void getMenuComand_andDescription() {
        assertThat(menuStart.getMenuComand()).isEqualTo(COMMAND_START);
        assertThat(menuStart.getDescription()).contains("Начало");
    }

    @Test
    void menuRun_needSetting() {
        assertThat(menuStart.menuRun(user(1L, UserRole.NEED_SETTING, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_blocked() {
        assertThat(menuStart.menuRun(user(1L, UserRole.BLOCKED, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_employee_withRsConverters() {
        assertThat(menuStart.menuRun(user(1L, UserRole.EMPLOYEE, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_mainEmployee() {
        assertThat(menuStart.menuRun(user(1L, UserRole.MAIN_EMPLOYEE, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_support() {
        assertThat(menuStart.menuRun(user(1L, UserRole.SUPPORT, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_admin() {
        assertThat(menuStart.menuRun(user(1L, UserRole.ADMIN, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_employeeApi() {
        assertThat(menuStart.menuRun(user(1L, UserRole.EMPLOYEE_API, company), update(1L))).isNotEmpty();
    }

    @Test
    void menuRun_employeeRs() {
        assertThat(menuStart.menuRun(user(1L, UserRole.EMPLOYEE_RS, company), update(1L))).isNotEmpty();
    }

    @Test
    void checkAccess_startAndDefaultAlways() {
        var u = user(1L, UserRole.EMPLOYEE, company);
        assertThat(menuStart.checkAccess(u, COMMAND_START)).isTrue();
        assertThat(menuStart.checkAccess(u, COMMAND_DEFAULT)).isTrue();
    }

    @Test
    void checkAccess_deniedForOtherCommand() {
        var u = user(1L, UserRole.EMPLOYEE, company);
        assertThat(menuStart.checkAccess(u, COMMAND_NO_ACCESS)).isFalse();
    }

    @Test
    void menuRun_employee_noRsConverters() {
        var clientOnly = List.of(converter(COMMAND_CONVERT_ART_FRUIT, "АртФрут", ExcelType.CLIENT, null));
        var cs = new CompanySetting();
        cs.setCompanyConverter(Map.of(company, clientOnly));
        var granted = List.of(COMMAND_START, COMMAND_DEFAULT, COMMAND_CONVERT_ART_FRUIT);
        var roleAccess = new HashMap<UserRole, List<String>>();
        for (UserRole role : UserRole.values()) {
            roleAccess.put(role, granted);
        }
        var companyAccess = new HashMap<Company, List<String>>();
        companyAccess.put(company, granted);
        var menu = new MenuStart(cs, roleAccess, companyAccess);

        assertThat(menu.menuRun(user(1L, UserRole.EMPLOYEE, company), update(1L))).isNotEmpty();
    }
}
