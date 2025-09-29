package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.entity.Notification;
import java.util.List;

public class NotificationMapper {

  private NotificationMapper() {
  }

  public static NotificationResponse toNotificationResponse(Notification notification) {
    return NotificationResponse.builder()
        .id(notification.getId())
        .messageContent(notification.getMessage())
        .isRead(notification.isRead())
        .createdAt(notification.getCreatedAt())
        .build();
  }

  public static List<NotificationResponse> toNotificationResponseList(
      List<Notification> notifications) {
    return notifications.stream()
        .map(NotificationMapper::toNotificationResponse)
        .toList();
  }
}
