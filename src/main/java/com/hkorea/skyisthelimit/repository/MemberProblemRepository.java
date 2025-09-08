package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.MemberProblem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProblemRepository extends JpaRepository<MemberProblem, Integer> {

  Optional<MemberProblem> findByMemberUsernameAndProblemBaekjoonId(String username,
      Integer baekjoonId);

  List<MemberProblem> findByMemberUsername(String username);
}
