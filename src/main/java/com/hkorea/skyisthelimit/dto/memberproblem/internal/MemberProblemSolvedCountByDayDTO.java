package com.hkorea.skyisthelimit.dto.memberproblem.internal;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberProblemSolvedCountByDayDTO {

  @Schema(
      title = "날짜",
      description = "회원이 문제를 푼 날짜",
      example = "2025-09-03"
  )
  private LocalDate date;

  @Schema(
      title = "해당 날짜에 푼 문제 수",
      description = "해당 날짜에 회원이 푼 문제 수",
      example = "2"
  )
  private Integer solvedCounts;
}
