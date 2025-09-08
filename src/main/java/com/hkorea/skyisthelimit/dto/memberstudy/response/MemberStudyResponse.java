package com.hkorea.skyisthelimit.dto.memberstudy.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberStudyResponse {


  @Schema(description = "스터디 ID", example = "1")
  private Integer studyId;

  @Schema(description = "스터디 이름", example = "알고리즘 스터디")
  private String studyName;

  @Schema(description = "스터디 썸네일 이미지 URL",
      example = "https://example.com/images/study-thumbnail.png")
  private String studyThumbnailUrl;
}
