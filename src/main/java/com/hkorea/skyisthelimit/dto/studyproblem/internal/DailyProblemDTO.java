package com.hkorea.skyisthelimit.dto.studyproblem.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyProblemDTO {

  @Schema(description = "문제의 고유 ID", example = "1000")
  private Integer problemId;

  @Schema(description = "문제 제목", example = "A+B")
  private String title;

  @Schema(title = "문제 URL", description = "문제 페이지 URL", example = "https://www.acmicpc.net/problem/1000")
  private String problemUrl;

  @Schema(description = "해당 문제가 출제된 날짜", example = "2025-09-06")
  private LocalDate assignedDate;
}
