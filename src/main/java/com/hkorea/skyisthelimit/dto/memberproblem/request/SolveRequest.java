package com.hkorea.skyisthelimit.dto.memberproblem.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class SolveRequest {

  @Schema(
      title = "제출 번호",
      description = "문제 제출당 고유 제출 번호",
      example = "98245328"
  )
  private Long submitId;

  @Schema(
      title = "백준 문제 ID",
      description = "회원이 푼 문제의 고유 ID",
      example = "1000"
  )
  private Integer baekjoonId;

  @Schema(
      title = "문제 풀이 여부",
      description = "회원이 문제를 맞췄는지 여부",
      example = "true"
  )
  private Boolean isSolved;
}