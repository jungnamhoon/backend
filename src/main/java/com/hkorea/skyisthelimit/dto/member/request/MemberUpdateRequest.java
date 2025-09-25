package com.hkorea.skyisthelimit.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberUpdateRequest {

  @Schema(
      title = "닉네임",
      description = "사용자의 새 닉네임을 입력",
      example = "elephant1")
  private String nickname;
}
