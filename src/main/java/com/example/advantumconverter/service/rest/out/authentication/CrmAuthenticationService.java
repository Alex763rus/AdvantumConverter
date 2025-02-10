package com.example.advantumconverter.service.rest.out.authentication;

import com.example.advantumconverter.model.rest.out.OpenConnectResponseDto;

public interface CrmAuthenticationService {

    OpenConnectResponseDto openConnect(String login, String password);

    OpenConnectResponseDto refreshConnect(String refreshToken);

}
