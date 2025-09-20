package com.hkorea.skyisthelimit.common.security.oauth2.repository;

import com.hkorea.skyisthelimit.common.security.oauth2.entity.Consent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentRepository extends JpaRepository<Consent, String> {

  Optional<Consent> findByRegisteredClientIdAndPrincipalName(String registeredClientId,
      String principalName);

  void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
