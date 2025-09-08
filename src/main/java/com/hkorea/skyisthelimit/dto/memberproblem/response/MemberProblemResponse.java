package com.hkorea.skyisthelimit.dto.memberproblem.response;

import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberProblemResponse {

  @Schema(title = "백준 문제 ID", description = "문제의 고유 ID", example = "1000")
  private Integer baekjoonId;

  @Schema(title = "문제 제목", description = "문제의 제목", example = "A+B")
  private String title;

  @Schema(title = "문제 난이도", description = "문제의 난이도 등급", example = "BRONZE_V")
  private ProblemRank rank;

  @Schema(title = "풀이 날짜", description = "문제를 푼 날짜", example = "2025-08-31")
  private LocalDate solvedDate;

  @Schema(title = "풀이 횟수", description = "문제를 푼 횟수", example = "3")
  private Integer solvedCount;

  @Schema(title = "문제 상태", description = "문제 상태 (SOLVED, MULTI-TRY, UNSOLVED)", example = "SOLVED")
  private MemberProblemStatus status;

  @Schema(title = "노트 작성 여부", description = "문제에 대해 오답 노트를 작성했는지 여부", example = "true")
  private Boolean noteWritten;

  @Schema(title = "문제 URL", description = "문제 페이지 URL", example = "https://www.acmicpc.net/problem/1000")
  private String url;

  @Schema(title = "문제 태그 리스트", description = "문제에 붙은 태그 목록")
  private List<ProblemTagDTO> problemTags;

  @Data
  @AllArgsConstructor
  @Builder
  public static class ProblemTagDTO {

    @Schema(title = "태그 영어 이름", description = "문제 태그의 영어 이름", example = "math")
    private String enName;

    @Schema(title = "태그 한글 이름", description = "문제 태그의 한글 이름", example = "수학")
    private String koName;
  }

}