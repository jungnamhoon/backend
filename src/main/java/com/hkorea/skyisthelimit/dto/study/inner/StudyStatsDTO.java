package com.hkorea.skyisthelimit.dto.study.inner;

import com.hkorea.skyisthelimit.dto.member.internal.MemberNotSolvingDailyProblemsDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.DailyProblemDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyStatsDTO {

  @Schema(description = "오늘의 문제 출제자 유저네임", example = "user1")
  private String dailyProblemSetterUsername;

  @Schema(description = "오늘의 문제")
  private List<DailyProblemDTO> dailyProblems;

  @Schema(description = "문제를 풀지 않은 멤버 정보")
  private List<MemberNotSolvingDailyProblemsDTO> membersNotSolvingDailyProblems;

  @Schema(description = "스터디가 해결한 문제")
  private List<StudyProblemSolvedDTO> problemListSolved;

  @Schema(description = "스터디가 날짜별로 푼 문제 수 리스트")
  private List<StudyProblemSolvedCountByDayDTO> solvedCountList;

  @Schema(description = "스터디가 푼 전체 문제 수")
  private int totalSolvedProblemCount;

  @Schema(description = "스터디원 전체가 연속으로 문제를 푼 일 수")
  private int streak;
}
