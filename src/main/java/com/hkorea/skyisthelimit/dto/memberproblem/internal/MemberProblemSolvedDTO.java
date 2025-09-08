package com.hkorea.skyisthelimit.dto.memberproblem.internal;

import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberProblemSolvedDTO {

  @Schema(title = "문제 ID", description = "백준 문제의 고유 ID", example = "1000")
  private Integer problemId;

  @Schema(title = "문제 제목", description = "백준 문제의 제목", example = "A+B")
  private String problemTitle;

  @Schema(title = "문제 URL", description = "문제 페이지 URL", example = "https://www.acmicpc.net/problem/1000")
  private String problemUrl;

  @Schema(title = "문제 등급", description = "문제 난이도 등급", example = "BRONZE_V")
  private ProblemRank problemRank;
}
