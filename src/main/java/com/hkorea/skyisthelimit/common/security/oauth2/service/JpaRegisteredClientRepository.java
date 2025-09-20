package com.hkorea.skyisthelimit.common.security.oauth2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.common.security.oauth2.entity.Register;
import com.hkorea.skyisthelimit.common.security.oauth2.repository.RegisterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

  private final RegisterRepository registerRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 생성자 - Spring Security 관련 직렬화/역직렬화 모듈(ObjectMapper 모듈) 등록
   */
  public JpaRegisteredClientRepository(RegisterRepository registerRepository) {
    this.registerRepository = registerRepository;

    ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
    List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
    this.objectMapper.registerModules(securityModules);
    this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
  }

  /**
   * 문자열 → AuthorizationGrantType 변환 - DB에 저장된 문자열을 OAuth2 표준 AuthorizationGrantType 객체로 변환
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
    }
    return new AuthorizationGrantType(authorizationGrantType);
  }

  /**
   * 문자열 → ClientAuthenticationMethod 변환 - DB에 저장된 문자열을 OAuth2 표준 ClientAuthenticationMethod 객체로 변환
   */
  private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
      String clientAuthenticationMethod) {
    if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_POST;
    } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.NONE;
    }
    return new ClientAuthenticationMethod(clientAuthenticationMethod);
  }

  /**
   * RegisteredClient 저장 - RegisteredClient 객체를 Register 엔티티로 변환하여 DB에 저장
   */
  @Override
  public void save(RegisteredClient registeredClient) {
    registerRepository.save(toEntity(registeredClient));
  }

  /**
   * ID 값으로 RegisteredClient 조회 - Register 엔티티를 찾아 RegisteredClient 객체로 변환하여 반환
   */
  @Override
  public RegisteredClient findById(String id) {
    return registerRepository.findById(id).map(this::toObject).orElse(null);
  }

  /**
   * clientId 값으로 RegisteredClient 조회 - Register 엔티티를 찾아 RegisteredClient 객체로 변환하여 반환
   */
  @Override
  public RegisteredClient findByClientId(String clientId) {
    return registerRepository.findByClientId(clientId).map(this::toObject).orElse(null);
  }

  /**
   * Register 엔티티 → RegisteredClient 객체 변환 - DB에 저장된 String/JSON 값들을 Set, Map 등으로 변환 -
   * RegisteredClient.Builder 를 통해 RegisteredClient 생성
   */
  private RegisteredClient toObject(Register entity) {
    Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(
        entity.getClientAuthenticationMethods());
    Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(
        entity.getAuthorizationGrantTypes());
    Set<String> redirectUris = StringUtils.commaDelimitedListToSet(entity.getRedirectUris());
    Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(
        entity.getPostLogoutRedirectUris());
    Set<String> clientScopes = StringUtils.commaDelimitedListToSet(entity.getScopes());

    RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
        .clientId(entity.getClientId())
        .clientIdIssuedAt(entity.getClientIdIssuedAt())
        .clientSecret(entity.getClientSecret())
        .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
        .clientName(entity.getClientName())
        .clientAuthenticationMethods(authenticationMethods ->
            clientAuthenticationMethods.forEach(authenticationMethod ->
                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
        .authorizationGrantTypes((grantTypes) ->
            authorizationGrantTypes.forEach(grantType ->
                grantTypes.add(resolveAuthorizationGrantType(grantType))))
        .redirectUris((uris) -> uris.addAll(redirectUris))
        .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
        .scopes((scopes) -> scopes.addAll(clientScopes));

    Map<String, Object> clientSettingsMap = parseMap(entity.getClientSettings());
    builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

    Map<String, Object> tokenSettingsMap = parseMap(entity.getTokenSettings());
    builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

    return builder.build();
  }

  /**
   * RegisteredClient 객체 → Register 엔티티 변환 - Set/Collection → String (콤마 구분) - Map → JSON 문자열 - DB
   * 저장 가능하도록 변환
   */
  private Register toEntity(RegisteredClient registeredClient) {
    List<String> clientAuthenticationMethods = new ArrayList<>(
        registeredClient.getClientAuthenticationMethods().size());
    registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
        clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

    List<String> authorizationGrantTypes = new ArrayList<>(
        registeredClient.getAuthorizationGrantTypes().size());
    registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
        authorizationGrantTypes.add(authorizationGrantType.getValue()));

    Register entity = new Register();
    entity.setId(registeredClient.getId());
    entity.setClientId(registeredClient.getClientId());
    entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
    entity.setClientSecret(registeredClient.getClientSecret());
    entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
    entity.setClientName(registeredClient.getClientName());
    entity.setClientAuthenticationMethods(
        StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
    entity.setAuthorizationGrantTypes(
        StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
    entity.setRedirectUris(
        StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
    entity.setPostLogoutRedirectUris(
        StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
    entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
    entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
    entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

    return entity;
  }

  /**
   * JSON 문자열 → Map 변환 (역직렬화) - DB에 저장된 JSON 문자열을 Map 형태로 변환
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
   * Map → JSON 문자열 변환 (직렬화) - 객체(Map)를 문자열로 변환하여 DB에 저장 가능하도록 처리
   */
  private String writeMap(Map<String, Object> data) {
    try {
      return this.objectMapper.writeValueAsString(data);
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }
}
