package com.hkorea.skyisthelimit.repository.predicate;

import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.util.List;

public class MemberProblemPredicates {

  public static BooleanExpression usernameEq(String username) {
    if (username == null) {
      return null;
    }
    return QMemberProblem.memberProblem.member.username.eq(username);
  }

  public static BooleanExpression statusIn(List<MemberProblemStatus> statuses) {
    if (statuses == null || statuses.isEmpty()) {
      return Expressions.asBoolean(true).isTrue();
    }
    return QMemberProblem.memberProblem.status.in(statuses);
  }

  public static BooleanExpression solvedCountLoe(Integer solvedCount) {
    if (solvedCount == null) {
      return Expressions.asBoolean(true).isTrue();
    }
    return QMemberProblem.memberProblem.solvedCount.loe(solvedCount);
  }

  public static BooleanExpression noteWrittenEq(Boolean noteWritten) {
    if (noteWritten == null) {
      return Expressions.asBoolean(true).isTrue();
    }
    return QMemberProblem.memberProblem.noteWritten.eq(noteWritten);
  }


  public static BooleanExpression levelBetween(Integer levelStart, Integer levelEnd) {
    if (levelStart == null && levelEnd == null) {
      return Expressions.asBoolean(true).isTrue();
    } else if (levelStart != null && levelEnd != null) {
      return QMemberProblem.memberProblem.problem.level.between(levelStart, levelEnd);
    } else if (levelStart != null) {
      return QMemberProblem.memberProblem.problem.level.goe(levelStart);
    } else {
      return QMemberProblem.memberProblem.problem.level.loe(levelEnd);
    }
  }

  public static BooleanExpression hasAnyTag(List<String> tagKoNames) {

    if (tagKoNames == null || tagKoNames.isEmpty()) {
      return Expressions.asBoolean(true).isTrue();
    }

    BooleanExpression predicate = null;
    for (String tagKoName : tagKoNames) {
      BooleanExpression singleTag = hasTagKoName(tagKoName);
      predicate = (predicate == null) ? singleTag : predicate.or(singleTag);
    }
    return predicate;
  }

  public static BooleanExpression hasTagKoName(String tagKoName) {
    if (tagKoName == null || tagKoName.isBlank()) {
      return Expressions.asBoolean(true).isTrue();
    }
    return QMemberProblem.memberProblem.problem.problemTagList.any().koName.eq(tagKoName);
  }


  public static BooleanExpression solvedCountBetween(Integer start, Integer end) {
    if (start == null && end == null) {
      return Expressions.asBoolean(true).isTrue();
    } else if (start != null && end != null) {
      return QMemberProblem.memberProblem.solvedCount.between(start, end);
    } else if (start != null) {
      return QMemberProblem.memberProblem.solvedCount.goe(start);
    } else {
      return QMemberProblem.memberProblem.solvedCount.loe(end);
    }
  }

  public static BooleanExpression solvedAtBetween(LocalDate start,
      LocalDate end) {
    if (start == null && end == null) {
      return Expressions.asBoolean(true).isTrue();
    } else if (start != null && end != null) {
      return QMemberProblem.memberProblem.solvedDate.between(start, end);
    } else if (start != null) {
      return QMemberProblem.memberProblem.solvedDate.goe(start);
    } else {
      return QMemberProblem.memberProblem.solvedDate.loe(end);
    }
  }

  public static BooleanExpression search(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return Expressions.asBoolean(true).isTrue();
    }

    QMemberProblem q = QMemberProblem.memberProblem;

    return q.problem.title.containsIgnoreCase(keyword)
        .or(q.problem.baekjoonId.stringValue().containsIgnoreCase(keyword));
  }
}
