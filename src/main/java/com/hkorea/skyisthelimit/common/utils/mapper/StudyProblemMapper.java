package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.DailyProblemDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudyProblemMapper {

  private StudyProblemMapper() {
  }

  public static List<StudyProblemSolvedDTO> toStudyProblemSolvedDTOList(
      List<StudyProblem> studyProblemList) {
    if (studyProblemList == null || studyProblemList.isEmpty()) {
      return Collections.emptyList();
    }
    return studyProblemList.stream()
        .map(StudyProblemMapper::toStudyProblemSolvedDTO)
        .collect(Collectors.toList());
  }

  public static List<StudyProblemSolvedCountByDayDTO> toStudyProblemSolvedCountByDayDTOList(
      Map<LocalDate, Integer> dateToSolvedCountMap) {
    return dateToSolvedCountMap.entrySet().stream()
        .map(entry -> new StudyProblemSolvedCountByDayDTO(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(StudyProblemSolvedCountByDayDTO::getDate))
        .toList();
  }

  public static List<DailyProblemDTO> toDailyProblemDTOList(List<StudyProblem> studyProblemList) {
    if (studyProblemList == null || studyProblemList.isEmpty()) {
      return Collections.emptyList();
    }

    return studyProblemList.stream()
        .map(StudyProblemMapper::toDailyProblemDTO)
        .collect(Collectors.toList());
  }


  public static DailyProblemCreateResponse toDailyProblemCreateResponse(StudyProblem studyProblem) {
    return DailyProblemCreateResponse.builder()
        .problemId(studyProblem.getProblem().getBaekjoonId())
        .title(studyProblem.getProblem().getTitle())
        .assignedDate(studyProblem.getAssignedDate())
        .build();
  }

  public static DailyProblemDTO toDailyProblemDTO(StudyProblem studyProblem) {
    return DailyProblemDTO.builder()
        .problemId(studyProblem.getProblem().getBaekjoonId())
        .title(studyProblem.getProblem().getTitle())
        .assignedDate(studyProblem.getAssignedDate())
        .build();
  }

  public static StudyProblemSolvedDTO toStudyProblemSolvedDTO(StudyProblem studyProblem) {

    return StudyProblemSolvedDTO.builder()
        .problemId(studyProblem.getProblem().getBaekjoonId())
        .problemTitle(studyProblem.getProblem().getTitle())
        .problemUrl(studyProblem.getProblem().getUrl())
        .problemRank(studyProblem.getProblem().getRank())
        .build();
  }


}
