package com.example.advantumconverter.rest;

import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.security.ConverterAccessService;
import com.example.advantumconverter.security.CustomUserDetails;
import com.example.advantumconverter.security.dto.RegistrationForm;
import com.example.advantumconverter.service.database.UserService;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import com.example.advantumconverter.service.rest.out.crm.CrmHelper;
import com.example.advantumconverter.service.rest.out.mapper.BookToCrmReisMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@AllArgsConstructor
@Log4j2
public class WebController {

    private final ConverterAccessService converterAccessService;
    private final UserService userService;
    private final ClientExcelGenerateService excelGenerateService;
    private final CrmHelper crmHelper;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(RegistrationForm form) {
        // Перенаправляем на страницу настройки
        userService.registerNewUser(form.getTelegramChatId(), form.getUsername(), form.getFullName());
        return "redirect:/setup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String showUploadPage(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roleTitle", userDetails.getRole().getTitle());
        model.addAttribute("companyName", userDetails.getCompany().getCompanyName());
        model.addAttribute("formats", converterAccessService.getAvailableFormats(authentication));
        model.addAttribute("showSendToCrm", converterAccessService.shouldShowSendToCrm(authentication));
        return "upload";
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convertFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("converterType") String converterType,
            @RequestParam(required = false, defaultValue = "false") boolean sendToCrm,
            Authentication authentication) {

        try {
            // 1. Проверка файла
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Файл не выбран");
            }

            // 2. Проверка доступа к конвертеру
            var converterOpt = converterAccessService.getConverter(converterType);
            if (converterOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Недопустимый тип конвертации: " + converterType);
            }
            var converter = converterOpt.get();
            XSSFWorkbook inputFile;
            try (InputStream is = file.getInputStream()) {
                inputFile = (XSSFWorkbook) WorkbookFactory.create(is);
            }

            // 🔻 Конвертация
            ConvertedBookV2 convertedFile;
            try {
                convertedFile = converter.getConvertedBookV2(inputFile);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ошибка конвертации: " + e.getMessage());
            }

            // Генерация результата
            File resultFile = excelGenerateService.createXlsxV2(convertedFile).getNewMediaFile();
            if (!resultFile.exists()) {
                return ResponseEntity.status(500).body("Не удалось сгенерировать файл");
            }

            // ✅ Успех: отправляем в crm:
            if (sendToCrm) {
                var crmGatewayResponseDto = crmHelper.sendDocument(BookToCrmReisMapper.map(convertedFile), converter.getCrmCreds());
                if (Objects.requireNonNullElse(crmGatewayResponseDto.getReisError(), new ArrayList<>()).isEmpty()) {
                    return ResponseEntity.ok().body(crmGatewayResponseDto.getMessage());
                } else {
                    log.error("Ошибка во время загрузки рейсов. " + crmGatewayResponseDto.getMessage());
                    return ResponseEntity.status(500).body("Ошибка во время загрузки рейсов. " + crmGatewayResponseDto.getMessage());
                }
            }
            // ✅ Успех: возвращаем файл
            InputStreamResource resource = new InputStreamResource(new FileInputStream(resultFile));

            String encodedFilename = "filename*=UTF-8''" + UriUtils.encode(resultFile.getName(), StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + encodedFilename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resultFile.length())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Внутренняя ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadFile(
            HttpSession session) {

        // ✅ Читаем из сессии
        String filePath = (String) session.getAttribute("download.file.path");
        String filename = (String) session.getAttribute("download.file.name");

        // Очищаем сессию сразу
        session.removeAttribute("download.file.path");
        session.removeAttribute("download.file.name");

        if (filePath == null || filename == null) {
            return ResponseEntity.badRequest().build();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(404).build();
        }

        StreamingResponseBody stream = outputStream -> {
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.transferTo(outputStream);
            }
        };

        String encodedFilename = "filename*=UTF-8''" + UriUtils.encode(filename, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + encodedFilename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(stream);
    }
}