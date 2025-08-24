package com.example.advantumconverter.rest;

import com.example.advantumconverter.model.dictionary.company.CompanySetting;
import com.example.advantumconverter.security.ConverterAccessService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.UriUtils;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@AllArgsConstructor
public class WebController {

    private final ConverterAccessService converterAccessService;
    private final UserService userService;
    private final CompanySetting companySetting;
    private final ConvertServiceImplSber convertServiceImplSber;
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
    public String showUploadPage(Model model) {
        model.addAttribute("formats", converterAccessService.getAvailableFormats());
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

            if (!converterAccessService.isConversionAllowed(converterType)) {
                return ResponseEntity.status(403).build();
            }

            var inputFile = (XSSFWorkbook) (WorkbookFactory.create(file.getInputStream()));
            var convertedFile = convertServiceImplSber.getConvertedBookV2(inputFile);
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
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resultFile.getName() + "\"")
//                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
////                    .contentLength(resultFile.length())
//                    .body(resource);

//            byte[] convertedData = convertExcel(file.getInputStream(), converterType);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            convertedFile.write(out);
//            convertedFile.close(); // освобождаем ресурсы
//            out.close();
//
//            byte[] data = out.toByteArray();
//
//            // 3. Создаём Resource
//            ByteArrayResource resource = new ByteArrayResource(data);
//
//            // 4. Возвращаем как файл для скачивания
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.xlsx\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .contentLength(data.length)
//                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private byte[] convertExcel(InputStream inputStream, String type) throws IOException {
        return switch (type.toLowerCase()) {
            case "csv" -> "id,name,age\n1,Alice,25\n2,Bob,30".getBytes();
            case "json" ->
                    "[{\"id\":1,\"name\":\"Alice\",\"age\":25},{\"id\":2,\"name\":\"Bob\",\"age\":30}]".getBytes();
            case "xml" -> """
                    <users>
                        <user id="1">
                            <name>Alice</name>
                            <age>25</age>
                        </user>
                        <user id="2">
                            <name>Bob</name>
                            <age>30</age>
                        </user>
                    </users>
                    """.getBytes();
            default -> throw new IllegalArgumentException("Неизвестный формат: " + type);
        };
    }

    private String getContentType(String type) {
        return switch (type.toLowerCase()) {
            case "csv" -> "text/csv";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            default -> "application/octet-stream";
        };
    }
}