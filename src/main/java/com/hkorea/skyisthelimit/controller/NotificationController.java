package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.NotificationControllerDocs;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerDocs {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
      @AuthenticationPrincipal Jwt token) {

    List<NotificationResponse> responseDTOS = notificationService.getNotifications(
        token.getClaim("username"));
    return ApiResponse.of(SuccessCode.OK, responseDTOS);
  }

  @GetMapping(value = "me/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@AuthenticationPrincipal Jwt token) {
    return notificationService.subscribe(token.getClaim("username"));
  }
}
