package com.hkorea.skyisthelimit.common.security.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "oauth2_consent")
@IdClass(Consent.AuthorizationConsentId.class)
@Getter
@Setter
public class Consent {

  @Id
  private String registeredClientId;
  @Id
  private String principalName; // 로그인 시 username
  @Column(length = 1000)
  private String authorities;


  public static class AuthorizationConsentId implements Serializable {

    private String registeredClientId;
    private String principalName;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      AuthorizationConsentId that = (AuthorizationConsentId) o;
      return registeredClientId.equals(that.registeredClientId) && principalName.equals(
          that.principalName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(registeredClientId, principalName);
    }
  }
}
