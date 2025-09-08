package com.hkorea.skyisthelimit.dto.memberproblem.response;

import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteResponse {

  @Schema(description = "백준 문제 ID", example = "1000")
  private Integer baekjoonId;      // 문제 ID

  @Schema(description = "문제 제목", example = "A+B")
  private String title;            // 문제 제목

  @Schema(description = "오답노트 내용", example = "dp 방식으로 풀었습니다.")
  private String content;             // 오답노트 내용

  @Schema(description = "마지막 풀이 날짜", example = "2025-09-02")
  private LocalDate solvedDate;  // 마지막 풀이 시간


  @Schema(
      description = "문제 상태",
      example = "SOLVED"
  )
  private MemberProblemStatus status;    // 문제 상태 (ex: MULTI_TRY)
}
