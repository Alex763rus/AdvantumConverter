package com.example.advantumconverter.service.rest.out.authentication.impl;

import com.example.advantumconverter.model.rest.out.OpenConnectResponseDto;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;

@Component
@Slf4j
public class CrmAuthenticationServiceImpl implements CrmAuthenticationService {

    private final static String OPEN_AND_REFRESH_CONNECT_PATH = "/keycloak/auth/realms/atms/protocol/openid-connect/token";

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OpenConnectResponseDto openConnect(String login, String password) {
//        var body = "client_id=atms-user-account&username=ozon-test&password=76gwv76v&grant_type=password";
        StringBuilder body = new StringBuilder();
        body.append("client_id=atms-user-account&username=");
        body.append(login);
        body.append("&password=");
        body.append(password);
        body.append("&grant_type=password");

        log.info("Старт: отправка запроса на получение токена: " + body);
        return webClient
                .post()
                .uri(String.join(EMPTY, OPEN_AND_REFRESH_CONNECT_PATH))
                .bodyValue(body)
                .headers(e -> e.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .retrieve()
                .bodyToMono(OpenConnectResponseDto.class)
                .doOnSuccess(response -> {
                    if (response.getError() == null) {
                        log.info("Успех: токен получен");
                    } else {
                        log.error(String.format("Ошибка во время получения токена: %s, описание: %s",
                                response.getError(), response.getErrorDescription()));
                    }
                })
                .doOnError(error -> {
                            log.error("Ошибка во время запроса на получение токена {}", error.getMessage());
                            var statusCode = ((WebClientResponseException) error).getStatusCode();
                            throw new CrmException("Ошибка во время запроса на получение токена: " + ((WebClientResponseException.BadRequest) error).getResponseBodyAsString(), statusCode);
                        }
                )
                .block();
    }

    @Override
    public OpenConnectResponseDto refreshConnect(String refreshToken) {
        StringBuilder body = new StringBuilder();
        body.append("client_id=atms-user-account&refresh_token=");
        body.append(refreshToken);
        body.append("&grant_type=refresh_token");
        log.info("Старт: отправка запроса на обновление токена");
        return webClient
                .post()
                .uri(String.join(EMPTY, OPEN_AND_REFRESH_CONNECT_PATH))
                .bodyValue(body)
                .headers(e -> e.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .retrieve()
                .bodyToMono(OpenConnectResponseDto.class)
                .doOnSuccess(response -> {
                    if (response.getError() == null) {
                        log.info("Успех: токен обновлен");
                    } else {
                        log.error(String.format("Ошибка во время обновления токена: %s, описание: %s",
                                response.getError(), response.getErrorDescription()));
                    }
                })
                .doOnError(error -> {
                            log.error("Ошибка во время запроса на обновление токена {}", error.getMessage());
                            var statusCode = ((WebClientResponseException) error).getStatusCode();
                            throw new CrmException("Ошибка во время запроса на обновление токена: " + ((WebClientResponseException.BadRequest) error).getResponseBodyAsString(), statusCode);
                        }
                )
                .block();
    }

}
