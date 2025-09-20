package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.common.security.oauth2.entity.Register;
import com.hkorea.skyisthelimit.dto.auth.response.RegisterResponse;

public class AuthMapper {

  public static RegisterResponse toRegisterResponse(Register register) {

    return RegisterResponse.builder()
        .clientId(register.getClientId())
        .clientSecret(register.getClientSecret())
        .clientName(register.getClientName())
        .clientAuthenticationMethods(register.getClientAuthenticationMethods())
        .redirectUris(register.getRedirectUris())
        .postLogoutRedirectUris(register.getPostLogoutRedirectUris())
        .scopes(register.getScopes())
        .build();
  }
}
