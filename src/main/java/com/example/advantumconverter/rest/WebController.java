package com.example.advantumconverter.rest;

import com.example.advantumconverter.model.jpa.UserRepository;
import com.example.advantumconverter.security.ConverterAccessService;
import com.example.advantumconverter.security.dto.RegistrationForm;
import com.example.advantumconverter.service.database.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class WebController {

    private final ConverterAccessService converterAccessService;
    private final UserService userService;

    public WebController(ConverterAccessService converterAccessService, UserService userService) {
        this.converterAccessService = converterAccessService;
        this.userService = userService;
    }

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
    public ResponseEntity<Resource> convertFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("converterType") String converterType) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (!converterAccessService.isConversionAllowed(converterType)) {
                return ResponseEntity.status(403).build();
            }

            byte[] convertedData = convertExcel(file.getInputStream(), converterType);
            String filename = "converted." + converterType;
            String contentType = getContentType(converterType);

            ByteArrayResource resource = new ByteArrayResource(convertedData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

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