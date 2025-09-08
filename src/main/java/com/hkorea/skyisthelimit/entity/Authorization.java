package com.hkorea.skyisthelimit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "oauth2_authorization")
@Getter
@Setter
public class Authorization {

  @Id
  @Column
  private String id;                           // 고유 ID (PK)
  private String registeredClientId;           // 등록된 클라이언트 ID (어떤 클라이언트가 요청했는지)
  private String principalName;                // 인증된 사용자 이름 (username)
  private String authorizationGrantType;       // 인가 방식 (authorization_code, client_credentials 등)

  @Column(columnDefinition = "TEXT")
  private String authorizedScopes;             // 허용된 Scope 목록 (ex: profile,email)
  @Column(columnDefinition = "TEXT")
  private String attributes;                   // 인증 관련 부가 정보(JSON으로 직렬화됨)
  @Column(length = 500)
  private String state;                        // CSRF 방지용 state 값

  // ----- Authorization Code -----
  @Column(columnDefinition = "TEXT")
  private String authorizationCodeValue;       // 발급된 Authorization Code 값
  private Instant authorizationCodeIssuedAt;   // 코드 발급 시각
  private Instant authorizationCodeExpiresAt;  // 코드 만료 시각
  private String authorizationCodeMetadata;    // 코드 관련 메타데이터(JSON)

  // ----- Access Token -----
  @Column(columnDefinition = "TEXT")
  private String accessTokenValue;             // Access Token 문자열
  private Instant accessTokenIssuedAt;         // Access Token 발급 시각
  private Instant accessTokenExpiresAt;        // Access Token 만료 시각
  @Column(length = 2000)
  private String accessTokenMetadata;          // Access Token 관련 메타데이터(JSON)
  private String accessTokenType;              // 토큰 타입 (Bearer 등)
  @Column(length = 1000)
  private String accessTokenScopes;            // Access Token에 포함된 Scope들

  // ----- Refresh Token -----
  @Column(columnDefinition = "TEXT")
  private String refreshTokenValue;            // Refresh Token 문자열
  private Instant refreshTokenIssuedAt;        // Refresh Token 발급 시각
  private Instant refreshTokenExpiresAt;       // Refresh Token 만료 시각
  @Column(length = 2000)
  private String refreshTokenMetadata;         // Refresh Token 관련 메타데이터(JSON)

  // ----- OIDC ID Token -----
  @Column(columnDefinition = "TEXT")
  private String oidcIdTokenValue;             // OIDC ID Token 값 (사용자 인증 정보 포함)
  private Instant oidcIdTokenIssuedAt;         // ID Token 발급 시각
  private Instant oidcIdTokenExpiresAt;        // ID Token 만료 시각
  @Column(length = 2000)
  private String oidcIdTokenMetadata;          // ID Token 메타데이터
  @Column(length = 2000)
  private String oidcIdTokenClaims;            // ID Token 안의 Claims(JSON: sub, email 등)

  // ----- Device/User Code (Device Authorization Flow) -----
  @Column(columnDefinition = "TEXT")
  private String userCodeValue;                // User Code 값
  private Instant userCodeIssuedAt;            // User Code 발급 시각
  private Instant userCodeExpiresAt;           // User Code 만료 시각
  @Column(length = 2000)
  private String userCodeMetadata;             // User Code 메타데이터

  @Column(columnDefinition = "TEXT")
  private String deviceCodeValue;              // Device Code 값
  private Instant deviceCodeIssuedAt;          // Device Code 발급 시각
  private Instant deviceCodeExpiresAt;         // Device Code 만료 시각
  @Column(length = 2000)
  private String deviceCodeMetadata;           // Device Code 메타데이터
}
