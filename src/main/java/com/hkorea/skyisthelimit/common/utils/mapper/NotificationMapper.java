package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.entity.Notification;

public class NotificationMapper {

  private NotificationMapper() {
  }

  public static NotificationResponse toNotificationResponse(Notification notification) {
    return NotificationResponse.builder()
        .id(notification.getId())
        .message(notification.getMessage())
        .isRead(notification.isRead())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}
