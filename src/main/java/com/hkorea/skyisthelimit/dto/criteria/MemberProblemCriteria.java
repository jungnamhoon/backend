package com.hkorea.skyisthelimit.dto.criteria;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.levelBetween;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.noteWrittenEq;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.search;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.solvedCountLoe;
import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.statusIn;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class MemberProblemCriteria implements PageableCriteria<QMemberProblem> {

  // filter
  @Schema(description = "조회할 문제 레벨의 시작 범위(포함)", example = "1")
  private Integer levelStart;

  @Schema(description = "조회할 문제 레벨의 끝 범위(포함)", example = "10")
  private Integer levelEnd;

  @Schema(
      description = "문제 상태 필터 (다중 선택 가능)", example = "SOLVED"
  )
  private List<MemberProblemStatus> status;

  @Schema(description = "해결 횟수 이하 필터", example = "5")
  private Integer solvedCount;

  @Schema(description = "노트 작성 여부", example = "true")
  private Boolean noteWritten;

  // search
  @Schema(description = "문제 제목 또는 문제 id 검색어", example = "A+B")
  private String search;

  // sort & pagination

  @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
  private int page = 0;

  @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
  private int size = 10;

  @Schema(description = "정렬 기준 필드",
      allowableValues = {"baekjoonId", "level", "solvedDate", "solvedCount"},
      example = "level")
  private String sort = "baekjoonId";

  @Schema(description = "정렬 방향", allowableValues = {"asc",
      "desc"}, example = "asc", defaultValue = "asc")
  private String direction = "asc";

  @Override
  public BooleanExpression toPredicate() {
    return levelBetween(levelStart, levelEnd)
        .and(statusIn(status))
        .and(solvedCountLoe(solvedCount))
        .and(noteWrittenEq(noteWritten))
        .and(search(search));
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QMemberProblem memberProblem) {
    boolean asc = "asc".equalsIgnoreCase(direction);
    return switch (sort) {
      case "baekjoonId" -> asc ? memberProblem.problem.baekjoonId.asc()
          : memberProblem.problem.baekjoonId.desc();
      case "level" -> asc ? memberProblem.problem.level.asc()
          : memberProblem.problem.level.desc();
      case "solvedAt" -> asc ? memberProblem.solvedDate.asc()
          : memberProblem.solvedDate.desc();
      case "solvedCount" -> asc ? memberProblem.solvedCount.asc()
          : memberProblem.solvedCount.desc();
      default -> throw new BusinessException(ErrorCode.UNSUPPORTED_SORT_FIELD);
    };
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
  }

}
