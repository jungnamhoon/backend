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

  @Schema(
      title = "새 프로필 이미지 URL",
      description = "회원 정보 수정 후 적용된 새 프로필 이미지 URL",
      example = "https://cdn.example.com/profiles/user1.png"
  )
  private String newProfileImageUrl;
}
