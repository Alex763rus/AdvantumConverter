package com.example.advantumconverter.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
public class WebController {

    @GetMapping("/")
    public String showUploadPage() {
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

            // Проверяем, поддерживаем ли такой тип
            if (!Arrays.asList("csv", "json", "xml").contains(converterType)) {
                return ResponseEntity.badRequest().build();
            }

            // Конвертируем в зависимости от типа
            byte[] convertedData = convertExcel(file.getInputStream(), converterType);

            // Определяем имя и тип файла
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
        // Здесь будет твоя логика с Apache POI
        // Пока — заглушки

        return switch (type.toLowerCase()) {
            case "csv" -> "id,name,age\n1,Alice,25\n2,Bob,30".getBytes();
            case "json" -> "[{\"id\":1,\"name\":\"Alice\",\"age\":25},{\"id\":2,\"name\":\"Bob\",\"age\":30}]".getBytes();
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