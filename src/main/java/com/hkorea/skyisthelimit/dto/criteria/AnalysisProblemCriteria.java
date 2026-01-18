package com.hkorea.skyisthelimit.dto.criteria;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.hasAnyTag;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.levelBetween;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.solvedCountBetween;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.statusIn;

import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class AnalysisProblemCriteria implements Criteria<QMemberProblem>{

  @Schema(description = "문제 최소 레벨", example = "1")
  private Integer levelStart;

  @Schema(description = "문제 최대 레벨", example = "10")
  private Integer levelEnd;

  @Schema(description = "태그 필터 (다중 선택 가능)", example = "[\"dp\", \"graph\"]")
  private List<String> tags;


  @Override
  public BooleanExpression toPredicate() {
    return levelBetween(levelStart, levelEnd)
        .and(statusIn(List.of(MemberProblemStatus.MULTI_TRY,MemberProblemStatus.UNSOLVED)))
        .and(hasAnyTag(tags));
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QMemberProblem entity) {
    return null;
  }
  
}
