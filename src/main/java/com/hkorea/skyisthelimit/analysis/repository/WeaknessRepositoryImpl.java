package com.hkorea.skyisthelimit.analysis.repository;

import static com.hkorea.skyisthelimit.analysis.entity.QWeakness.weakness;
import static com.hkorea.skyisthelimit.analysis.entity.QWrongReason.wrongReason;
import static com.hkorea.skyisthelimit.entity.QMemberProblem.memberProblem;
import static com.hkorea.skyisthelimit.entity.QProblem.problem;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.entity.QProblem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeaknessRepositoryImpl implements WeaknessRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Weakness> findTopWeaknesses(String username, Criteria<QProblem> criteria,
      int limit) {
    return queryFactory
        .select(weakness)
        .from(weakness)
        .join(weakness.wrongReasons, wrongReason)
        .join(wrongReason.memberProblem, memberProblem)
        .join(memberProblem.problem,problem)
        .where(
            memberProblem.member.username.eq(username),
            criteria.toPredicate()
        )
        .distinct()
        .orderBy(weakness.frequency.desc())
        .limit(limit)
        .fetch();
  }
}
