package com.hkorea.skyisthelimit.analysis.dto.request;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.*;

import com.hkorea.skyisthelimit.analysis.entity.QWeakness;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.entity.QProblem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisCriteria implements Criteria<QProblem> {

  @Schema(description = "문제 최소 레벨", example = "1")
  private Integer levelStart;

  @Schema(description = "문제 최대 레벨", example = "10")
  private Integer levelEnd;

  @Schema(description = "태그 필터 (다중 선택 가능)", example = "[\"dp\", \"graph\"]")
  private List<String> tags;

  @Override
  public BooleanExpression toPredicate() {
    return levelBetween(levelStart, levelEnd)
        .and(hasAnyTag(tags));
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QProblem entity) {
    return null;
  }
}
