package com.example.advantumconverter.service.rest.out.crm.impl;

import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.example.advantumconverter.service.rest.out.crm.CrmService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import com.example.advantumconverter.support.AbstractIntegrationTest;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CrmServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private CrmService crmService;

    private RouteWithDictionaryDto request() {
        return RouteWithDictionaryDto.init().setExternalId("EXT-1").build();
    }

    @Test
    void routeAndDictionary_success_sendsAuthorizedRequest() throws Exception {
        crmMockServer.enqueue(new MockResponse().setResponseCode(200));

        crmService.routeAndDictionary("my-token", request());

        var recorded = crmMockServer.takeRequest(5, TimeUnit.SECONDS);
        assertThat(recorded.getPath()).isEqualTo("/public/routes/route-and-dictionary");
        assertThat(recorded.getHeader("Authorization")).isEqualTo("Bearer my-token");
        assertThat(recorded.getBody().readUtf8()).contains("EXT-1");
    }

    @Test
    void routeAndDictionary_badRequest_throwsCrmException() {
        crmMockServer.enqueue(new MockResponse().setResponseCode(400).setBody("bad request"));

        assertThatThrownBy(() -> crmService.routeAndDictionary("tok", request()))
                .isInstanceOf(CrmException.class)
                .hasMessageContaining("Ошибка во время отправки файла: bad request");
    }
}
