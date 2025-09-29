package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

  Boolean existsByUsername(String username);

  Boolean existsByNickname(String nickname);

  Optional<Member> findByUsername(String username);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByOauth2Username(String oauth2Username);

  @Query("SELECT COUNT(m) + 1 " +
      "FROM Member m " +
      "WHERE m.score > :score")
  Integer findRanking(@Param("score") int score);

}
