package com.hkorea.skyisthelimit.dto.study.response;

import com.hkorea.skyisthelimit.dto.member.internal.MemberNotSolvingDailyProblemsDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.DailyProblemDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import com.hkorea.skyisthelimit.entity.enums.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "스터디 응답 DTO")
public class StudyInfoResponse {

  @Schema(description = "스터디 ID", example = "1")
  private Integer studyId;

  @Schema(description = "스터디 썸네일 이미지 URL", example = "https://example.com/study.png")
  private String thumbnailUrl;

  @Schema(description = "스터디 이름", example = "백준 스터디")
  private String name;

  @Schema(description = "스터디 시작일", example = "2025-09-01")
  private LocalDate startDate;

  @Schema(description = "스터디 종료일", example = "2025-12-31")
  private LocalDate endDate;

  @Schema(description = "문제 최소 난이도")
  private ProblemRank minRank;

  @Schema(description = "문제 최대 난이도")
  private ProblemRank maxRank;

  @Schema(description = "현재 참여 인원 수", example = "5")
  private Integer currentMemberCount;

  @Schema(description = "최대 참여 인원 수", example = "20")
  private Integer maxMemberCount;

  @Schema(description = "스터디 설명", example = "매일 알고리즘 문제를 풀고 리뷰하는 스터디입니다.")
  private String description;

  @Schema(description = "오늘의 문제 출제자 유저네임", example = "user1")
  private String dailyProblemsSetterUsername;

  @Schema(description = "오늘의 문제")
  private List<DailyProblemDTO> dailyProblems;

  @Schema(description = "스터디원 모두가 푼 문제 총 개수")
  private int totalSolvedProblemsCount;

  @Schema(description = "연속 달성")
  private int streak;

  @Schema(description = "아직 제출을 안한 스터디원")
  private List<MemberNotSolvingDailyProblemsDTO> membersNotSolvingDailyProblems;

  @Schema(description = "스터디가 해결한 문제")
  private List<StudyProblemSolvedDTO> problemListSolved;

  @Schema(description = "스터디가 날짜별로 푼 문제 수 리스트")
  private List<StudyProblemSolvedCountByDayDTO> solvedCountList;


  // 화면에 표기 되지 않는 정보
  @Schema(description = "하루에 등록되는 문제 개수", example = "3")
  private Integer dailyProblemCount;

  @Schema(description = "스터디 생성자(스터디장) 유저네임", example = "skyisthelimit")
  private String creatorUsername;

  @Schema(description = "스터디 상태", example = "ACTIVE", allowableValues = {"RECRUITING", "FULL",
      "ONGOING", "COMPLETED", "CANCELLED"})
  private StudyStatus status;

  @Schema(description = "스터디 생성 일시", example = "2025-09-03T15:30:00")
  private LocalDateTime createdAt;


}
