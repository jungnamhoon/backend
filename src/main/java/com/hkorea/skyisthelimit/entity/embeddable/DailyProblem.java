package com.hkorea.skyisthelimit.entity.embeddable;

import static com.hkorea.skyisthelimit.controller.TodayController.getToday;

import com.hkorea.skyisthelimit.entity.Problem;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class DailyProblem {

  private Integer problemId;
  private String problemTitle;
  private LocalDate assignedDate;

  public static DailyProblem create(Problem problem) {

    return DailyProblem.builder()
        .problemId(problem.getBaekjoonId())
        .problemTitle(problem.getTitle())
        .assignedDate(getToday())
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DailyProblem that)) {
      return false;
    }
    return Objects.equals(problemId, that.problemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(problemId);
  }
}
