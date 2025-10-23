package com.hkorea.skyisthelimit.repository;

import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByMemberOrderByCreatedAtDesc(Member member);

  List<Notification> findAllByIsReadTrueAndCreatedAtBefore(LocalDateTime dateTime);
}
