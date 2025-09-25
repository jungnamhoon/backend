package com.hkorea.skyisthelimit.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberUpdateResponse {

  @Schema(
      title = "새 닉네임",
      description = "회원 정보 수정 후 적용된 새 닉네임",
      example = "elephant1"
  )
  private String newNickname;
  
}
