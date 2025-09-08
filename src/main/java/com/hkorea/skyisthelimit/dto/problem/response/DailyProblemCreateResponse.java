package com.hkorea.skyisthelimit.dto.problem.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyProblemCreateResponse {

  @Schema(description = "문제의 고유 ID", example = "1000")
  private Integer problemId;

  @Schema(description = "문제 제목", example = "A+B")
  private String title;

  @Schema(description = "해당 문제가 출제된 날짜", example = "2025-09-06")
  private LocalDate assignedDate;

}
