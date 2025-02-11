package com.example.advantumconverter.config;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Autowired
    private CrmConfigProperties crmConfigProperties;

    @Bean
    public WebClient crmWebClient() {
        return WebClient.builder()
                .baseUrl(crmConfigProperties.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .clientConnector(new ReactorClientHttpConnector(createHttpClient(getSslContextTrustAll())))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Отключить вывод времени как метки времени
        mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS); // Разрешить символы управления без кавычек
        return mapper;
    }

    private HttpClient createHttpClient(SslContext sslContext) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, crmConfigProperties.getConnectTimeoutMillis())
                .secure(t -> t.sslContext(sslContext))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(crmConfigProperties.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(crmConfigProperties.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS));
                });
    }

    private SslContext getSslContextTrustAll() {
        try {
            return SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (Exception e) {
            log.error("Error initializing SSLContext for HTTPS requests: {}", e.getMessage(), e);
            return null;
        }
    }
}