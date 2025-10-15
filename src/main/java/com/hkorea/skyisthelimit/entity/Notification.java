package com.hkorea.skyisthelimit.entity;

import com.hkorea.skyisthelimit.dto.notification.internal.MessageContent;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private MessageContent message;

  private boolean isRead = false;

  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public static Notification create(Member member, MessageContent message) {
    Notification notification = new Notification();
    notification.member = member;
    notification.message = message;
    notification.isRead = false;
    notification.createdAt = LocalDateTime.now();
    return notification;
  }

  public void setIsRead(boolean isRead) {
    this.isRead = isRead;
  }
}
