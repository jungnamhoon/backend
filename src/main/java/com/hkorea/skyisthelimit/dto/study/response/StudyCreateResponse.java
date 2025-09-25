package com.hkorea.skyisthelimit.dto.study.response;

import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyCreateResponse {

  @Schema(description = "스터디 고유 ID", example = "1")
  private Integer studyId;

  @Schema(description = "스터디 기본 썸네일 Url", example = "https://skyisthelimit.kro.kr/files/skyisthelimit/study/basic-profile.png")
  private String thumbnailUrl;

  @Schema(description = "스터디 이름", example = "백준 스터디")
  private String name;

  @Schema(description = "스터디 시작일", example = "2025-09-10")
  private LocalDate startDate;

  @Schema(description = "스터디 종료일", example = "2025-12-31")
  private LocalDate endDate;

  @Schema(description = "최대 참여 인원 수", example = "20")
  private Integer maxMemberCount;

  @Schema(description = "하루에 등록되는 문제 개수", example = "3")
  private Integer dailyProblemCount;

  @Schema(description = "스터디 설명", example = "매일 알고리즘 문제를 풀고 리뷰하는 스터디입니다.")
  private String description;

  @Schema(description = "문제 최소 난이도")
  private ProblemRank minRank;

  @Schema(description = "문제 최대 난이도")
  private ProblemRank maxRank;

}
