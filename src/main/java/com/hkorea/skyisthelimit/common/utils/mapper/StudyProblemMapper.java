package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.problem.response.DailyProblemCreateResponse;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.studyproblem.internal.StudyProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.StudyProblem;
import com.hkorea.skyisthelimit.entity.embeddable.DailyProblem;
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
      List<StudyProblem> studyProblemList) {

    Map<LocalDate, Integer> dateToSolvedCountMap = studyProblemList.stream()
        .filter(sp -> sp.getSolvedDate() != null)
        .collect(Collectors.groupingBy(
            StudyProblem::getSolvedDate,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));

    return dateToSolvedCountMap.entrySet().stream()
        .map(entry -> new StudyProblemSolvedCountByDayDTO(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(StudyProblemSolvedCountByDayDTO::getDate))
        .toList();
  }

  public static DailyProblemCreateResponse toDailyProblemCreateResponse(DailyProblem dailyProblem) {
    return DailyProblemCreateResponse.builder()
        .problemId(dailyProblem.getProblemId())
        .title(dailyProblem.getProblemTitle())
        .assignedDate(dailyProblem.getAssignedDate())
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
