package com.hkorea.skyisthelimit.dto.memberproblem.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProblemTagCountResponse {

  @Schema(description = "문제의 알고리즘 종류(태그명)", example = "그래프")
  private String tagName;

  @Schema(description = "해당 알고리즘 종류 문제를 푼 횟수", example = "42")
  private Long count;

}