package com.example.advantumconverter.service.rest.out.authentication.impl;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.model.rest.out.CrmAuthenticationResponseDto;
import com.example.advantumconverter.model.rest.out.CrmGatewayReisResponseDto;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationHelper;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CrmAuthenticationHelperImpl implements CrmAuthenticationHelper {

    @Autowired
    private CrmAuthenticationService crmAuthenticationService;

    private Map<CrmConfigProperties.CrmCreds, CrmAuthenticationResponseDto> tokens = new HashMap<>();

    @Override
    public CrmAuthenticationResponseDto getOrCreateAccessToken(CrmConfigProperties.CrmCreds crmCreds) {
        var token = tokens.get(crmCreds);
        if (token != null) {
            log.info("Успех: Токен найден среди существующих");
            return token;
        }
        log.info("Токен не найден среди существующих, будет попытка завести новый");
        tokens.remove(crmCreds);
        return createAndSaveNewToken(crmCreds);
    }
    @Override
    public CrmAuthenticationResponseDto refreshAndGetAccessToken(CrmConfigProperties.CrmCreds crmCreds) {
        var token = tokens.get(crmCreds);
        //токена не было, надо создать:
        if (token == null) {
            log.info(String.format("Обновление токена. Токена не было, пробуем создать. token: %s", token));
            return createAndSaveNewToken(crmCreds);
        }
        var refresh1 = refreshToken(token.getRefreshToken());

        if (refresh1.isSuccess()) {
            //токен обновлен
            tokens.put(crmCreds, refresh1);
            return refresh1;
        }
        //Токен обновить не получилось, получили 400 ошибку. Пробуем заново авторизоваться:
        if(refresh1.getCode() == 400){
            log.info(String.format("Обновление токена. Токен обновить не получилось получили 400 ошибку, пробуем создать еще раз. token: %s", token));
            var newToken = createNewToken(crmCreds);
            if(newToken.isSuccess()) {
                tokens.put(crmCreds, newToken);
            }
            //Ошибка после попытки повторной авторизации:
            return newToken;
        }
        return refresh1;
    }

    private CrmAuthenticationResponseDto createAndSaveNewToken(CrmConfigProperties.CrmCreds crmCreds){
        var crmAuthentication = createNewToken(crmCreds);
        if(crmAuthentication.isSuccess()){
            log.info("Успех: токен получен");
            tokens.put(crmCreds, crmAuthentication);
        }
        return crmAuthentication;
    }

    private CrmAuthenticationResponseDto createNewToken(CrmConfigProperties.CrmCreds crmCreds) {
        try {
            var response = crmAuthenticationService.openConnect(crmCreds.getLogin(), crmCreds.getPassword());
            return CrmAuthenticationResponseDto.ofSuccess(response.getAccessToken(), response.getRefreshToken());
        } catch (CrmException ex) {
            log.error("Ошибка при попытке получения токена: " + ex.getMessage());
            return CrmAuthenticationResponseDto.ofError(
                    "Ошибка при попытке получения токена: " + ex.getMessage(), ex.getHttpStatusCode().value());
        } catch (Exception ex) {
            log.error("Неожиданная ошибка при попытке получения токена: " + ex.getMessage());
            return CrmAuthenticationResponseDto.ofError("Неожиданная ошибка при попытке получения токена: " + ex.getMessage());
        }
    }

    private CrmAuthenticationResponseDto refreshToken(String refreshToken) {
        try {
            var response = crmAuthenticationService.refreshConnect(refreshToken);
            return CrmAuthenticationResponseDto.ofSuccess(response.getAccessToken(), response.getRefreshToken());
        } catch (CrmException ex) {
            log.error("Ошибка при попытке обновления токена: " + ex.getMessage());
            return CrmAuthenticationResponseDto.ofError(
                    "Ошибка при попытке обновлении токена: " + ex.getMessage(), ex.getHttpStatusCode().value());
        } catch (Exception ex) {
            log.error("Неожиданная ошибка при попытке обновления токена: " + ex.getMessage());
            return CrmAuthenticationResponseDto.ofError("Неожиданная ошибка при попытке обновления токена: " + ex.getMessage());
        }
    }
}
