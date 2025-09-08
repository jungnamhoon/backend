package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.auth.response.RegisterResponse;
import com.hkorea.skyisthelimit.entity.Register;

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
