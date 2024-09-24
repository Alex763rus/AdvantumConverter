package com.example.advantumconverter.model.pojo.converter.v2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class ConvertedListV2 {

    private String excelListName;

    private List<String> headersV2;

    private List<ConvertedListDataV2> excelListContentV2;

}
