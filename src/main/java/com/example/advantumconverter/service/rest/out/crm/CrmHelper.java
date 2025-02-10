package com.example.advantumconverter.service.rest.out.crm;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;
import com.example.advantumconverter.model.pojo.converter.ConvertedBook;
import com.example.advantumconverter.model.pojo.converter.v2.ConvertedBookV2;
import com.example.advantumconverter.model.rest.out.CrmGatewayResponseDto;

import java.util.List;

public interface CrmHelper {

    CrmGatewayResponseDto sendDocument(List<RouteWithDictionaryDto> reises, CrmConfigProperties.CrmCreds crmCreds);

}
