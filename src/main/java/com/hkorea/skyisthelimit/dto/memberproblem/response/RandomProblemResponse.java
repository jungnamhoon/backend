package com.hkorea.skyisthelimit.dto.memberproblem.response;


import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RandomProblemResponse {

  @Schema(title = "백준 문제 ID", description = "문제의 고유 ID", example = "1000")
  private Integer baekjoonId;

  @Schema(title = "문제 제목", description = "문제의 제목", example = "A+B")
  private String title;

  @Schema(title = "문제 난이도", description = "문제의 난이도 등급", example = "BRONZE_V")
  private ProblemRank rank;

  @Schema(title = "문제 상태", description = "문제 상태 (SOLVED, MULTI-TRY, UNSOLVED)", example = "SOLVED")
  private MemberProblemStatus status;

  @Schema(title = "문제 URL", description = "문제 페이지 URL", example = "https://www.acmicpc.net/problem/1000")
  private String url;
}

