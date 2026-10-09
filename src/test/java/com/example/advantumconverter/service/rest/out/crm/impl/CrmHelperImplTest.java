package com.example.advantumconverter.service.rest.out.crm.impl;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.example.advantumconverter.model.rest.out.CrmAuthenticationResponseDto;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationHelper;
import com.example.advantumconverter.service.rest.out.crm.CrmService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CrmHelperImplTest {

    @Mock
    private CrmAuthenticationHelper crmAuthenticationHelper;

    @Mock
    private CrmService crmService;

    @InjectMocks
    private CrmHelperImpl helper;

    private CrmConfigProperties.CrmCreds creds() {
        var creds = new CrmConfigProperties.CrmCreds();
        creds.setLogin("login");
        creds.setPassword("password");
        return creds;
    }

    private RouteWithDictionaryDto reis(String externalId) {
        return RouteWithDictionaryDto.init().setExternalId(externalId).build();
    }

    @Test
    void sendDocument_tokenError_returnsMessage() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofError("no token", 500));

        var result = helper.sendDocument(List.of(reis("e1")), creds());

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.getMessage()).contains("Не смогли получить токен: no token");
        verify(crmService, never()).routeAndDictionary(any(), any());
    }

    @Test
    void sendDocument_allSuccess_returnsSuccessMessage() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));

        var result = helper.sendDocument(List.of(reis("e1"), reis("e2")), creds());

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getReisSuccess()).hasSize(2);
        assertThat(result.getReisError()).isEmpty();
        assertThat(result.getMessage()).contains("Все рейсы загружены успешно!");
        assertThat(result.getMessage()).contains("e1, e2");
    }

    @Test
    void sendDocument_someError_returnsErrorDetails() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));
        doThrow(new CrmException("bad", HttpStatus.INTERNAL_SERVER_ERROR))
                .when(crmService).routeAndDictionary(eq("a1"), eq(reis("e2")));

        var result = helper.sendDocument(List.of(reis("e1"), reis("e2")), creds());

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getReisSuccess()).hasSize(1);
        assertThat(result.getReisError()).hasSize(1);
        assertThat(result.getMessage()).contains("Ошибка, не все рейсы загружены успешно!");
        assertThat(result.getMessage()).contains("e2 : bad");
    }

    @Test
    void sendDocument_401_refreshSuccess_retriesAndSucceeds() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));
        doThrow(new CrmException("unauthorized", HttpStatus.UNAUTHORIZED))
                .doNothing()
                .when(crmService).routeAndDictionary(any(), any());
        when(crmAuthenticationHelper.refreshAndGetAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a2", "r2"));

        var result = helper.sendDocument(List.of(reis("e1")), creds());

        assertThat(result.getReisSuccess()).hasSize(1);
        assertThat(result.getReisError()).isEmpty();
        verify(crmAuthenticationHelper, times(1)).refreshAndGetAccessToken(any());
    }

    @Test
    void sendDocument_401_refreshFail_returnsError() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));
        doThrow(new CrmException("unauthorized", HttpStatus.UNAUTHORIZED))
                .when(crmService).routeAndDictionary(any(), any());
        when(crmAuthenticationHelper.refreshAndGetAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofError("refresh fail", 500));

        var result = helper.sendDocument(List.of(reis("e1")), creds());

        assertThat(result.getReisError()).hasSize(1);
        assertThat(result.getMessage()).contains("Ошибка при обновлении токена:refresh fail");
    }

    @Test
    void sendDocument_nonAuthHttpError_returnsWithoutRefresh() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));
        doThrow(new CrmException("server", HttpStatus.INTERNAL_SERVER_ERROR))
                .when(crmService).routeAndDictionary(any(), any());

        var result = helper.sendDocument(List.of(reis("e1")), creds());

        assertThat(result.getReisError()).hasSize(1);
        verify(crmAuthenticationHelper, never()).refreshAndGetAccessToken(any());
    }

    @Test
    void sendDocument_genericException_retried_thenRefreshFail() {
        when(crmAuthenticationHelper.getOrCreateAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofSuccess("a1", "r1"));
        doThrow(new RuntimeException("unexpected"))
                .when(crmService).routeAndDictionary(any(), any());
        when(crmAuthenticationHelper.refreshAndGetAccessToken(any()))
                .thenReturn(CrmAuthenticationResponseDto.ofError("refresh fail", 500));

        var result = helper.sendDocument(List.of(reis("e1")), creds());

        assertThat(result.getReisError()).hasSize(1);
        verify(crmService, times(2)).routeAndDictionary(any(), any());
    }
}
