package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.entity.Member;

public class MemberMapper {

  private MemberMapper() {
  }

  public static MemberInfoResponse toMemberInfoResponse(Member member) {
    return MemberInfoResponse.builder()
        .username(member.getUsername())
        .profileImageUrl(member.getProfileImageUrl())
        .nickname(member.getNickname())
        .score(member.getScore())
        .ranking(member.getRanking())
        .totalSolvedProblems(member.getTotalSolvedProblems())
        .totalReviewNotes(member.getTotalReviewNotes())
        .streak(member.getStreak())
        .solvedProblemList(
            MemberProblemMapper.toMemberProblemSolvedDTOList(member.getMemberProblems()))
        .solvedCountList(MemberProblemMapper.toMemberProblemSolvedCountByDayDTOList(
            member.getMemberProblems()))
        .build();
  }

  public static MemberUpdateResponse toMemberUpdateResponse(Member member) {
    return MemberUpdateResponse.builder()
        .newNickname(member.getNickname())
        .newProfileImageUrl(member.getProfileImageUrl())
        .build();
  }
}
