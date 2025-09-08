package com.hkorea.skyisthelimit.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

  @Schema(
      title = "클라이언트 ID",
      description = "OAuth 클라이언트 ID",
      example = "c6879d62-fe7b-4c0d-b5d0-f721df86b172"
  )
  private String clientId;

  @Schema(
      title = "클라이언트 Secret",
      description = "OAuth 클라이언트의 Secret",
      example = "c6879d62-fe7b-4c0d-b5d0-f721df86b172"
  )
  private String clientSecret;

  @Schema(
      title = "클라이언트 이름",
      description = "OAuth 클라이언트의 이름",
      example = "skyisthelimit"
  )
  private String clientName;

  @Schema(
      title = "클라이언트 인증 방법",
      description = "OAuth 클라이언트에서 지원하는 인증 방법(예: 'client_secret_basic', 'client_secret_post' 등)",
      example = "client_secret_basic"
  )
  private String clientAuthenticationMethods;

  @Schema(
      title = "리디렉션 URI",
      description = "OAuth 클라이언트가 인증 후 리디렉션될 URI",
      example = "https://yourapp.com/callback"
  )
  private String redirectUris;

  @Schema(
      title = "로그아웃 후 리디렉션 URI",
      description = "사용자가 로그아웃 후 리디렉션될 URI 목록",
      example = "https://yourapp.com/logout"
  )
  private String postLogoutRedirectUris;

  @Schema(
      title = "스코프",
      description = "OAuth 클라이언트에서 요구하는 권한 스코프",
      example = "read write"
  )
  private String scopes;

}
