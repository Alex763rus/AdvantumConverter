package com.example.advantumconverter.model.pojo.converter.v2;

import com.example.advantumconverter.enums.ResultCode;
import com.example.advantumconverter.model.pojo.converter.ConvertedBookDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "init", setterPrefix = "set")
@ToString
public class ConvertedBookV2 implements ConvertedBookDto {

    private String bookName;

    private List<ConvertedListV2> bookV2;

    private String message;

    private ResultCode resultCode;
}
