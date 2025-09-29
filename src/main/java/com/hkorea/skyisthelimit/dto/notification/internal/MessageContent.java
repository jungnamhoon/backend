package com.hkorea.skyisthelimit.dto.notification.internal;

import com.hkorea.skyisthelimit.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageContent {

  private Integer fromMemberId;
  private String fromUsername;
  private String fromRealName;
  private String fromNickname;
  private String fromEmail;
  private Integer studyId;
  private MessageType messageType;
}
