package com.example.advantumconverter.service.excel.generate;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.ConvertedList;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClientExcelGenerateServiceTest {

    private final ClientExcelGenerateService service = new ClientExcelGenerateService();

    private static List<String> strings(String... values) {
        return new ArrayList<>(List.of(values));
    }

    private ConvertedBookV2 bookV2() {
        var clients = ConvertedListDataClientsV2.init()
                .setColumnAdata("A")
                .setColumnBdata(new Date())
                .setColumnCdata("C")
                .setColumnDdata("D")
                .setColumnEdata(1)
                .setColumnFdata("F")
                .setColumnGdata("G")
                .setColumnHdata("H")
                .setColumnIdata(1)
                .setColumnJdata(2)
                .setColumnKdata(3)
                .setColumnLdata(4)
                .setColumnMdata(5)
                .setColumnNdata(6)
                .setColumnOdata(7)
                .setColumnPdata(8)
                .setColumnQdata(9)
                .setColumnRdata(10)
                .setColumnSdata(new Date())
                .setColumnTdata(new Date())
                .setColumnUdata("U")
                .setColumnVdata("V")
                .setColumnWdata("W")
                .setColumnXdata(1)
                .setColumnYdata(1.5)
                .setColumnZdata(null)
                .setColumnAaData("AA")
                .setColumnAbData("AB")
                .setColumnAcData("AC")
                .setColumnAdData("AD")
                .setColumnAeData("AE")
                .setColumnAfData(1)
                .setColumnAgData("AG")
                .setColumnAhData("AH")
                .setColumnAiData(1)
                .setColumnAjData("AJ")
                .setColumnAkData("AK")
                .setColumnAlData("AL")
                .setColumnAmData("AM")
                .setColumnAnData("AN")
                .setColumnAoData("AO")
                .setColumnApData("AP")
                .setColumnAqData(11)
                .setColumnArData(12)
                .build();
        var list = ConvertedListV2.init()
                .setExcelListName("Test")
                .setHeadersV2(List.of("h1", "h2", "h3"))
                .setExcelListContentV2(List.of(clients))
                .build();
        return ConvertedBookV2.init().setBookName("TestBook").setBookV2(List.of(list)).build();
    }

    private ConvertedList convertedList() {
        var header = new ArrayList<String>();
        for (int i = 0; i < 35; i++) {
            header.add("h" + i);
        }
        var data = strings(
                "A", "15.01.2025", "C", "D", "1", "F", "G", "H",
                "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
                "15.01.2025 10:00", "15.01.2025 11:00",
                "U", "V", "W", "1", "1.5", "2.5",
                "X", "X", "X", "X", "X", "1", "Y", "Y", "Y");
        return ConvertedList.init()
                .setExcelListName("Sheet1")
                .setExcelListContent(List.of(header, data))
                .build();
    }

    @Test
    void createXlsxV2_success() {
        assertThat(service.createXlsxV2(bookV2())).isNotNull();
    }

    @Test
    void createXlsx_success() {
        var book = ConvertedBook.init()
                .setBookName("TestBook")
                .setBook(List.of(convertedList()))
                .build();
        assertThat(service.createXlsx(book)).isNotNull();
    }

    @Test
    void createXlsx_badDate() {
        var header = new ArrayList<String>();
        for (int i = 0; i < 35; i++) {
            header.add("h" + i);
        }
        var data = strings("A", "not-a-date");
        var list = ConvertedList.init()
                .setExcelListName("Sheet1")
                .setExcelListContent(List.of(header, data))
                .build();
        var book = ConvertedBook.init().setBookName("TestBook").setBook(List.of(list)).build();
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.createXlsx(book))
                .isInstanceOf(com.example.advantumconverter.exception.ExcelGenerationException.class);
    }

    @Test
    void createXlsxV2_badDataCast() {
        var list = ConvertedListV2.init()
                .setExcelListName("Test")
                .setHeadersV2(List.of("h1"))
                .setExcelListContentV2(List.of(
                        com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataRsLentaV2.init().build()))
                .build();
        var book = ConvertedBookV2.init().setBookName("TestBook").setBookV2(List.of(list)).build();
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.createXlsxV2(book))
                .isInstanceOf(com.example.advantumconverter.exception.ExcelGenerationException.class);
    }
}
