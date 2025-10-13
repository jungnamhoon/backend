package com.hkorea.skyisthelimit.dto.study.request;

import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;
import com.hkorea.skyisthelimit.entity.enums.ProblemRank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Schema(description = "스터디 생성/수정 요청 DTO")
public class StudyCreateRequest {

  @Schema(description = "스터디 이름", example = "백준 스터디")
  @NotNull
  private String name;

  @Schema(description = "스터디 시작일", example = "2025-09-10")
  @NotNull
  private LocalDate startDate;

  @Schema(description = "스터디 종료일", example = "2025-12-31")
  @NotNull
  private LocalDate endDate;

  @Schema(description = "최대 참여 인원 수", example = "20")
  @NotNull
  private Integer maxMemberCount;

  @Schema(description = "하루에 등록되는 문제 개수", example = "3")
  @NotNull
  private Integer dailyProblemCount;

  @Schema(description = "스터디 설명", example = "매일 알고리즘 문제를 풀고 리뷰하는 스터디입니다.")
  private String description;

  @Schema(description = "참여 가능 최소 레벨", example = "1")
  @NotNull
  private Integer minLevel;

  @Schema(description = "참여 가능 최대 레벨", example = "5")
  @NotNull
  private Integer maxLevel;

  @Schema(description = "썸네일 base64 데이터")
  private String thumbnailData;

  public Study toEntity(Member host) {
    Study study = Study.builder()
        .name(this.name)
        .startDate(this.startDate)
        .endDate(this.endDate)
        .description(this.description)
        .maxMemberCount(this.maxMemberCount)
        .dailyProblemCount(this.dailyProblemCount)
        .currentMemberCount(1)
        .minLevel(this.minLevel)
        .maxLevel(this.maxLevel)
        .minRank(ProblemRank.fromLevel(this.minLevel))
        .maxRank(ProblemRank.fromLevel(this.maxLevel))
        .createdAt(LocalDateTime.now())
        .problemSetterIdx(0)
        .creator(host)
        .build();

    MemberStudy creatorMemberStudy = MemberStudy.create(host, study, MemberStudyStatus.APPROVED);
    study.addMemberStudy(creatorMemberStudy);

    return study;
  }
}
