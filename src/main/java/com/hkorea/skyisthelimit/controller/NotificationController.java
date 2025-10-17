package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.controller.docs.NotificationControllerDocs;
import com.hkorea.skyisthelimit.dto.criteria.NotificationCriteria;
import com.hkorea.skyisthelimit.dto.notification.request.NotificationMarkAsReadRequest;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.service.NotificationService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerDocs {

  private final NotificationService notificationService;

//  @GetMapping("/notifications/me")
//  public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
//      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
//
//    List<NotificationResponse> responseDTOS = notificationService.getNotifications(
//        customOAuth2User.getUsername());
//    return ApiResponse.of(SuccessCode.OK, responseDTOS);
//  }

  @GetMapping("/notifications/me")
  public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getNotificationPage(
      @ModelAttribute NotificationCriteria criteria,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    Page<NotificationResponse> responsePage = notificationService.getNotificationPage(criteria,
        customOAuth2User.getUsername());
    return ApiResponse.of(SuccessCode.OK, responsePage);
  }

  @PatchMapping("/notifications/me/{messageId}")
  public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
      @PathVariable Long messageId) {

    NotificationResponse responseDTO = notificationService.markAsRead(messageId);

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PatchMapping("/notifications/me")
  public ResponseEntity<ApiResponse<Void>> updateNotifications(
      @RequestBody NotificationMarkAsReadRequest requestDTO) {

    notificationService.markAsReadAll(requestDTO.getMessageIds());

    return ApiResponse.of(SuccessCode.OK, null);
  }


  @GetMapping(value = "/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    return notificationService.subscribe(customOAuth2User.getUsername());
  }

  @GetMapping("/notifications/subscriptions")
  public Set<String> getSubscriptions() {
    return notificationService.getAllSubscribers();
  }
}
