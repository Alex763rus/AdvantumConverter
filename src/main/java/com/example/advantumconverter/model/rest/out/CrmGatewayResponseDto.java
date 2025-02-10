package com.example.advantumconverter.model.rest.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class CrmGatewayResponseDto {

    private boolean isSuccessful;
    private String message;
    private List<CrmGatewayReisResponseDto> reisSuccess = new ArrayList<>();
    private List<CrmGatewayReisResponseDto> reisError = new ArrayList<>();


    public static CrmGatewayResponseDto of(String message) {
        return CrmGatewayResponseDto
                .init()
                .setIsSuccessful(false)
                .setMessage(message)
                .build();
    }

    public static CrmGatewayResponseDto of(String message,
                                           List<CrmGatewayReisResponseDto> reisSuccess
            , List<CrmGatewayReisResponseDto> reisError) {
        return CrmGatewayResponseDto
                .init()
                .setIsSuccessful(true)
                .setMessage(message)
                .setReisSuccess(reisSuccess)
                .setReisError(reisError)
                .build();
    }
}
