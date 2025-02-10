package com.example.advantumconverter.service.rest.out.crm.impl;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.example.advantumconverter.model.rest.out.CrmGatewayReisResponseDto;
import com.example.advantumconverter.model.rest.out.CrmGatewayResponseDto;
import com.example.advantumconverter.service.rest.out.authentication.CrmAuthenticationHelper;
import com.example.advantumconverter.service.rest.out.crm.CrmHelper;
import com.example.advantumconverter.service.rest.out.crm.CrmService;
import com.example.advantumconverter.service.rest.out.exception.CrmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.tgcommons.constant.Constant.TextConstants.NEW_LINE;

@Component
public class CrmHelperImpl implements CrmHelper {

    private static final Logger log = LoggerFactory.getLogger(CrmHelperImpl.class);

    @Autowired
    private CrmAuthenticationHelper crmAuthenticationHelper;

    @Autowired
    protected CrmService crmService;

    @Override
    public CrmGatewayResponseDto sendDocument(List<RouteWithDictionaryDto> reises, CrmConfigProperties.CrmCreds crmCreds) {
 /*
          1) если токена нет, то идем за токеном
            -если успешно, идем дальше
            -если ошибка, вовзращаем ошибку
          2) токен был или получен, пробуем отправить файл:
            -если 401, то надо обновить токен
            -если если другая ошибка, то вовзращаем ее текстом, конец.
            -если успех, то вовзращаем успех, конец.
          3) если токен есть, пробуем отправить. Если 401, идем за токеном
            -если ок, пробуем отправить файл
            -если 401, то пробуем получить токен
            -если другая ошибка, то вовзращаем ее текстом, конец.
            -если успех, то вовзращаем успех, конец.
         */
        // 1) если токена нет, то идем за токеном
        var accessToken = crmAuthenticationHelper.getOrCreateAccessToken(crmCreds);
        if (!accessToken.isSuccess()) {
            return CrmGatewayResponseDto.of("Не смогли получить токен: " + accessToken.getMessage());
        }
        //2) токен был или получен, пробуем отправить файл:
        List<CrmGatewayReisResponseDto> reisSuccess = new ArrayList<>();
        List<CrmGatewayReisResponseDto> reisError = new ArrayList<>();
        for (var reis : reises) {
            var crmGatewayReisResponseDto = sendReis(reis, crmCreds, accessToken.getAccessToken());
            if (crmGatewayReisResponseDto.isSuccess()) {
                reisSuccess.add(crmGatewayReisResponseDto);
            } else {
                reisError.add(crmGatewayReisResponseDto);
            }
        }
        StringBuilder messsage = new StringBuilder();
        if (reisError.isEmpty()) {
            messsage.append("Все рейсы загружены успешно!").append(NEW_LINE)
                    .append("Количество: ").append(reisSuccess.size()).append(NEW_LINE)
                    .append("ExternalId: ").append(reisSuccess.stream()
                            .map(CrmGatewayReisResponseDto::getExternalId)
                            .collect(Collectors.joining(", ")));
        } else {
            messsage.append("Ошибка, не все рейсы загружены успешно!").append(NEW_LINE)
                    .append("Успешно загружены: ").append(reisSuccess.size()).append(NEW_LINE)
                    .append("Ошибка во время загрузки: ").append(reisError.size()).append(NEW_LINE)
                    .append("Детализация ошибок: ").append(NEW_LINE);
            reisError.forEach(reis -> messsage.append(reis.getExternalId()).append(" : ").append(reis.getMessage()).append(NEW_LINE));
        }
        return CrmGatewayResponseDto.of(messsage.toString(), reisSuccess, reisError);
    }

    private CrmGatewayReisResponseDto sendReis(RouteWithDictionaryDto document,
                                               CrmConfigProperties.CrmCreds crmCreds,
                                               String accessToken) {
        var sendDocumentResult = sendDocument(accessToken, document);
        //непонятная ошибка, попробуем еще раз:
        if (!sendDocumentResult.isSuccess() && sendDocumentResult.getCode() == null) {
            sendDocumentResult = sendDocument(accessToken, document);
        }
        if (sendDocumentResult.isSuccess() ||
                (sendDocumentResult.getCode() != null && sendDocumentResult.getCode() != 401)) {
            return sendDocumentResult;
        }
        //значит 401 ошибка, нужно обновить токен:
        var refreshTokenResult = crmAuthenticationHelper.refreshAndGetAccessToken(crmCreds);
        if (refreshTokenResult.isSuccess()) {
            //токен обновлен, пробуем отправку
            return sendDocument(accessToken, document);
        }
        return CrmGatewayReisResponseDto.ofError(document.getExternalId(),
                refreshTokenResult.getCode(), "Ошибка при обновлении токена:" + refreshTokenResult.getMessage());
    }

    private CrmGatewayReisResponseDto sendDocument(String accessToken, RouteWithDictionaryDto routeWithDictionaryDto) {
        try {
            crmService.routeAndDictionary(accessToken, routeWithDictionaryDto);
            return CrmGatewayReisResponseDto.ofSuccess(routeWithDictionaryDto.getExternalId());
        } catch (CrmException ex) {
            log.error("Ошибка при отправке документа: " + ex.getMessage());
            return CrmGatewayReisResponseDto.ofError(routeWithDictionaryDto.getExternalId(),
                    ex.getHttpStatusCode().value(), ex.getMessage());
        } catch (Exception ex) {
            log.error("Неожиданная ошибка при попытке отправки документа: " + ex.getMessage());
            return CrmGatewayReisResponseDto.ofError(routeWithDictionaryDto.getExternalId(), null,
                    "Неожиданная ошибка при попытке отправки документа: " + ex.getMessage());
        }
    }

}
