package com.example.advantumconverter.service.rest.out.authentication;

import com.example.advantumconverter.config.properties.CrmConfigProperties;
import com.example.advantumconverter.model.rest.out.CrmAuthenticationResponseDto;

public interface CrmAuthenticationHelper {

    CrmAuthenticationResponseDto getOrCreateAccessToken(CrmConfigProperties.CrmCreds crmCreds);

    CrmAuthenticationResponseDto refreshAndGetAccessToken(CrmConfigProperties.CrmCreds crmCreds);
}
