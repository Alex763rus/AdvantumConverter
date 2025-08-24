package com.example.advantumconverter.rest;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.security.ConverterAccessService;
import com.example.advantumconverter.security.CustomUserDetails;
import com.example.advantumconverter.security.dto.RegistrationForm;
import com.example.advantumconverter.service.database.UserService;
import com.example.advantumconverter.service.excel.converter.client.ConvertServiceImplSber;
import com.example.advantumconverter.service.excel.generate.ClientExcelGenerateService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@Controller
@AllArgsConstructor
public class WebController {

    private final ConverterAccessService converterAccessService;
    private final UserService userService;
    private final ClientExcelGenerateService excelGenerateService;


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
        return "upload";
    }

    @PostMapping("/convert")
    public ResponseEntity<StreamingResponseBody> convertFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("converterType") String converterType) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

//            if (!converterAccessService.getConverter(converterType)) {
//                return ResponseEntity.status(403).build();
//            }

            var converter = converterAccessService.getConverter(converterType);
            if (converter.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            var inputFile = (XSSFWorkbook) (WorkbookFactory.create(file.getInputStream()));
            var convertedFile = converter.get().getConvertedBookV2(inputFile);
            var resultFile = excelGenerateService.createXlsxV2(convertedFile).getNewMediaFile();

            if (!resultFile.exists() || !resultFile.canRead()) {
                return ResponseEntity.status(500).build();
            }
            StreamingResponseBody stream = outputStream -> {
                try (FileInputStream fis = new FileInputStream(resultFile)) {
                    fis.transferTo(outputStream); // автоматически закроется
                }
            };

            String encodedFilename = "filename*=UTF-8''" + UriUtils.encode(resultFile.getName(), StandardCharsets.UTF_8);


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + encodedFilename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resultFile.length())
                    .body(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}