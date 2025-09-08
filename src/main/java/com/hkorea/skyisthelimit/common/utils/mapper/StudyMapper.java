package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyCreateResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyInfoResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudySummaryResponse;
import com.hkorea.skyisthelimit.dto.study.response.StudyUpdateResponse;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import com.hkorea.skyisthelimit.entity.Study;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudyMapper {

  private StudyMapper() {
  }

  public static StudyCreateResponse toStudyCreateResponse(Study study) {
    return StudyCreateResponse.builder()
        .studyId(study.getId())
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .maxMemberCount(study.getMaxMemberCount())
        .dailyProblemCount(study.getDailyProblemCount())
        .thumbnailUrl(study.getThumbnailUrl())
        .description(study.getDescription())
        .minRank(study.getMinRank())
        .maxRank(study.getMaxRank())
        .build();
  }

  public static StudySummaryResponse toStudySummaryResponse(Study study) {
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
        .build();
  }

  public static StudyInfoResponse toStudyInfoResponse(Study study, Set<MemberStudy> memberStudies,
      List<StudyProblem> studyProblemList) {

    List<DailyProblemCreateResponse> dailyProblems = study.getDailyProblems().stream()
        .map(StudyProblemMapper::toDailyProblemCreateResponse)
        .collect(Collectors.toList());

    return StudyInfoResponse.builder()
        .studyId(study.getId())
        .thumbnailUrl(study.getThumbnailUrl())
        .name(study.getName())
        .startDate(study.getStartDate())
        .endDate(study.getEndDate())
        .minRank(study.getMinRank())
        .maxLevel(study.getMaxRank())
        .currentMemberCount(study.getCurrentMemberCount())
        .maxMemberCount(study.getMaxMemberCount())
        .description(study.getDescription())
        .dailyProblemsSetterUsername(study.getDailyProblemsSetter().getUsername())
        .dailyProblems(dailyProblems)
        .totalSolvedProblemsCount(study.getTotalSolvedProblemsCount())
        .streak(study.getStreak())
        .membersNotSolvingDailyProblems(
            MemberStudyMapper.toMemberNotSolvingDailyProblemsDTOS(memberStudies))
        .problemListSolved(
            StudyProblemMapper.toStudyProblemSolvedDTOList(study.getSolvedStudyProblemList()))
        .solvedCountList(StudyProblemMapper.toStudyProblemSolvedCountByDayDTOList(
            study.getSolvedStudyProblemList()))
        .dailyProblemCount(study.getDailyProblemCount())
        .creatorUsername(study.getCreator().getUsername())
        .createdAt(study.getCreatedAt())
        .status(study.getStatus())
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
}
