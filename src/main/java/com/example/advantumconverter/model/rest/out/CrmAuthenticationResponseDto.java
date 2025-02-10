package com.example.advantumconverter.model.rest.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class CrmAuthenticationResponseDto {

    private boolean success;

    private String accessToken;

    private String refreshToken;

    private String message;

    private int code;

    public static CrmAuthenticationResponseDto ofSuccess(String accessToken, String refreshToken) {
        return CrmAuthenticationResponseDto.init()
                .setSuccess(true)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .build();
    }

    public static CrmAuthenticationResponseDto ofError(String message, int code) {
        return CrmAuthenticationResponseDto.init()
                .setSuccess(false)
                .setMessage(message)
                .setCode(code)
                .build();
    }

    public static CrmAuthenticationResponseDto ofError(String message) {
        return CrmAuthenticationResponseDto.init()
                .setSuccess(false)
                .setMessage(message)
                .build();
    }

}
