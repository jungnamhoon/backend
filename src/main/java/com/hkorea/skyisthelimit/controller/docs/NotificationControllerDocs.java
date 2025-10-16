package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.dto.criteria.NotificationCriteria;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "알림 관련 API - JWT 토큰 필요")
public interface NotificationControllerDocs {

//  @Operation(
//      summary = "알림 목록 조회",
//      description = "로그인한 사용자의 알림 전체 목록을 조회합니다"
//  )
//
//  @ApiResponses({
//      @io.swagger.v3.oas.annotations.responses.ApiResponse(
//          responseCode = "200",
//          description = "알림 목록 조회 성공"
//      )
//  })
//  ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
//      @AuthenticationPrincipal CustomOAuth2User customOAuth2User);

  @Operation(
      summary = "알림 목록 페이지 조회",
      description = "로그인한 사용자의 알림 목록 페이지를 조회합니다"
  )

  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "알림 목록 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<Page<NotificationResponse>>> getNotificationPage(
      @ModelAttribute NotificationCriteria criteria,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User);

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
  SseEmitter subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User);

  @Operation(
      summary = "알림 읽음 처리",
      description = "지정한 알림을 읽음 상태로 변경합니다."
  )
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "성공적으로 읽음 처리됨",
          content = @Content(
              mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
              schema = @Schema(implementation = SseEmitter.class)
          )
      )
  })
  ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
      @Parameter(description = "읽음 처리할 알림 ID", required = true)
      @PathVariable Long messageId);
}
