package com.example.advantumconverter.rest;

import com.example.advantumconverter.enums.ExcelType;
import com.example.advantumconverter.enums.ResultCode;
import com.example.advantumconverter.enums.UserRole;
import com.example.advantumconverter.model.jpa.Company;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import com.example.advantumconverter.model.rest.out.CrmGatewayResponseDto;
import com.example.advantumconverter.security.ConverterAccessService;
import com.example.advantumconverter.security.CustomUserDetails;
import com.example.advantumconverter.security.dto.RegistrationForm;
import com.example.advantumconverter.service.HistoryActionService;
import com.example.advantumconverter.service.database.UserService;
import com.example.advantumconverter.service.excel.converter.ConvertService;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import com.example.advantumconverter.service.rest.out.crm.CrmHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebControllerTest {

    @Mock
    private ConverterAccessService converterAccessService;
    @Mock
    private UserService userService;
    @Mock
    private CrmHelper crmHelper;
    @Mock
    private ExcelGenerateService excelGenerateService;
    @Mock
    private HistoryActionService historyActionService;
    @Mock
    private ConvertService converter;

    private WebController controller;
    private org.springframework.security.core.Authentication authentication;

    private final Company company = new Company();

    @BeforeEach
    void setUp() {
        company.setCompanyName("Test");
        Map<String, ExcelGenerateService> map = Map.of(ExcelType.CLIENT.getExcelType(), excelGenerateService);
        controller = new WebController(converterAccessService, userService, crmHelper, map, historyActionService);

        var details = new CustomUserDetails("user", "pass", UserRole.ADMIN, company);
        authentication = org.mockito.Mockito.mock(org.springframework.security.core.Authentication.class);
        when(authentication.getPrincipal()).thenReturn(details);
    }

    private ConvertedBookV2 convertedBook() {
        return ConvertedBookV2.init()
                .setMessage("done")
                .setResultCode(ResultCode.OK)
                .setBookV2(List.of(ConvertedListV2.init().setExcelListContentV2(List.of()).build()))
                .build();
    }

    private MultipartFile xlsxFile() throws Exception {
        byte[] bytes;
        try (var wb = new XSSFWorkbook(); var out = new ByteArrayOutputStream()) {
            wb.createSheet("Sheet1");
            wb.write(out);
            bytes = out.toByteArray();
        }
        return new MockMultipartFile("file", "in.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", bytes);
    }

    @Test
    void showRegistrationForm_addsForm() {
        Model model = org.mockito.Mockito.mock(Model.class);
        assertThat(controller.showRegistrationForm(model)).isEqualTo("register");
        verify(model).addAttribute(eq("registrationForm"), any());
    }

    @Test
    void processRegistration_registersAndRedirects() {
        var form = new RegistrationForm();
        form.setTelegramChatId(5L);
        form.setUsername("login");
        form.setFullName("Full Name");

        assertThat(controller.processRegistration(form)).isEqualTo("redirect:/setup");
        verify(userService).registerNewUser(5L, "login", "Full Name");
    }

    @Test
    void login_returnsLoginView() {
        assertThat(controller.login()).isEqualTo("login");
    }

    @Test
    void showUploadPage_populatesModel() {
        Model model = org.mockito.Mockito.mock(Model.class);
        when(converterAccessService.getAvailableFormats(authentication)).thenReturn(List.of());
        when(converterAccessService.shouldShowSendToCrm(authentication)).thenReturn(true);

        assertThat(controller.showUploadPage(model, authentication)).isEqualTo("upload");
        verify(model).addAttribute("username", "user");
        verify(model).addAttribute("showSendToCrm", true);
    }

    @Test
    void convertFile_emptyFile_badRequest() {
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        var response = controller.convertFile(file, "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Файл не выбран");
    }

    @Test
    void convertFile_unknownConverter_badRequest() {
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(converterAccessService.getConverter("cmd")).thenReturn(Optional.empty());

        var response = controller.convertFile(file, "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(historyActionService).saveWebHistoryErrorActionProtect(any(), any(), eq(""));
    }

    @Test
    void convertFile_success_returnsFile() throws Exception {
        var resultFile = Files.createTempFile("result", ".xlsx");
        resultFile.toFile().deleteOnExit();
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenReturn(convertedBook());
        when(converter.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(converter.getConverterCommand()).thenReturn("cmd");
        when(excelGenerateService.createXlsxV2(any())).thenReturn(new InputFile(resultFile.toFile()));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(org.mockito.Mockito.mock(HttpSession.class));

        var response = controller.convertFile(xlsxFile(), "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(historyActionService).saveWebHistoryActionProtect(any(), eq("in.xlsx"), eq("cmd"));
    }

    @Test
    void convertFile_conversionThrows_badRequest() throws Exception {
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenThrow(new RuntimeException("bad"));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);

        var response = controller.convertFile(xlsxFile(), "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void convertFile_sendToCrm_success() throws Exception {
        var resultFile = Files.createTempFile("result", ".xlsx");
        resultFile.toFile().deleteOnExit();
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenReturn(convertedBook());
        when(converter.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(converter.getConverterCommand()).thenReturn("cmd");
        when(excelGenerateService.createXlsxV2(any())).thenReturn(new InputFile(resultFile.toFile()));
        when(crmHelper.sendDocument(any(), any())).thenReturn(
                CrmGatewayResponseDto.of("crm ok", List.of(), List.of()));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(org.mockito.Mockito.mock(HttpSession.class));

        var response = controller.convertFile(xlsxFile(), "cmd", true, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("crm ok");
    }

    @Test
    void convertFile_sendToCrm_error_serverError() throws Exception {
        var resultFile = Files.createTempFile("result", ".xlsx");
        resultFile.toFile().deleteOnExit();
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenReturn(convertedBook());
        when(converter.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(excelGenerateService.createXlsxV2(any())).thenReturn(new InputFile(resultFile.toFile()));
        when(crmHelper.sendDocument(any(), any())).thenReturn(
                CrmGatewayResponseDto.of("crm fail", List.of(), List.of(
                        com.example.advantumconverter.model.rest.out.CrmGatewayReisResponseDto
                                .ofError("e1", 500, "err"))));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(org.mockito.Mockito.mock(HttpSession.class));

        var response = controller.convertFile(xlsxFile(), "cmd", true, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getConversionResultCode_sessionVariants() {
        HttpServletRequest noSession = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(noSession.getSession(false)).thenReturn(null);
        assertThat(controller.getConversionResultCode(noSession)).isEqualTo("OK");

        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(session.getAttribute("conversionResultCode")).thenReturn("WARNING");
        HttpServletRequest withSession = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(withSession.getSession(false)).thenReturn(session);
        assertThat(controller.getConversionResultCode(withSession)).isEqualTo("WARNING");
    }

    @Test
    void getConversionMessage_sessionVariants() {
        HttpServletRequest noSession = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(noSession.getSession(false)).thenReturn(null);
        assertThat(controller.getConversionMessage(noSession)).isEqualTo("");

        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(session.getAttribute("conversionMessage")).thenReturn("hello");
        HttpServletRequest withSession = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(withSession.getSession(false)).thenReturn(session);
        assertThat(controller.getConversionMessage(withSession)).isEqualTo("hello");
    }

    @Test
    void downloadFile_missingAttributes_badRequest() {
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);

        var response = controller.downloadFile(session);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void downloadFile_fileNotFound_404() {
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(session.getAttribute("download.file.path")).thenReturn("Z:\\does\\not\\exist.xlsx");
        when(session.getAttribute("download.file.name")).thenReturn("x.xlsx");

        var response = controller.downloadFile(session);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void downloadFile_existingFile_ok() throws Exception {
        var file = Files.createTempFile("download", ".xlsx");
        file.toFile().deleteOnExit();
        Files.writeString(file, "content");
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(session.getAttribute("download.file.path")).thenReturn(file.toString());
        when(session.getAttribute("download.file.name")).thenReturn("x.xlsx");

        var response = controller.downloadFile(session);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void downloadFile_streamsContent() throws Exception {
        var file = Files.createTempFile("download", ".xlsx");
        file.toFile().deleteOnExit();
        Files.writeString(file, "payload");
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(session.getAttribute("download.file.path")).thenReturn(file.toString());
        when(session.getAttribute("download.file.name")).thenReturn("x.xlsx");

        var response = controller.downloadFile(session);
        var out = new ByteArrayOutputStream();
        response.getBody().writeTo(out);

        assertThat(out.toString()).isEqualTo("payload");
    }

    @Test
    void convertFile_emptyMessage_usesDefault() throws Exception {
        var resultFile = Files.createTempFile("result", ".xlsx");
        resultFile.toFile().deleteOnExit();
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenReturn(ConvertedBookV2.init()
                .setMessage("")
                .setResultCode(ResultCode.OK)
                .setBookV2(List.of(ConvertedListV2.init().setExcelListContentV2(List.of()).build()))
                .build());
        when(converter.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(converter.getConverterCommand()).thenReturn("cmd");
        when(excelGenerateService.createXlsxV2(any())).thenReturn(new InputFile(resultFile.toFile()));
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        var response = controller.convertFile(xlsxFile(), "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(session).setAttribute("conversionMessage", "Конвертация выполнена успешно");
    }

    @Test
    void convertFile_resultFileMissing_serverError() throws Exception {
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converter.getConvertedBookV2(any())).thenReturn(convertedBook());
        when(converter.getExcelType()).thenReturn(ExcelType.CLIENT);
        when(converter.getConverterCommand()).thenReturn("cmd");
        when(excelGenerateService.createXlsxV2(any()))
                .thenReturn(new InputFile(new java.io.File("Z:\\does\\not\\exist.xlsx")));
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(org.mockito.Mockito.mock(HttpSession.class));

        var response = controller.convertFile(xlsxFile(), "cmd", false, authentication, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void convertFile_unexpectedException_serverError() throws Exception {
        doReturn(Optional.of(converter)).when(converterAccessService).getConverter("cmd");
        when(converterAccessService.getConverter("boom")).thenThrow(new RuntimeException("boom"));

        var response = controller.convertFile(xlsxFile(), "boom", false, authentication,
                org.mockito.Mockito.mock(HttpServletRequest.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
