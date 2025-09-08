package com.hkorea.skyisthelimit.dto.criteria;

import static com.hkorea.skyisthelimit.repository.predicate.StudyPredicates.levelBetween;
import static com.hkorea.skyisthelimit.repository.predicate.StudyPredicates.search;
import static com.hkorea.skyisthelimit.repository.predicate.StudyPredicates.statusIn;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.entity.QStudy;
import com.hkorea.skyisthelimit.entity.enums.StudyStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "스터디 조회 조건 (필터링, 검색, 정렬, 페이지네이션)")
public class StudyCriteria implements PageableCriteria<QStudy> {

  @Schema(description = "스터디 최소 레벨(포함)", example = "1")
  private Integer minLevel;

  @Schema(description = "스터디 최대 레벨(포함)", example = "10")
  private Integer maxLevel;

  @Schema(description = "스터디 상태 필터 (다중 선택 가능)", example = "BEFORE_START")
  private List<StudyStatus> status;

  @Schema(description = "검색어 (스터디명)", example = "백준 스터디")
  private String search;

  @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
  private int page = 0;

  @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
  private int size = 10;

  @Schema(
      description = "정렬 기준 필드",
      allowableValues = {"id", "minLevel", "maxLevel", "startDate", "endDate"},
      example = "id",
      defaultValue = "id"
  )
  private String sort = "id";


  @Schema(
      description = "정렬 방향",
      allowableValues = {"asc", "desc"},
      example = "asc",
      defaultValue = "asc"
  )
  private String direction = "asc";

  @Override
  public BooleanExpression toPredicate() {
    return levelBetween(minLevel, maxLevel)
        .and(statusIn(status))
        .and(search(search));
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QStudy study) {
    boolean asc = "asc".equalsIgnoreCase(getDirection());

    return switch (sort) {
      case "id" -> asc ? study.id.asc() : study.id.desc();
      case "minLevel" -> asc ? study.minLevel.asc() : study.minLevel.desc();
      case "maxLevel" -> asc ? study.maxLevel.asc() : study.maxLevel.desc();
      case "startDate" -> asc ? study.startDate.asc() : study.startDate.desc();
      case "endDate" -> asc ? study.endDate.asc() : study.endDate.desc();
      default -> throw new BusinessException(ErrorCode.UNSUPPORTED_SORT_FIELD);
    };
  }

}
