package com.example.advantumconverter.model.pojo.converter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class ConvertedList {

    private String excelListName;

    private List<List<String>> excelListContent;

}
