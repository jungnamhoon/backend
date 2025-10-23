package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.study.inner.StudyStatsDTO;
import com.hkorea.skyisthelimit.dto.study.response.StudyCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyInfoResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudySummaryResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyUpdateResponse;
import com.hkorea.skyisthelimit.dto.study.response.ThumbnailUpdateResponse;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus;

public class StudyMapper {

  private StudyMapper() {
  }

  public static StudyCreateResponse toStudyCreateResponse(Study study) {
    return StudyCreateResponse.builder()
        .studyId(study.getId())
        .thumbnailUrl(study.getThumbnailUrl())
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .maxMemberCount(study.getMaxMemberCount())
        .dailyProblemCount(study.getDailyProblemCount())
        .description(study.getDescription())
        .minRank(study.getMinRank())
        .maxRank(study.getMaxRank())
        .build();
  }

  public static StudySummaryResponse toStudySummaryResponse(Study study,
      MemberStudyStatus memberStudyStatus) {
    return StudySummaryResponse.builder()
        .studyId(study.getId())
        .thumbnailUrl(study.getThumbnailUrl())
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .currentMemberCount(study.getCurrentMemberCount())
        .maxMemberCount(study.getMaxMemberCount())
        .minRank(study.getMinRank())
        .maxRank(study.getMaxRank())
        .dailyProblemCount(study.getDailyProblemCount())
        .description(study.getDescription())
        .status(study.getStatus())
        .memberStudyStatus(memberStudyStatus)
        .build();
  }

  public static StudyInfoResponse toStudyInfoResponse(Study study, StudyStatsDTO studyStatsDTO) {

    return StudyInfoResponse.builder()
        .studyId(study.getId())
        .thumbnailUrl(study.getThumbnailUrl())
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .minRank(study.getMinRank())
        .maxRank(study.getMaxRank())
        .currentMemberCount(study.getCurrentMemberCount())
        .maxMemberCount(study.getMaxMemberCount())
        .description(study.getDescription())

        .dailyProblemsSetterUsername(studyStatsDTO.getDailyProblemSetterUsername())
        .dailyProblemsSetterProfileUrl(studyStatsDTO.getDailyProblemSetterProfileUrl())

        .dailyProblems(studyStatsDTO.getDailyProblems())
        .totalSolvedProblemsCount(studyStatsDTO.getTotalSolvedProblemCount())
        .streak(studyStatsDTO.getStreak())
        .membersNotSolvingDailyProblems(studyStatsDTO.getMembersNotSolvingDailyProblems())
        .problemListSolved(studyStatsDTO.getProblemListSolved())
        .solvedCountList(studyStatsDTO.getSolvedCountList())

        .dailyProblemCount(study.getDailyProblemCount())
        .creatorUsername(study.getCreator().getUsername())
        .status(study.getStatus())
        .createdAt(study.getCreatedAt())
        .build();
  }

  public static StudyUpdateResponse toStudyUpdateResponse(Study study) {

    return StudyUpdateResponse.builder()
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .maxMemberCount(study.getMaxMemberCount())
        .dailyProblemCount(study.getDailyProblemCount())
        .thumbnailUrl(study.getThumbnailUrl())
        .description(study.getDescription())
        .minLevel(study.getMinLevel())
        .maxLevel(study.getMaxLevel())
        .build();
  }

  public static ThumbnailUpdateResponse toThumbnailUpdateResponse(Study study) {

    return ThumbnailUpdateResponse.builder()
        .imageUrl(study.getThumbnailUrl())
        .build();
  }
}
