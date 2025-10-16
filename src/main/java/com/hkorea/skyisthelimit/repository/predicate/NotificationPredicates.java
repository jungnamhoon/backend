package com.hkorea.skyisthelimit.repository.predicate;

import com.hkorea.skyisthelimit.entity.QNotification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

public class NotificationPredicates {

  private static final QNotification notification = QNotification.notification;

  public static BooleanExpression usernameEq(String username) {
    if (username == null) {
      return null;
    }

    return QNotification.notification.member.username.eq(username);
  }

  public static BooleanExpression isRead(Boolean isRead) {
    if (isRead == null) {
      return Expressions.asBoolean(true).isTrue();
    }

    return notification.isRead.eq(isRead);
  }

}
