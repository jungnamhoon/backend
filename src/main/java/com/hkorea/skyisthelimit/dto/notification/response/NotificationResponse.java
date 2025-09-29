package com.hkorea.skyisthelimit.dto.notification.response;

import com.hkorea.skyisthelimit.dto.notification.internal.MessageContent;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "알림 응답 DTO")
public class NotificationResponse {

  @Schema(description = "알림 ID", example = "1")
  private Long id;

  @Schema(description = "알림 메시지 내용", example = "user2 이 스터디 참가 요청을 보냈습니다.;")
  private MessageContent messageContent;

  @Schema(description = "읽음 여부", example = "false")
  private boolean isRead;

  @Schema(description = "알림 생성 시각", example = "2025-09-03T12:00:00")
  private LocalDateTime createdAt;

}
