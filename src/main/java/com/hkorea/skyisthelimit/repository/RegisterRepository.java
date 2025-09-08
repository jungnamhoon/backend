package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Register;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, String> {

  Optional<Register> findByClientId(String clientId);
}