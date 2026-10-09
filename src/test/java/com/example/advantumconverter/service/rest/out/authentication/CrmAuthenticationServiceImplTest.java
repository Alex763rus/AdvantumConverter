package com.example.advantumconverter.service.rest.out.authentication;

import com.example.advantumconverter.model.rest.out.OpenConnectResponseDto;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import com.example.advantumconverter.support.AbstractIntegrationTest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CrmAuthenticationServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private CrmAuthenticationService crmAuthenticationService;

    @BeforeEach
    void setUp() throws Exception {
        crmMockServer.takeRequest(1, TimeUnit.MILLISECONDS);
    }

    @Test
    void openConnect_success() throws Exception {
        crmMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"access\",\"refresh_token\":\"refresh\"}"));

        OpenConnectResponseDto response = crmAuthenticationService.openConnect("user", "pass");

        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
        var recorded = crmMockServer.takeRequest(5, TimeUnit.SECONDS);
        assertThat(recorded.getPath()).isEqualTo("/keycloak/auth/realms/atms/protocol/openid-connect/token");
        assertThat(recorded.getBody().readUtf8()).isEqualTo(
                "client_id=atms-user-account&username=user&password=pass&grant_type=password");
    }

    @Test
    void openConnect_badRequest_throwsCrmException() {
        crmMockServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("bad"));

        assertThatThrownBy(() -> crmAuthenticationService.openConnect("user", "pass"))
                .isInstanceOf(CrmException.class)
                .hasMessageContaining("Ошибка во время запроса на получение токена");
    }

    @Test
    void openConnect_errorInBody_logsError() {
        crmMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"error\":\"invalid_grant\",\"error_description\":\"bad creds\"}"));

        OpenConnectResponseDto response = crmAuthenticationService.openConnect("user", "wrong");

        assertThat(response.getError()).isEqualTo("invalid_grant");
    }

    @Test
    void refreshConnect_success() throws Exception {        crmMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"a2\",\"refresh_token\":\"r2\"}"));

        OpenConnectResponseDto response = crmAuthenticationService.refreshConnect("refresh");

        assertThat(response.getAccessToken()).isEqualTo("a2");
        var recorded = crmMockServer.takeRequest(5, TimeUnit.SECONDS);
        assertThat(recorded.getBody().readUtf8()).isEqualTo(
                "client_id=atms-user-account&refresh_token=refresh&grant_type=refresh_token");
    }

    @Test
    void refreshConnect_badRequest_throwsCrmException() {
        crmMockServer.enqueue(new MockResponse().setResponseCode(400).setBody("bad"));

        assertThatThrownBy(() -> crmAuthenticationService.refreshConnect("refresh"))
                .isInstanceOf(CrmException.class)
                .hasMessageContaining("Ошибка во время запроса на обновление токена");
    }

    @Test
    void refreshConnect_errorInBody_logsError() {
        crmMockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"error\":\"invalid_grant\",\"error_description\":\"expired\"}"));

        OpenConnectResponseDto response = crmAuthenticationService.refreshConnect("expired");

        assertThat(response.getErrorDescription()).isEqualTo("expired");
    }
}
