package com.example.advantumconverter.service.excel;

import com.example.advantumconverter.config.BotConfig;
import com.example.advantumconverter.enums.FileType;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileUploadServiceTest {

    private final BotConfig botConfig = mock(BotConfig.class);

    private FileUploadService service() {
        var service = new FileUploadService();
        ReflectionTestUtils.setField(service, "botConfig", botConfig);
        return service;
    }

    @Test
    void init_buildsUrls() {
        when(botConfig.getBotToken()).thenReturn("token");
        service().init();
    }

    @Test
    void getFileName_buildsPath() throws Exception {
        when(botConfig.getInputFilePath()).thenReturn("C:\\in\\");
        var fileName = service().getFileName(FileType.USER_IN, "file.xlsx");
        assertThat(fileName).contains("userIn\\").endsWith("file.xlsx");
    }

    @Test
    void uploadFileFromServer_returnsFile() throws Exception {
        var file = service().uploadFileFromServer("some/path.xlsx");
        assertThat(file.getName()).isEqualTo("path.xlsx");
    }
}
