package com.hkorea.skyisthelimit.dto.auth.request;

import com.hkorea.skyisthelimit.entity.Register;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class RegisterRequest {

  @Schema(
      title = "클라이언트 이름",
      description = "OAuth 클라이언트의 이름",
      example = "skyisthelimit"
  )
  private String clientName;

  @Schema(
      title = "리다이렉트 URI",
      description = "인증 후 사용자를 리다이렉트할 URI",
      example = "http://localhost:5000/callback.html"
  )
  private String redirectUris;

  @Schema(
      title = "로그아웃 후 리다이렉트 URI",
      description = "사용자가 로그아웃한 후 이동할 URI",
      example = "https://example.com/logout-success"
  )
  private String postLogoutRedirectUris;


  @Schema(
      title = "스코프",
      description = "클라이언트가 요청할 권한 범위",
      example = "profile"
  )
  private String scopes;


  public Register toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
    return Register.builder()
        .id(UUID.randomUUID().toString())
        .clientId("a") // 하드 코딩 -> 추후에 변경
        .clientIdIssuedAt(Instant.now())
        .clientSecret(bCryptPasswordEncoder.encode("a")) // 하드 코딩 -> 추후에 변경
        .clientAuthenticationMethods("client_secret_basic")
        .authorizationGrantTypes("refresh_token,authorization_code,client_credentials")
        .clientName(this.clientName)
        .redirectUris(this.redirectUris)
        .postLogoutRedirectUris(this.postLogoutRedirectUris)
        .scopes(this.scopes)
        .clientSettings(
            "{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}")
        .tokenSettings(
            "{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.x509-certificate-bound-access-tokens\":false,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}")
        .build();
  }
}
