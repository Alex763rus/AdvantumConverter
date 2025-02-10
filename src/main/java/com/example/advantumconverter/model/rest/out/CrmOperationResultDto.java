package com.example.advantumconverter.model.rest.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@AllArgsConstructor
public class CrmOperationResultDto {

    private boolean success;
    private String message;
    private int code;

}
