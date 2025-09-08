package com.hkorea.skyisthelimit.dto.member.response;

import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedCountByDayDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.internal.MemberProblemSolvedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoResponse {

  @Schema(title = "사용자 이름", description = "회원의 고유 username", example = "user1")
  private String username;

  @Schema(title = "프로필 이미지 URL", description = "회원의 프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
  private String profileImageUrl;

  @Schema(title = "닉네임", description = "회원이 설정한 닉네임", example = "monkey1")
  private String nickname;

  @Schema(title = "점수", description = "회원의 총 점수", example = "240")
  private Integer score;

  @Schema(title = "랭킹", description = "회원의 현재 랭킹", example = "17")
  private Integer ranking;

  @Schema(title = "총 푼 문제 수", description = "회원이 지금까지 푼 문제 수", example = "34")
  private Integer totalSolvedProblems;

  @Schema(title = "총 오답 노트 수", description = "회원이 작성한 오답 노트 수", example = "20")
  private Integer totalReviewNotes;

  @Schema(title = "연속 학습 일수", description = "회원의 연속 학습 일수", example = "12")
  private Integer streak;

  @Schema(title = "푼 문제 리스트", description = "회원이 푼 문제 리스트 (SOLVED, MULTI_TRY 등 상태 포함)")
  private List<MemberProblemSolvedDTO> solvedProblemList;

  @Schema(title = "일별 문제 풀이 수", description = "회원이 날짜별로 푼 문제 수 리스트")
  private List<MemberProblemSolvedCountByDayDTO> solvedCountList;

}
