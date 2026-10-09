package com.example.advantumconverter.support;

import com.example.advantumconverter.config.BotInitializer;
import com.example.advantumconverter.config.DatabaseTestConfig;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.service.TelegramBot;
import com.example.advantumconverter.service.excel.generate.ExcelGenerateService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.ExcelReader.read;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractConverterIntegrationTest {

    @MockBean
    protected TelegramBot telegramBot;

    @MockBean
    protected BotInitializer botInitializer;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        DatabaseTestConfig.start();
        registry.add("spring.datasource.url", DatabaseTestConfig::getJdbcUrl);
        registry.add("spring.datasource.username", DatabaseTestConfig::getUsername);
        registry.add("spring.datasource.password", DatabaseTestConfig::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    protected void assertMatchesGolden(ExcelGenerateService generator, ConvertedBookV2 book, String goldenResource)
            throws IOException {
        File generated = generator.createXlsxV2(book).getNewMediaFile();
        if (isUpdateGolden()) {
            Path target = Path.of("src", "test", "resources").resolve(goldenResource);
            Files.createDirectories(target.getParent());
            Files.copy(generated.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        try (InputStream is = new FileInputStream(generated)) {
            assertXlsxEquals(new XSSFWorkbook(is), read(goldenResource));
        }
    }

    private boolean isUpdateGolden() {
        return Boolean.getBoolean("update.golden")
                || "true".equalsIgnoreCase(System.getenv("UPDATE_GOLDEN"));
    }

    protected void assertXlsxEquals(XSSFWorkbook actual, XSSFWorkbook expected) {
        List<List<String>> actualGrid = toGrid(actual.getSheetAt(0));
        List<List<String>> expectedGrid = toGrid(expected.getSheetAt(0));
        int rows = Math.max(actualGrid.size(), expectedGrid.size());
        for (int r = 0; r < rows; r++) {
            List<String> actualRow = r < actualGrid.size() ? actualGrid.get(r) : List.of();
            List<String> expectedRow = r < expectedGrid.size() ? expectedGrid.get(r) : List.of();
            int cols = Math.max(actualRow.size(), expectedRow.size());
            for (int c = 0; c < cols; c++) {
                String actualCell = c < actualRow.size() ? actualRow.get(c) : "<нет>";
                String expectedCell = c < expectedRow.size() ? expectedRow.get(c) : "<нет>";
                assertThat(actualCell)
                        .as("cell [row=%s, col=%s]", r, c)
                        .isEqualTo(expectedCell);
            }
        }
    }

    private List<List<String>> toGrid(Sheet sheet) {
        int lastRow = sheet.getLastRowNum();
        List<List<String>> grid = new ArrayList<>();
        for (int r = 0; r <= lastRow; r++) {
            Row row = sheet.getRow(r);
            int lastCol = row == null ? 0 : row.getLastCellNum();
            List<String> cells = new ArrayList<>();
            for (int c = 0; c < lastCol; c++) {
                cells.add(canonical(row.getCell(c)));
            }
            grid.add(cells);
        }
        return grid;
    }

    private String canonical(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return "BOOL:" + cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return "DATE:" + cell.getDateCellValue().getTime();
                }
                double value = cell.getNumericCellValue();
                if (!Double.isInfinite(value) && value == Math.rint(value)) {
                    return "NUM:" + (long) value;
                }
                return "NUM:" + value;
            case Cell.CELL_TYPE_STRING:
                return "STR:" + cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return "FORMULA:" + cell.getCellFormula();
            default:
                return "";
        }
    }
}
