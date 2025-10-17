package com.hkorea.skyisthelimit.dto.notification.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class NotificationMarkAsReadRequest {

  @Schema(
      description = "읽음 처리할 알림 ID 목록",
      example = "[1, 2, 3]"
  )
  private List<Long> messageIds;

}
