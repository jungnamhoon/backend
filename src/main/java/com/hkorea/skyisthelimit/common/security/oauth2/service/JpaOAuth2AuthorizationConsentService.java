package com.hkorea.skyisthelimit.common.security.oauth2.service;

import com.hkorea.skyisthelimit.common.security.oauth2.entity.Consent;
import com.hkorea.skyisthelimit.common.security.oauth2.repository.ConsentRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

  private final ConsentRepository consentRepository;
  private final RegisteredClientRepository registeredClientRepository;

  public JpaOAuth2AuthorizationConsentService(ConsentRepository consentRepository,
      RegisteredClientRepository registeredClientRepository) {
    this.consentRepository = consentRepository;
    this.registeredClientRepository = registeredClientRepository;
  }

  @Override
  public void save(OAuth2AuthorizationConsent authorizationConsent) {
    consentRepository.save(toEntity(authorizationConsent));
  }

  @Override
  public void remove(OAuth2AuthorizationConsent authorizationConsent) {
    consentRepository.deleteByRegisteredClientIdAndPrincipalName(
        authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
  }

  @Override
  public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
    return consentRepository.findByRegisteredClientIdAndPrincipalName(registeredClientId,
        principalName).map(this::toObject).orElse(null);
  }

  private OAuth2AuthorizationConsent toObject(Consent authorizationConsent) {
    String registeredClientId = authorizationConsent.getRegisteredClientId();
    RegisteredClient registeredClient = this.registeredClientRepository.findById(
        registeredClientId);
    if (registeredClient == null) {
      throw new DataRetrievalFailureException(
          "The RegisteredClient with id '" + registeredClientId
              + "' was not found in the RegisteredClientRepository.");
    }

    OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
        registeredClientId, authorizationConsent.getPrincipalName());
    if (authorizationConsent.getAuthorities() != null) {
      for (String authority : StringUtils.commaDelimitedListToSet(
          authorizationConsent.getAuthorities())) {
        builder.authority(new SimpleGrantedAuthority(authority));
      }
    }

    return builder.build();
  }

  private Consent toEntity(OAuth2AuthorizationConsent authorizationConsent) {
    Consent entity = new Consent();
    entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
    entity.setPrincipalName(authorizationConsent.getPrincipalName());

    Set<String> authorities = new HashSet<>();
    for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
      authorities.add(authority.getAuthority());
    }
    entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

    return entity;
  }
}
