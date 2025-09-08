package com.hkorea.skyisthelimit.dto.criteria;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface Criteria<T> {

  BooleanExpression toPredicate();

  OrderSpecifier<?> toOrderSpecifier(T entity);
}
