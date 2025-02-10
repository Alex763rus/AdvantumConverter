package com.example.advantumconverter.model.rest.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class CrmGatewayReisResponseDto {

    private String externalId;
    private boolean isSuccess;
    private Integer code;
    private String message;

    public static CrmGatewayReisResponseDto ofSuccess(final String externalId) {
        return CrmGatewayReisResponseDto.init()
                .setExternalId(externalId)
                .setIsSuccess(true)
                .build();
    }

    public static CrmGatewayReisResponseDto ofError(final String externalId, Integer code, String message) {
        return CrmGatewayReisResponseDto.init()
                .setExternalId(externalId)
                .setIsSuccess(false)
                .setCode(code)
                .setMessage(message)
                .build();
    }

}
