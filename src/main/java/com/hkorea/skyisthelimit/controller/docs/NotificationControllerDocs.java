package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "알림 관련 API - JWT 토큰 필요")
public interface NotificationControllerDocs {

  @Operation(
      summary = "알림 목록 조회",
      description = "로그인한 사용자의 알림 전체 목록을 조회합니다"
  )

  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "알림 목록 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
      @AuthenticationPrincipal Jwt token);

  @Operation(
      summary = "알림 구독",
      description = "현재 사용자의 SSE 연결을 설정하여 실시간 알림을 구독합니다. " +
          "Swagger UI에서는 테스트가 불가하므로 클라이언트 도구를 사용하여(Postman, curl, 브라우저) 확인하세요"
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "SSE 연결 성공",
          content = @Content(
              mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
              schema = @Schema(implementation = SseEmitter.class)
          )
      )
  })
  SseEmitter subscribe(@AuthenticationPrincipal Jwt token);
}
