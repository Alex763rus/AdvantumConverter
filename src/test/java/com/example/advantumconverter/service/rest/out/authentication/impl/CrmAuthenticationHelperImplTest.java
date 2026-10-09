package com.example.advantumconverter.service.rest.out.authentication.impl;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.model.rest.out.CrmAuthenticationResponseDto;
import com.example.advantumconverter.model.rest.out.OpenConnectResponseDto;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CrmAuthenticationHelperImplTest {

    @Mock
    private CrmAuthenticationService crmAuthenticationService;

    @InjectMocks
    private CrmAuthenticationHelperImpl helper;

    private CrmConfigProperties.CrmCreds creds() {
        var creds = new CrmConfigProperties.CrmCreds();
        creds.setLogin("login");
        creds.setPassword("password");
        return creds;
    }

    private OpenConnectResponseDto token(String access, String refresh) {
        return OpenConnectResponseDto.init()
                .setAccessToken(access)
                .setRefreshToken(refresh)
                .build();
    }

    @Test
    void getOrCreate_success_cachesToken() {
        when(crmAuthenticationService.openConnect("login", "password")).thenReturn(token("a1", "r1"));

        var first = helper.getOrCreateAccessToken(creds());
        var second = helper.getOrCreateAccessToken(creds());

        assertThat(first.isSuccess()).isTrue();
        assertThat(first.getAccessToken()).isEqualTo("a1");
        assertThat(second.getAccessToken()).isEqualTo("a1");
        verify(crmAuthenticationService, times(1)).openConnect("login", "password");
    }

    @Test
    void getOrCreate_crmException_returnsErrorWithCode() {
        when(crmAuthenticationService.openConnect("login", "password"))
                .thenThrow(new CrmException("boom", HttpStatus.BAD_REQUEST));

        var result = helper.getOrCreateAccessToken(creds());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getCode()).isEqualTo(400);
        assertThat(result.getMessage()).contains("Ошибка при попытке получения токена: boom");
    }

    @Test
    void getOrCreate_unexpectedException_returnsError() {
        when(crmAuthenticationService.openConnect("login", "password"))
                .thenThrow(new RuntimeException("unexpected"));

        var result = helper.getOrCreateAccessToken(creds());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("Неожиданная ошибка при попытке получения токена: unexpected");
    }

    @Test
    void refresh_noToken_createsNewToken() {
        when(crmAuthenticationService.openConnect("login", "password")).thenReturn(token("a1", "r1"));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getAccessToken()).isEqualTo("a1");
    }

    @Test
    void refresh_refreshSuccess_returnsRefreshedToken() {
        when(crmAuthenticationService.openConnect("login", "password")).thenReturn(token("a1", "r1"));
        helper.getOrCreateAccessToken(creds());
        when(crmAuthenticationService.refreshConnect("r1")).thenReturn(token("a2", "r2"));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getAccessToken()).isEqualTo("a2");
    }

    @Test
    void refresh_refresh400_thenRecreateSuccess() {
        when(crmAuthenticationService.openConnect("login", "password"))
                .thenReturn(token("a1", "r1"), token("a3", "r3"));
        helper.getOrCreateAccessToken(creds());
        when(crmAuthenticationService.refreshConnect("r1"))
                .thenThrow(new CrmException("bad refresh", HttpStatus.BAD_REQUEST));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getAccessToken()).isEqualTo("a3");
    }

    @Test
    void refresh_refresh400_recreateFails_returnsError() {
        when(crmAuthenticationService.openConnect("login", "password"))
                .thenReturn(token("a1", "r1"))
                .thenThrow(new CrmException("recreate fail", HttpStatus.INTERNAL_SERVER_ERROR));
        helper.getOrCreateAccessToken(creds());
        when(crmAuthenticationService.refreshConnect("r1"))
                .thenThrow(new CrmException("bad refresh", HttpStatus.BAD_REQUEST));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("recreate fail");
    }

    @Test
    void refresh_refreshNon400Error_returnsRefreshError() {
        when(crmAuthenticationService.openConnect("login", "password")).thenReturn(token("a1", "r1"));
        helper.getOrCreateAccessToken(creds());
        when(crmAuthenticationService.refreshConnect("r1"))
                .thenThrow(new CrmException("server error", HttpStatus.INTERNAL_SERVER_ERROR));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getCode()).isEqualTo(500);
        assertThat(result.getMessage()).contains("Ошибка при попытке обновлении токена: server error");
    }

    @Test
    void refresh_unexpectedException_returnsError() {
        when(crmAuthenticationService.openConnect("login", "password")).thenReturn(token("a1", "r1"));
        helper.getOrCreateAccessToken(creds());
        when(crmAuthenticationService.refreshConnect("r1")).thenThrow(new RuntimeException("boom"));

        var result = helper.refreshAndGetAccessToken(creds());

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("Неожиданная ошибка при попытке обновления токена: boom");
    }
}
