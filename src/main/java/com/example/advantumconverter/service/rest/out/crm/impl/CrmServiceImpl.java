package com.example.advantumconverter.service.rest.out.crm.impl;

import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.example.advantumconverter.service.rest.out.crm.CrmService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;

@Component
@Slf4j
public class CrmServiceImpl implements CrmService {

    private final static String ROUTE_AND_DICTIONARY_PATH = "/public/routes/route-and-dictionary";

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void routeAndDictionary(String accessToken, RouteWithDictionaryDto request) {
        try {
            log.info(String.format("Старт: отправка документа, externalId= %s, тело: %s",
                    request.getExternalId(), objectMapper.writeValueAsString(request)));
        } catch (JsonProcessingException e) {
            log.error("Ошибка. Не удалось преобразовать объект в json: " + e.getMessage());
            log.info("Старт: отправка документа: " + request.toString());
        }
        webClient
                .post()
                .uri(String.join(EMPTY, ROUTE_AND_DICTIONARY_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(e -> e.add("Authorization", "Bearer " + accessToken))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> log.info("Успех: файл загружен"))
                .doOnError(error -> {
                            log.error("Ошибка во время отправки файла {}", error.getMessage());
                            var statusCode = ((WebClientResponseException) error).getStatusCode();
                            throw new CrmException("Ошибка во время отправки файла: " + ((WebClientResponseException.BadRequest) error).getResponseBodyAsString(), statusCode);
                        }
                )
                .block();
    }
}
