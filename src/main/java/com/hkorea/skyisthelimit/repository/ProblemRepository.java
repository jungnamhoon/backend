package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

  boolean existsByBaekjoonId(Integer baekjoonId);

  boolean existsByTitle(String title);
}