package com.hkorea.skyisthelimit.dto.criteria;

import static com.hkorea.skyisthelimit.repository.predicate.NotificationPredicates.isRead;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.entity.QNotification;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "알림 조회 조건 (필터링, 검색, 정렬, 페이지네이션)")
public class NotificationCriteria implements PageableCriteria<QNotification> {

  @Schema(description = "알림 읽음 상태", example = "false")
  private Boolean isRead;

  @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
  private int page = 0;

  @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
  private int size = 10;

  @Schema(
      description = "정렬 기준 필드",
      allowableValues = {"id", "createdAt"},
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
    return isRead(isRead);
  }

  @Override
  public OrderSpecifier<?> toOrderSpecifier(QNotification notification) {

    boolean asc = "asc".equalsIgnoreCase(direction);

    return switch (sort) {
      case "id" -> asc ? notification.id.asc() : notification.id.desc();
      case "createdAt" -> asc ? notification.createdAt.asc() : notification.createdAt.desc();
      default -> throw new BusinessException(ErrorCode.UNSUPPORTED_SORT_FIELD);
    };
  }
}
