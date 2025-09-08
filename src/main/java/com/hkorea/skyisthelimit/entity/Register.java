package com.hkorea.skyisthelimit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oauth2_register")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Register {

  @Id
  private String id;

  private String clientId;                                // OAuth2 클라이언트 식별자
  private Instant clientIdIssuedAt;                       // clientId 발급 시각
  private String clientSecret;                            // OAuth2 클라이언트 비밀키
  private Instant clientSecretExpiresAt;                  // clientSecret 만료 시각
  private String clientName;                              // 클라이언트 이름

  @Column(length = 1000)
  private String clientAuthenticationMethods;            // 클라이언트 인증 방식 (예: client_secret_basic, client_secret_post)

  @Column(length = 1000)
  private String authorizationGrantTypes;                // 클라이언트가 어떤식으로 AccessToken을 받을지 (예: authorization_code, client_credentials)

  @Column(length = 1000)
  private String redirectUris;                           // OAuth2 리다이렉트 URI

  @Column(length = 1000)
  private String postLogoutRedirectUris;                 // 로그아웃 후 리다이렉트 URI

  @Column(length = 1000)
  private String scopes;                                 // 클라이언트가 요청할 수 있는 권한 범위 (profile)

  @Column(length = 2000)
  private String clientSettings;                         // 클라이언트 설정(JSON 형태로 저장 가능, 예: requireProofKey, requireAuthorizationConsent)

  @Column(length = 2000)
  private String tokenSettings;                          // 토큰 설정(JSON 형태로 저장 가능, 예: accessTokenTimeToLive, refreshTokenTimeToLive)
}
