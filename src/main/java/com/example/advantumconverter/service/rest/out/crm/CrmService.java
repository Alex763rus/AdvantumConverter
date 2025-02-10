package com.example.advantumconverter.service.rest.out.crm;

import com.example.advantumconverter.gen.model.RouteWithDictionaryDto;

public interface CrmService {

    void routeAndDictionary(String accessToken, RouteWithDictionaryDto request);

}
