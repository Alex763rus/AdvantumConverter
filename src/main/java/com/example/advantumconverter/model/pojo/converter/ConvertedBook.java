package com.example.advantumconverter.model.pojo.converter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
public class ConvertedBook {

    private String bookName;

    private List<ConvertedList> book;

    private String message;
}
