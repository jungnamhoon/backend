package com.hkorea.skyisthelimit.common.security.oauth2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.common.security.oauth2.entity.Authorization;
import com.hkorea.skyisthelimit.common.security.oauth2.repository.AuthorizationRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

  private final AuthorizationRepository authorizationRepository;
  private final RegisteredClientRepository registeredClientRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 생성자 - Spring Security 관련 직렬화/역직렬화 모듈(ObjectMapper 모듈) 등록
   */
  public JpaOAuth2AuthorizationService(AuthorizationRepository authorizationRepository,
      RegisteredClientRepository registeredClientRepository) {
    this.authorizationRepository = authorizationRepository;
    this.registeredClientRepository = registeredClientRepository;

    ClassLoader classLoader = JpaOAuth2AuthorizationService.class.getClassLoader();
    List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
    this.objectMapper.registerModules(securityModules);
    this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
  }

  /**
   * 문자열 값으로 권한 부여 타입(AuthorizationGrantType)을 확인 - 주어진 문자열에 해당하는 OAuth2 AuthorizationGrantType 객체를
   * 반환
   */
  private static AuthorizationGrantType resolveAuthorizationGrantType(
      String authorizationGrantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()
        .equals(authorizationGrantType)) {
      return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.REFRESH_TOKEN;
    } else if (AuthorizationGrantType.DEVICE_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.DEVICE_CODE;
    }
    return new AuthorizationGrantType(authorizationGrantType);
  }

  /**
   * OAuth2Authorization 저장 - OAuth2Authorization 객체를 Authorization 엔티티로 변환하여 DB에 저장
   */
  @Override
  public void save(OAuth2Authorization authorization) {
    authorizationRepository.save(toEntity(authorization));
  }

  /**
   * OAuth2Authorization 제거 - OAuth2Authorization 객체의 ID를 사용하여 DB에서 제거
   */
  @Override
  public void remove(OAuth2Authorization authorization) {
    authorizationRepository.deleteById(authorization.getId());
  }

  /**
   * ID로 OAuth2Authorization 조회 - 주어진 ID에 해당하는 Authorization 엔티티를 찾아 OAuth2Authorization 객체로 변환
   */
  @Override
  public OAuth2Authorization findById(String id) {
    return authorizationRepository.findById(id).map(this::toObject).orElse(null);
  }

  /**
   * 토큰으로 OAuth2Authorization 조회 - 주어진 토큰 값과 토큰 타입에 따라 DB에서 Authorization 엔티티를 찾아
   * OAuth2Authorization 객체로 변환
   */
  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Optional<Authorization> result;
    if (tokenType == null) {
      result = this.authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
          token);
    } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByState(token);
    } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByAuthorizationCodeValue(token);
    } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByAccessTokenValue(token);
    } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByRefreshTokenValue(token);
    } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByOidcIdTokenValue(token);
    } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByUserCodeValue(token);
    } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
      result = this.authorizationRepository.findByDeviceCodeValue(token);
    } else {
      result = Optional.empty();
    }

    return result.map(this::toObject).orElse(null);
  }

  /**
   * Authorization 엔티티를 OAuth2Authorization 객체로 변환 - DB에서 조회한 엔티티 데이터를 기반으로 OAuth2Authorization 객체를
   * 재구성
   */
  private OAuth2Authorization toObject(Authorization entity) {
    RegisteredClient registeredClient = this.registeredClientRepository.findById(
        entity.getRegisteredClientId());
    if (registeredClient == null) {
      throw new DataRetrievalFailureException(
          "The RegisteredClient with id '" + entity.getRegisteredClientId()
              + "' was not found in the RegisteredClientRepository.");
    }

    OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
        .id(entity.getId())
        .principalName(entity.getPrincipalName())
        .authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
        .authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
        .attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));
    if (entity.getState() != null) {
      builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
    }

    if (entity.getAuthorizationCodeValue() != null) {
      OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
          entity.getAuthorizationCodeValue(),
          entity.getAuthorizationCodeIssuedAt(),
          entity.getAuthorizationCodeExpiresAt());
      builder.token(authorizationCode,
          metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
    }

    if (entity.getAccessTokenValue() != null) {
      OAuth2AccessToken accessToken = new OAuth2AccessToken(
          OAuth2AccessToken.TokenType.BEARER,
          entity.getAccessTokenValue(),
          entity.getAccessTokenIssuedAt(),
          entity.getAccessTokenExpiresAt(),
          StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
      builder.token(accessToken,
          metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
    }

    if (entity.getRefreshTokenValue() != null) {
      OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
          entity.getRefreshTokenValue(),
          entity.getRefreshTokenIssuedAt(),
          entity.getRefreshTokenExpiresAt());
      builder.token(refreshToken,
          metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
    }

    if (entity.getOidcIdTokenValue() != null) {
      OidcIdToken idToken = new OidcIdToken(
          entity.getOidcIdTokenValue(),
          entity.getOidcIdTokenIssuedAt(),
          entity.getOidcIdTokenExpiresAt(),
          parseMap(entity.getOidcIdTokenClaims()));
      builder.token(idToken,
          metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
    }

    if (entity.getUserCodeValue() != null) {
      OAuth2UserCode userCode = new OAuth2UserCode(
          entity.getUserCodeValue(),
          entity.getUserCodeIssuedAt(),
          entity.getUserCodeExpiresAt());
      builder.token(userCode, metadata -> metadata.putAll(parseMap(entity.getUserCodeMetadata())));
    }

    if (entity.getDeviceCodeValue() != null) {
      OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
          entity.getDeviceCodeValue(),
          entity.getDeviceCodeIssuedAt(),
          entity.getDeviceCodeExpiresAt());
      builder.token(deviceCode,
          metadata -> metadata.putAll(parseMap(entity.getDeviceCodeMetadata())));
    }

    return builder.build();
  }

  /**
   * OAuth2Authorization 객체를 Authorization 엔티티로 변환 - OAuth2Authorization 객체의 데이터를 DB 저장을 위해
   * Authorization 엔티티로 매핑
   */
  private Authorization toEntity(OAuth2Authorization authorization) {
    Authorization entity = new Authorization();
    entity.setId(authorization.getId());
    entity.setRegisteredClientId(authorization.getRegisteredClientId());
    entity.setPrincipalName(authorization.getPrincipalName());
    entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
    entity.setAuthorizedScopes(
        StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
    entity.setAttributes(writeMap(authorization.getAttributes()));
    entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

    OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
        authorization.getToken(OAuth2AuthorizationCode.class);
    setTokenValues(
        authorizationCode,
        entity::setAuthorizationCodeValue,
        entity::setAuthorizationCodeIssuedAt,
        entity::setAuthorizationCodeExpiresAt,
        entity::setAuthorizationCodeMetadata
    );

    OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
        authorization.getToken(OAuth2AccessToken.class);
    setTokenValues(
        accessToken,
        entity::setAccessTokenValue,
        entity::setAccessTokenIssuedAt,
        entity::setAccessTokenExpiresAt,
        entity::setAccessTokenMetadata
    );
    if (accessToken != null && accessToken.getToken().getScopes() != null) {
      entity.setAccessTokenScopes(
          StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
    }

    OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
        authorization.getToken(OAuth2RefreshToken.class);
    setTokenValues(
        refreshToken,
        entity::setRefreshTokenValue,
        entity::setRefreshTokenIssuedAt,
        entity::setRefreshTokenExpiresAt,
        entity::setRefreshTokenMetadata
    );

    OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
        authorization.getToken(OidcIdToken.class);
    setTokenValues(
        oidcIdToken,
        entity::setOidcIdTokenValue,
        entity::setOidcIdTokenIssuedAt,
        entity::setOidcIdTokenExpiresAt,
        entity::setOidcIdTokenMetadata
    );
    if (oidcIdToken != null) {
      entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
    }

    OAuth2Authorization.Token<OAuth2UserCode> userCode =
        authorization.getToken(OAuth2UserCode.class);
    setTokenValues(
        userCode,
        entity::setUserCodeValue,
        entity::setUserCodeIssuedAt,
        entity::setUserCodeExpiresAt,
        entity::setUserCodeMetadata
    );

    OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
        authorization.getToken(OAuth2DeviceCode.class);
    setTokenValues(
        deviceCode,
        entity::setDeviceCodeValue,
        entity::setDeviceCodeIssuedAt,
        entity::setDeviceCodeExpiresAt,
        entity::setDeviceCodeMetadata
    );

    return entity;
  }

  /**
   * 토큰 정보를 Authorization 엔티티에 설정 - 토큰 값, 발행 시각, 만료 시각, 메타데이터를 엔티티의 필드에 설정
   */
  private void setTokenValues(
      OAuth2Authorization.Token<?> token,
      Consumer<String> tokenValueConsumer,
      Consumer<Instant> issuedAtConsumer,
      Consumer<Instant> expiresAtConsumer,
      Consumer<String> metadataConsumer) {
    if (token != null) {
      OAuth2Token oAuth2Token = token.getToken();
      tokenValueConsumer.accept(oAuth2Token.getTokenValue());
      issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
      expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
      metadataConsumer.accept(writeMap(token.getMetadata()));
    }
  }

  /**
   * JSON 문자열을 Map<String, Object>으로 역직렬화 - JSON 형식의 데이터를 Java Map 객체로 변환
   */
  private Map<String, Object> parseMap(String data) {
    try {
      return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }

  /**
   * Map<String, Object>을 JSON 문자열로 직렬화 - Java Map 객체를 JSON 형식의 문자열로 변환
   */
  private String writeMap(Map<String, Object> metadata) {
    try {
      return this.objectMapper.writeValueAsString(metadata);
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }
}