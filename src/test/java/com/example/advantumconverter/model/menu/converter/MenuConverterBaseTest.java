package com.example.advantumconverter.model.menu.converter;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.enums.State;
import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.User;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.model.rest.out.CrmGatewayResponseDto;
import com.example.advantumconverter.model.rest.out.CrmGatewayReisResponseDto;
import com.example.advantumconverter.service.excel.FileUploadService;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import com.example.advantumconverter.service.menu.StateService;
import com.example.advantumconverter.service.rest.out.crm.CrmHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuConverterBaseTest {

    private static final String COMMAND = "/test_convert";

    @Mock
    private StateService stateService;
    @Mock
    private FileUploadService fileUploadService;
    @Mock
    private ClientExcelGenerateService clientExcelGenerateService;
    @Mock
    private CrmHelper crmHelper;
    @Mock
    private ConvertService convertService;
    @Mock
    private ExcelGenerateService excelGenerateService;

    private TestMenu menu;

    static class TestMenu extends MenuConverterBase {
        private final ConvertService convertService;

        TestMenu(ConvertService convertService) {
            this.convertService = convertService;
        }

        @Override
        public String getMenuComand() {
            return COMMAND;
        }

        @Override
        public List<PartialBotApiMethod> menuRun(User user, Update update) {
            return convertFileLogic(user, update, convertService);
        }

        @Override
        public String getDescription() {
            return "test";
        }

        List<PartialBotApiMethod> free(User user, Update update, State state, String fileName) {
            return freeLogic(user, update, state, fileName);
        }

        List<PartialBotApiMethod> freeCallback(User user, Update update) {
            return freeLogic(user, update);
        }

        List<PartialBotApiMethod> unload(User user, Update update) {
            return unloadInCrmLogic(user, update, convertService);
        }

        List<PartialBotApiMethod> unloadCrm(User user, Update update, CrmConfigProperties.CrmCreds creds) {
            return unloadInCrm(user, update, creds);
        }
    }

    private static User user(long chatId, UserRole role) {
        var user = new User();
        user.setChatId(chatId);
        user.setUserRole(role);
        return user;
    }

    private static Update documentUpdate(long chatId, String fileName) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        var document = new Document();
        document.setFileName(fileName);
        document.setFileId("file-id");
        message.setDocument(document);
        var update = new Update();
        update.setMessage(message);
        return update;
    }

    private static Update callbackUpdate(long chatId, String data) {
        var message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setMessageId(42);
        var callback = new CallbackQuery();
        callback.setData(data);
        callback.setMessage(message);
        var update = new Update();
        update.setCallbackQuery(callback);
        return update;
    }

    @BeforeEach
    void setUp() {
        menu = new TestMenu(convertService);
        ReflectionTestUtils.setField(menu, "stateService", stateService);
        ReflectionTestUtils.setField(menu, "fileUploadService", fileUploadService);
        ReflectionTestUtils.setField(menu, "excelGenerateService", clientExcelGenerateService);
        ReflectionTestUtils.setField(menu, "excelGenerateServiceMap", Map.of("Client", excelGenerateService));
        ReflectionTestUtils.setField(menu, "crmHelper", crmHelper);
    }

    @Test
    void convertFile_v1_success() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = documentUpdate(1L, "in.xlsx");
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx("c:/in/in.xlsx", "file-id")).thenReturn(book);
        var converted = ConvertedBook.init().setBookName("b").setMessage("ok").build();
        when(convertService.getConvertedBook(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsx(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");

        assertThat(menu.menuRun(user, update)).isNotEmpty();
        verify(stateService).setState(user, State.FREE);
    }

    @Test
    void convertFile_v2_success() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = documentUpdate(1L, "in.xlsm");
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsm"))).thenReturn("c:/in/in.xlsm");
        when(fileUploadService.uploadXlsx("c:/in/in.xlsm", "file-id")).thenReturn(book);
        var converted = ConvertedBookV2.init().setBookName("b").setMessage("ok").build();
        when(convertService.getConvertedBook(book)).thenReturn(null);
        when(convertService.getConvertedBookV2(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void convertFile_v2_longMessage() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = documentUpdate(1L, "in.xlsx");
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx(any(), any())).thenReturn(book);
        var converted = ConvertedBookV2.init().setBookName("b").setMessage("x".repeat(900)).build();
        when(convertService.getConvertedBook(book)).thenReturn(null);
        when(convertService.getConvertedBookV2(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void convertFile_v2_crmBranch() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var update = documentUpdate(1L, "in.xlsx");
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx(any(), any())).thenReturn(book);
        var converted = ConvertedBookV2.init().setBookName("b").setMessage("ok").build();
        when(convertService.getConvertedBook(book)).thenReturn(null);
        when(convertService.getConvertedBookV2(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");
        when(convertService.getCrmCreds()).thenReturn(new CrmConfigProperties.CrmCreds());

        assertThat(menu.menuRun(user, update)).hasSize(2);
        verify(stateService).setState(user, State.CONVERTER_WAIT_UNLOAD_IN_CRM);
    }

    @Test
    void convertFile_noMessage() {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = mock(Update.class);
        when(update.hasMessage()).thenReturn(false);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        when(update.getMessage()).thenReturn(message);

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void convertFile_noDocument() {
        var user = user(1L, UserRole.EMPLOYEE);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        var update = new Update();
        update.setMessage(message);

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void convertFile_wrongExtension() {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = documentUpdate(1L, "in.txt");

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void convertFile_exception() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = documentUpdate(1L, "in.xlsx");
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx(any(), any())).thenReturn(book);
        when(convertService.getConvertedBook(book)).thenThrow(new RuntimeException("boom"));
        when(convertService.getConverterName()).thenReturn("Test");

        assertThat(menu.menuRun(user, update)).isNotEmpty();
    }

    @Test
    void freeLogic_matchingCommand() {
        var user = user(1L, UserRole.EMPLOYEE);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText(COMMAND);
        var update = new Update();
        update.setMessage(message);

        assertThat(menu.free(user, update, State.CONVERT_FILE_ART_FRUIT, "file.xlsx")).isNotEmpty();
        verify(stateService).setState(user, State.CONVERT_FILE_ART_FRUIT);
    }

    @Test
    void freeLogic_wrongCommand() {
        var user = user(1L, UserRole.EMPLOYEE);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setText("/other");
        var update = new Update();
        update.setMessage(message);

        assertThat(menu.free(user, update, State.CONVERT_FILE_ART_FRUIT, "file.xlsx")).isNotEmpty();
    }

    @Test
    void freeLogic_callback() {
        var user = user(1L, UserRole.EMPLOYEE);
        var update = callbackUpdate(1L, "FREE");

        assertThat(menu.freeCallback(user, update)).hasSize(2);
        verify(stateService).setState(user, State.FREE);
    }

    @Test
    void unloadInCrmLogic_free() {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var update = callbackUpdate(1L, State.FREE.name());

        assertThat(menu.unload(user, update)).isNotEmpty();
    }

    @Test
    void unloadInCrmLogic_unload() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx(any(), any())).thenReturn(book);
        var converted = ConvertedBookV2.init().setBookName("b").setMessage("ok").setBookV2(List.of(ConvertedListV2.init().build())).build();
        when(convertService.getConvertedBook(book)).thenReturn(null);
        when(convertService.getConvertedBookV2(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");
        when(convertService.getCrmCreds()).thenReturn(new CrmConfigProperties.CrmCreds());
        menu.menuRun(user, documentUpdate(1L, "in.xlsx"));

        var update = callbackUpdate(1L, State.CONVERTER_WAIT_UNLOAD_IN_CRM.name());
        when(crmHelper.sendDocument(any(), any())).thenReturn(CrmGatewayResponseDto.of("ok"));

        assertThat(menu.unload(user, update)).isNotEmpty();
    }

    @Test
    void unloadInCrmLogic_defaultState() {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var update = mock(Update.class);
        when(update.hasCallbackQuery()).thenReturn(true);
        var callback = new CallbackQuery();
        callback.setData(State.CANCEL.name());
        callback.setMessage(new Message());
        when(update.getCallbackQuery()).thenReturn(callback);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        when(update.getMessage()).thenReturn(message);

        assertThat(menu.unload(user, update)).isNotEmpty();
    }

    @Test
    void unloadInCrmLogic_noCallback() {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var message = new Message();
        message.setChat(new Chat(1L, "private"));
        var update = new Update();
        update.setMessage(message);

        assertThat(menu.unload(user, update)).isNotEmpty();
    }

    @Test
    void unloadInCrm_v1Book() {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var v1 = ConvertedBook.init().setBookName("b1").build();
        ReflectionTestUtils.setField(menu, "convertedBooks",
                new java.util.concurrent.ConcurrentHashMap<>(Map.of(user, v1)));

        var update = callbackUpdate(1L, State.CONVERTER_WAIT_UNLOAD_IN_CRM.name());

        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                        menu.unloadCrm(user, update, new CrmConfigProperties.CrmCreds()))
                .isInstanceOf(org.apache.commons.lang3.NotImplementedException.class);
    }

    @Test
    void unloadInCrm_logFileWriteError_caught() {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var v2 = ConvertedBookV2.init().setBookName("b")
                .setBookV2(List.of(ConvertedListV2.init().build()))
                .build();
        ReflectionTestUtils.setField(menu, "convertedBooksV2",
                new java.util.concurrent.ConcurrentHashMap<>(Map.of(user, v2)));

        var update = callbackUpdate(1L, State.CONVERTER_WAIT_UNLOAD_IN_CRM.name());
        var errors = CrmGatewayResponseDto.init()
                .setMessage(null)
                .setReisError(List.of(mock(CrmGatewayReisResponseDto.class)))
                .build();
        when(crmHelper.sendDocument(any(), any())).thenReturn(errors);

        assertThat(menu.unloadCrm(user, update, new CrmConfigProperties.CrmCreds())).isNotEmpty();
    }

    @Test
    void unloadInCrm_withErrors() throws Exception {
        var user = user(1L, UserRole.EMPLOYEE_API);
        var book = mock(XSSFWorkbook.class);
        when(fileUploadService.getFileName(any(), eq("in.xlsx"))).thenReturn("c:/in/in.xlsx");
        when(fileUploadService.uploadXlsx(any(), any())).thenReturn(book);
        var converted = ConvertedBookV2.init().setBookName("b").setMessage("ok").setBookV2(List.of(ConvertedListV2.init().build())).build();
        when(convertService.getConvertedBook(book)).thenReturn(null);
        when(convertService.getConvertedBookV2(book)).thenReturn(converted);
        when(convertService.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(converted)).thenReturn(new InputFile("out.xlsx"));
        when(convertService.getConverterName()).thenReturn("Test");
        when(convertService.getCrmCreds()).thenReturn(new CrmConfigProperties.CrmCreds());
        menu.menuRun(user, documentUpdate(1L, "in.xlsx"));

        var update = callbackUpdate(1L, State.CONVERTER_WAIT_UNLOAD_IN_CRM.name());
        var errors = CrmGatewayResponseDto.init()
                .setMessage("with errors")
                .setReisError(List.of(mock(CrmGatewayReisResponseDto.class)))
                .build();
        when(crmHelper.sendDocument(any(), any())).thenReturn(errors);

        assertThat(menu.unload(user, update)).isNotEmpty();
    }
}
