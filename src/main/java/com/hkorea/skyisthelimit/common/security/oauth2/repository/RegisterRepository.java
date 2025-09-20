package com.hkorea.skyisthelimit.common.security.oauth2.repository;

import com.hkorea.skyisthelimit.common.security.oauth2.entity.Register;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, String> {

  Optional<Register> findByClientId(String clientId);
}