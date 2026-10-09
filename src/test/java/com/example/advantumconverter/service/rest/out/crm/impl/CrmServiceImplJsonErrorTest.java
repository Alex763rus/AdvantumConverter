package com.example.advantumconverter.service.rest.out.crm.impl;

import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CrmServiceImplJsonErrorTest {

    @Test
    void routeAndDictionary_jsonSerializationFails_logsAndContinues() throws Exception {
        var server = new MockWebServer();
        server.start();
        server.enqueue(new MockResponse().setResponseCode(200));
        try {
            var service = new CrmServiceImpl();
            ReflectionTestUtils.setField(service, "webClient",
                    WebClient.builder().baseUrl(server.url("/").toString()).build());
            var mapper = mock(ObjectMapper.class);
            when(mapper.writeValueAsString(any()))
                    .thenThrow(new JsonProcessingException("boom") {
                    });
            ReflectionTestUtils.setField(service, "objectMapper", mapper);

            service.routeAndDictionary("tok", RouteWithDictionaryDto.init().setExternalId("EXT").build());
        } finally {
            server.shutdown();
        }
    }
}
