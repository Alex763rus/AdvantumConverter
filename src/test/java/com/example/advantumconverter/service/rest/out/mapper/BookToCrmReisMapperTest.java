package com.example.advantumconverter.service.rest.out.mapper;

import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListDataClientsV2;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedListV2;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookToCrmReisMapperTest {

    @Test
    void map_v1_notImplemented() {
        assertThatThrownBy(() -> BookToCrmReisMapper.map((ConvertedBook) null))
                .isInstanceOf(NotImplementedException.class);
    }

    @Test
    void map_v2_returnsEmptyWhenNoDocuments() {
        var convertedBook = ConvertedBookV2.init()
                .setBookV2(List.of(ConvertedListV2.init()
                        .setExcelListContentV2(List.of())
                        .build()))
                .build();

        assertThat(BookToCrmReisMapper.map(convertedBook)).isEmpty();
    }

    @Test
    void privateConstructor_throws() throws Exception {
        Constructor<BookToCrmReisMapper> constructor = BookToCrmReisMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThatThrownBy(constructor::newInstance).isInstanceOf(InvocationTargetException.class);
    }

    @Test
    void privateHelpers() {
        var point = ConvertedListDataClientsV2.init()
                .setColumnUdata("u")
                .setColumnVdata("v")
                .setColumnYdata(1.5)
                .setColumnZdata(2.5)
                .build();

        assertThat((Object) point.getColumnUdata()).isEqualTo("u");
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "preparePoint", point));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "prepareExternalOrganization", "n", "id"));
        org.junit.jupiter.api.Assertions.assertNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "prepareExternalVehicle", ""));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "prepareExternalVehicle", "X"));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "convert", new java.util.Date()));
        org.junit.jupiter.api.Assertions.assertNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "toBigDecimal", (Integer) null));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "toBigDecimal", Integer.valueOf(5)));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "toBigDecimal", Double.valueOf(5.5)));
        org.junit.jupiter.api.Assertions.assertNotNull(
                ReflectionTestUtils.invokeMethod(BookToCrmReisMapper.class, "toFloat", Integer.valueOf(5)));
    }
}
