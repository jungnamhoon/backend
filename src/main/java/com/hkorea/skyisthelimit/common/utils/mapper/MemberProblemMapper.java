package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.request.MemberProblemTagCountResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.NoteResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.RandomProblemResponse;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberProblemMapper {

  private MemberProblemMapper() {
  }

  public static MemberProblemResponse toMemberProblemResponse(MemberProblem memberProblem) {
    return MemberProblemResponse.builder()
        .baekjoonId(memberProblem.getProblem().getBaekjoonId())
        .title(memberProblem.getProblem().getTitle())
        .rank(memberProblem.getProblem().getRank())
        .solvedDate(memberProblem.getSolvedDate())
        .solvedCount(memberProblem.getSolvedCount())
        .status(memberProblem.getStatus())
        .noteWritten(memberProblem.getNoteWritten())
        .url(memberProblem.getProblem().getUrl())
        .problemTags(ProblemMapper.toProblemTagDTOList(memberProblem.getProblem()
            .getProblemTagList()))
        .build();
  }

  public static SolveResponse toSolveResponse(MemberProblem memberProblem) {
    return SolveResponse.builder()
        .baekjoonId(memberProblem.getProblem().getBaekjoonId())
        .title(memberProblem.getProblem().getTitle())
        .status(memberProblem.getStatus())
        .solvedDate(memberProblem.getSolvedDate())
        .build();
  }

  public static RandomProblemResponse toRandomProblemResponse(MemberProblem memberProblem) {

    if (memberProblem == null) {
      return null;
    }

    return RandomProblemResponse.builder()
        .baekjoonId(memberProblem.getProblem().getBaekjoonId())
        .title(memberProblem.getProblem().getTitle())
        .rank(memberProblem.getProblem().getRank())
        .status(memberProblem.getStatus())
        .url(memberProblem.getProblem().getUrl())
        .build();
  }

  public static NoteResponse toNoteResponse(MemberProblem memberProblem) {
    return NoteResponse.builder()
        .baekjoonId(memberProblem.getProblem().getBaekjoonId())
        .title(memberProblem.getProblem().getTitle())
        .content(memberProblem.getNote())
        .solvedDate(memberProblem.getSolvedDate())
        .status(memberProblem.getStatus())
        .build();
  }

  public static MemberProblemSolvedDTO toMemberProblemSolvedDTO(MemberProblem memberProblem) {
    return MemberProblemSolvedDTO.builder()
        .problemId(memberProblem.getProblem().getBaekjoonId())
        .problemTitle(memberProblem.getProblem().getTitle())
        .problemUrl(memberProblem.getProblem().getUrl())
        .problemRank(memberProblem.getProblem().getRank())
        .build();
  }

  public static List<MemberProblemTagCountResponse> toMemberProblemTagCountResponseList(
      List<MemberProblem> memberProblemList) {

    Map<String, Long> tagCounts = memberProblemList.stream()
        .flatMap(mp -> mp.getProblem().getProblemTagList().stream())
        .collect(Collectors.groupingBy(
            ProblemTag::getKoName,
            Collectors.counting()
        ));

    return tagCounts.entrySet().stream()
        .map(entry -> MemberProblemTagCountResponse.builder()
            .tagName(entry.getKey())
            .count(entry.getValue())
            .build())
        .sorted((a, b) -> b.getCount().compareTo(a.getCount()))
        .toList();
  }

  public static List<MemberProblemResponse> toMemberProblemResponseList(
      List<MemberProblem> memberProblems) {

    return memberProblems.stream()
        .map(MemberProblemMapper::toMemberProblemResponse)
        .toList();
  }

  public static List<MemberProblemSolvedDTO> toMemberProblemSolvedDTOList(
      List<MemberProblem> memberProblemSolvedList) {
    return memberProblemSolvedList.stream()
        .map(MemberProblemMapper::toMemberProblemSolvedDTO)
        .toList();
  }

  public static List<MemberProblemSolvedCountByDayDTO> toMemberProblemSolvedCountByDayDTOList(
      Map<LocalDate, Integer> dateToSolvedCountMap) {

    return dateToSolvedCountMap.entrySet().stream()
        .map(entry -> new MemberProblemSolvedCountByDayDTO(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(MemberProblemSolvedCountByDayDTO::getDate))
        .toList();
  }

}
