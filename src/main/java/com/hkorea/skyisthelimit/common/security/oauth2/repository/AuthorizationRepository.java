package com.hkorea.skyisthelimit.common.security.oauth2.repository;

import com.hkorea.skyisthelimit.common.security.oauth2.entity.Authorization;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorizationRepository extends JpaRepository<Authorization, String> {

  Optional<Authorization> findByState(
      String state);                                      // state 값으로 조회 (CSRF 방지용 state 확인)

  Optional<Authorization> findByAuthorizationCodeValue(
      String authorizationCode);        // authorization_code 값으로 조회 (코드 → 토큰 교환 시 사용)

  Optional<Authorization> findByAccessTokenValue(
      String accessToken);                    // access_token 값으로 조회 (리소스 접근 시 토큰 검증)

  Optional<Authorization> findByRefreshTokenValue(
      String refreshToken);                  // refresh_token 값으로 조회 (토큰 재발급 시 사용)

  Optional<Authorization> findByOidcIdTokenValue(
      String idToken);                       // OIDC id_token 값으로 조회 (OIDC 인증 시 사용)

  Optional<Authorization> findByUserCodeValue(
      String userCode);                         // user_code 값으로 조회 (Device Flow에서 사용)

  Optional<Authorization> findByDeviceCodeValue(
      String deviceCode);                     // device_code 값으로 조회 (Device Flow에서 사용)

  @Query("select a from Authorization a where a.state = :token" +
      " or a.authorizationCodeValue = :token" +
      " or a.accessTokenValue = :token" +
      " or a.refreshTokenValue = :token" +
      " or a.oidcIdTokenValue = :token" +
      " or a.userCodeValue = :token" +
      " or a.deviceCodeValue = :token"
  )
  Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
      @Param("token") String token);

}

