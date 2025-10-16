package com.hkorea.skyisthelimit.service;

import static com.hkorea.skyisthelimit.repository.predicate.NotificationPredicates.usernameEq;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.NotificationMapper;
import com.hkorea.skyisthelimit.dto.criteria.PageableCriteria;
import com.hkorea.skyisthelimit.dto.notification.internal.MessageContent;
import com.hkorea.skyisthelimit.dto.notification.response.NotificationResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.Notification;
import com.hkorea.skyisthelimit.entity.QNotification;
import com.hkorea.skyisthelimit.entity.enums.MessageType;
import com.hkorea.skyisthelimit.repository.EmitterRepository;
import com.hkorea.skyisthelimit.repository.NotificationRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  private final QueryDSLHelper queryDSLHelper;

  @Scheduled(fixedRate = 10000)
  public void sendPingToClients() {
    emitterRepository.getEmitters().forEach((username, emitter) -> {
      sendToClient(username, "ping");
    });
  }

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

  public Page<NotificationResponse> getNotificationPage(PageableCriteria<QNotification> criteria,
      String username) {

    QNotification notification = QNotification.notification;

    BooleanExpression predicate = criteria.toPredicate().and(usernameEq(username));
    OrderSpecifier<?> orderSpecifier = criteria.toOrderSpecifier(notification);
    Pageable pageable = criteria.toPageable();

    List<Notification> notifications = queryDSLHelper.fetchEntities(notification, predicate,
        orderSpecifier, pageable);

    List<NotificationResponse> notificationResponseList = NotificationMapper.toNotificationResponseList(
        notifications);

    return new PageImpl<>(notificationResponseList, pageable, notifications.size());
  }

  @Transactional
  public void createNotification(String receiverUsername, MessageContent message) {

    Member receiver = memberService.getMember(receiverUsername);
    Notification notification = Notification.create(receiver, message);
    notificationRepository.save(notification);
    sendToClient(receiverUsername, NotificationMapper.toNotificationResponse(notification));
  }

  @Transactional
  public NotificationResponse markAsRead(Long messageId) {

    Notification notification = notificationRepository.findById(messageId).orElseThrow(() ->
        new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.isRead()) {
      notification.setIsRead(true);
    }
    return NotificationMapper.toNotificationResponse(notification);
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

  public Set<String> getAllSubscribers() {
    return emitterRepository.getAllSubscribers();
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

}
