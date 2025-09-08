package com.hkorea.skyisthelimit.common.utils;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryDSLHelper {

  private final JPAQueryFactory queryFactory;

  public <T> List<T> fetchEntities(
      EntityPathBase<T> qEntity,
      BooleanExpression predicate,
      OrderSpecifier<?> orderSpecifier,
      Pageable pageable
  ) {
    return queryFactory
        .selectFrom(qEntity)
        .where(predicate)
        .orderBy(orderSpecifier)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  public <T> List<T> fetchEntities(
      EntityPathBase<T> qEntity,
      BooleanExpression predicate
  ) {
    return queryFactory
        .selectFrom(qEntity)
        .where(predicate)
        .fetch();
  }

  public <T> long fetchTotalCount(
      EntityPathBase<T> qEntity,
      BooleanExpression predicate
  ) {
    Long totalCount = queryFactory
        .select(qEntity.count())
        .from(qEntity)
        .where(predicate)
        .fetchOne();
    return totalCount != null ? totalCount : 0L;
  }
}
