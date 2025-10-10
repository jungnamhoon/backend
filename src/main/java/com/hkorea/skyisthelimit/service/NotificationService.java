package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.utils.mapper.NotificationMapper;
import com.hkorea.skyisthelimit.dto.notification.internal.MessageContent;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.Notification;
import com.hkorea.skyisthelimit.entity.enums.MessageType;
import com.hkorea.skyisthelimit.repository.EmitterRepository;
import com.hkorea.skyisthelimit.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final MemberService memberService;
  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;

  public SseEmitter subscribe(String username) {

    SseEmitter emitter = emitterRepository.save(username, new SseEmitter(DEFAULT_TIMEOUT));

    // 💡 1. 연결 완료 시 (onCompletion)
    emitter.onCompletion(() -> {
      log.info("[SSE] 🟢 Completed: Emitter for user {} finished. Deleting from repository.",
          username);
      emitterRepository.deleteByUsername(username);
    });

    emitter.onTimeout(() -> {
      log.info("[SSE] 🟡 Timeout: Emitter for user {} timed out. Deleting from repository.",
          username);
      emitterRepository.deleteByUsername(username);
    });

    emitter.onError((ex) ->
    {
      log.info("[SSE] 🔴 Error: Emitter for user {} failed with {}. Deleting from repository.",
          username, ex.getClass().getSimpleName(), ex);
      emitterRepository.deleteByUsername(username);
    });

    sendToClient(username, "subscribe event, username : " + username);

    return emitter;

  }

  @Transactional
  public List<NotificationResponse> getNotifications(String username) {
    Member member = memberService.getMember(username);
    List<Notification> oldNotifications = fetchNotificationsForMember(
        member);

    return NotificationMapper.toNotificationResponseList(oldNotifications);
  }

  @Transactional
  public void createNotification(String receiverUsername, MessageContent message) {

    Member receiver = memberService.getMember(receiverUsername);
    Notification notification = Notification.create(receiver, message);
    notificationRepository.save(notification);
    sendToClient(receiverUsername, NotificationMapper.toNotificationResponse(notification));
  }

  public MessageContent createMessage(Member fromMember, Integer studyId, MessageType messageType) {

    return new MessageContent(
        fromMember.getId(),
        fromMember.getUsername(),
        fromMember.getRealName(),
        fromMember.getNickname(),
        fromMember.getEmail(),
        studyId,
        messageType
    );
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

  @Scheduled(fixedRate = 10000)
  public void sendPingToClients() {
    emitterRepository.getEmitters().forEach((username, emitter) -> {
      sendToClient(username, "ping");
    });
  }

  // 구독 중인 모든 사용자 목록 반환
  public Set<String> getAllSubscribers() {
    return emitterRepository.getAllSubscribers();  // 구독 중인 모든 사용자 이름 목록 반환
  }
}
