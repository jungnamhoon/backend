package com.hkorea.skyisthelimit.dto.criteria;

import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MemberProblemStatisticsCriteria implements Criteria<QMemberProblem> {

  @Schema(description = "조회할 연도", example = "2025")
  private Integer year;

  @Schema(description = "조회할 월 (1~12)", example = "9")
  private Integer month;

  @Override
  public BooleanExpression toPredicate() {

    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()).plusDays(1);

    return MemberProblemPredicates.solvedAtBetween(startDate, endDate);
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QMemberProblem entity) {
    return null;
  }
}
