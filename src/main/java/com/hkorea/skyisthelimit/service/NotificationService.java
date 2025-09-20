package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.utils.mapper.NotificationMapper;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.Notification;
import com.hkorea.skyisthelimit.repository.EmitterRepository;
import com.hkorea.skyisthelimit.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final MemberService memberService;
  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;

  @Transactional
  public SseEmitter subscribe(String username) {

    SseEmitter emitter = emitterRepository.save(username, new SseEmitter(DEFAULT_TIMEOUT));

    emitter.onCompletion(() -> emitterRepository.deleteByUsername(username));
    emitter.onTimeout(() -> emitterRepository.deleteByUsername(username));
    emitter.onError((ex) -> emitterRepository.deleteByUsername(username));

    Member member = memberService.getMember(username);
    List<Notification> oldNotifications = fetchNotificationsForMember(
        member);

    oldNotifications.forEach(notification -> {
      sendToClient(username, notification);
    });

    sendToClient(username, "subscribe event, username : " + username);

    return emitter;

  }

  @Transactional
  public List<NotificationResponse> getNotifications(String username) {
    Member member = memberService.getMember(username);
    List<Notification> oldNotifications = fetchNotificationsForMember(
        member);

    return toResponseDTOS(oldNotifications);
  }

  @Transactional
  public void createNotification(String receiverUsername, String message) {

    Member receiver = memberService.getMember(receiverUsername);
    Notification notification = Notification.create(receiver, message);
    notificationRepository.save(notification);
    sendToClient(receiverUsername, NotificationMapper.toNotificationResponse(notification));
  }

  private void sendToClient(String username, Object data) {
    SseEmitter emitter = emitterRepository.getEmitter(username);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("sse").data(data));
      } catch (IOException e) {
        emitter.completeWithError(e);
        emitterRepository.deleteByUsername(username);
      }
    }
  }

  private List<Notification> fetchNotificationsForMember(Member member) {
    return notificationRepository.findByMemberOrderByCreatedAtDesc(
        member);
  }


  private List<NotificationResponse> toResponseDTOS(List<Notification> notifications) {
    return notifications.stream()
        .map(NotificationMapper::toNotificationResponse)
        .toList();
  }
}
