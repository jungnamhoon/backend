package com.hkorea.skyisthelimit.dto.studyproblem.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StudyProblemSolvedCountByDayDTO {

  @Schema(
      title = "날짜",
      description = "스터디가 문제를 푼 날짜",
      example = "2025-09-03"
  )
  private LocalDate date;

  @Schema(
      title = "해당 날짜에 푼 문제 수",
      description = "해당 날짜에 스터디가 푼 문제 수",
      example = "2"
  )
  private Integer solvedCounts;
}
