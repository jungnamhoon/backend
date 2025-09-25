package com.hkorea.skyisthelimit.dto.study.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ThumbnailUpdateResponse {

  @Schema(
      title = "이미지 Url",
      description = "프로필 이미지 업데이트 후 이미지 Url",
      example = "https://skyisthelimit.kro.kr/profile-bucket/profile/user1_123456.png"
  )
  private String imageUrl;
  
}
