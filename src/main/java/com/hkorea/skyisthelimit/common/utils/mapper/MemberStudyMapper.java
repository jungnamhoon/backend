package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.member.internal.MemberNotSolvingDailyProblemsDTO;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyParticipationResponse;
import com.hkorea.skyisthelimit.dto.memberstudy.response.MemberStudyResponse;
import com.hkorea.skyisthelimit.entity.MemberStudy;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MemberStudyMapper {

  private MemberStudyMapper() {
  }

  public static MemberStudyParticipationResponse toMemberStudyParticipationResponse(
      MemberStudy memberStudy) {

    return MemberStudyParticipationResponse.builder()
        .studyId(memberStudy.getStudy().getId())
        .studyTitle(memberStudy.getStudy().getName())
        .studyAdminUsername(memberStudy.getStudy().getCreator().getUsername())
        .studyParticipantUsername(memberStudy.getMember().getUsername())
        .requestStatus(memberStudy.getStatus())
        .build();
  }

  public static MemberStudyResponse toMemberStudyResponse(MemberStudy memberStudy) {
    return MemberStudyResponse.builder()
        .studyId(memberStudy.getStudy().getId())
        .studyName(memberStudy.getStudy().getName())
        .studyThumbnailUrl(memberStudy.getStudy().getThumbnailUrl())
        .build();
  }

  public static Set<MemberNotSolvingDailyProblemsDTO> toMemberNotSolvingDailyProblemsDTOS(
      Set<MemberStudy> memberStudyList) {

    if (memberStudyList == null || memberStudyList.isEmpty()) {
      return Collections.emptySet();
    }

    return memberStudyList.stream()
        .map(MemberStudyMapper::toMemberNotSolvedDailyProblemsDTO)
        .collect(Collectors.toSet());
  }

  public static List<MemberNotSolvingDailyProblemsDTO> toMemberNotSolvingDailyProblemsDTOList(
      List<MemberStudy> memberStudyList) {

    if (memberStudyList == null || memberStudyList.isEmpty()) {
      return Collections.emptyList();
    }

    return memberStudyList.stream()
        .map(MemberStudyMapper::toMemberNotSolvedDailyProblemsDTO)
        .collect(Collectors.toList());
  }


  public static MemberNotSolvingDailyProblemsDTO toMemberNotSolvedDailyProblemsDTO(
      MemberStudy memberStudy) {

    return MemberNotSolvingDailyProblemsDTO.builder()
        .username(memberStudy.getMember().getUsername())
        .profileUrl(memberStudy.getMember().getProfileImageUrl())
        .nickname(memberStudy.getMember().getNickname())
        .build();
  }


}
