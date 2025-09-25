package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.member.internal.MemberStatsDTO;
import com.hkorea.skyisthelimit.dto.member.response.MemberInfoResponse;
import com.hkorea.skyisthelimit.dto.member.response.MemberUpdateResponse;
import com.hkorea.skyisthelimit.dto.member.response.ProfileUpdateResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import com.hkorea.skyisthelimit.entity.Member;
import java.util.List;

public class MemberMapper {

  private MemberMapper() {
  }

  public static MemberInfoResponse toMemberInfoResponse(
      Member member, MemberStatsDTO memberStatsDTO) {
    return MemberInfoResponse.builder()
        .username(member.getUsername())
        .profileImageUrl(member.getProfileImageUrl())
        .nickname(member.getNickname())
        .score(member.getScore())
        .ranking(memberStatsDTO.getRanking())
        .totalSolvedProblems(member.getTotalSolvedProblems())
        .totalReviewNotes(member.getTotalReviewNotes())
        .streak(member.getStreak())
        .solvedProblemList(memberStatsDTO.getMemberProblemSolvedDTOList())
        .solvedCountList(memberStatsDTO.getMemberProblemSolvedCountByDayDTOList())
        .build();
  }

  public static MemberUpdateResponse toMemberUpdateResponse(Member member) {
    return MemberUpdateResponse.builder()
        .newNickname(member.getNickname())
        .build();
  }

  public static MemberStatsDTO toMemberStatsDTO(Integer ranking,
      List<MemberProblemSolvedDTO> memberProblemSolvedDTOList,
      List<MemberProblemSolvedCountByDayDTO> memberProblemSolvedCountByDayDTOList) {

    return MemberStatsDTO.builder()
        .ranking(ranking)
        .memberProblemSolvedDTOList(memberProblemSolvedDTOList)
        .memberProblemSolvedCountByDayDTOList(memberProblemSolvedCountByDayDTOList)
        .build();
  }

  public static ProfileUpdateResponse toProfileUpdateResponse(String imageUrl) {
    return ProfileUpdateResponse.builder()
        .imageUrl(imageUrl)
        .build();
  }
}
