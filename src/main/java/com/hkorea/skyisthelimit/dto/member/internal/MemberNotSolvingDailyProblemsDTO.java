package com.hkorea.skyisthelimit.dto.member.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberNotSolvingDailyProblemsDTO {

  private String username;
  private String profileUrl;
  private String nickname;
}
