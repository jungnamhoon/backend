package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

  Boolean existsByUsername(String username);

  Boolean existsByNickname(String nickname);

  Optional<Member> findByUsername(String username);
}
