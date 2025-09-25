package com.hkorea.skyisthelimit.repository.predicate;

import com.hkorea.skyisthelimit.entity.QStudy;
import com.hkorea.skyisthelimit.entity.enums.StudyStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.util.List;

public class StudyPredicates {

  private static final QStudy study = QStudy.study;

  public static BooleanExpression levelBetween(Integer minLevel, Integer maxLevel) {
    if (minLevel == null && maxLevel == null) {
      return Expressions.asBoolean(true).isTrue();
    } else if (minLevel != null && maxLevel != null) {
      return study.minLevel.goe(minLevel).and(study.maxLevel.loe(maxLevel));
    } else if (minLevel != null) {
      return study.minLevel.goe(minLevel);
    } else {
      return study.maxLevel.loe(maxLevel);
    }
  }

  public static BooleanExpression statusIn(List<StudyStatus> statuses) {
    if (statuses == null || statuses.isEmpty()) {
      return Expressions.asBoolean(true).isTrue();
    }

    LocalDate today = LocalDate.now();

    BooleanExpression expr = null;

    for (StudyStatus status : statuses) {
      BooleanExpression condition = switch (status) {
        case BEFORE_START -> study.startDate.gt(today);
        case ONGOING -> study.startDate.loe(today).and(study.endDate.goe(today));
        case ENDED -> study.endDate.lt(today);
      };

      expr = expr == null ? condition : expr.or(condition);
    }

    return expr;
  }

  public static BooleanExpression search(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return Expressions.asBoolean(true).isTrue();
    }
    return study.name.containsIgnoreCase(keyword);
  }
}
