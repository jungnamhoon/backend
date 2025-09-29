package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.dto.criteria.MemberProblemCriteria;
import com.hkorea.skyisthelimit.dto.criteria.RandomProblemCriteria;
import com.hkorea.skyisthelimit.dto.memberproblem.request.MemberProblemTagCountResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.request.NoteRequestDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveRequest;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.NoteResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.RandomProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "MemberProblem", description = "회원이 푼 문제 관련 API - JWT 토큰 필요")
public interface MemberProblemControllerDocs {

  @Operation(
      summary = "사용자 문제 조회",
      description = "특정 사용자가 시도한 문제 목록을 조건에 맞게 조회합니다.<br>" +
          "필터(levelStart, levelEnd, status, solvedCount, noteWritten)와 검색(search), " +
          "정렬(sort, direction), 페이징(page, size)을 지원합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "문제 목록 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<Page<MemberProblemResponse>>> getMemberProblemPage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute MemberProblemCriteria criteria
  );

  @Operation(
      summary = "문제 시도",
      description = """
          사용자가 문제를 시도했을 때 호출되는 API입니다.
          
          ✅ 사용 예시
          - 사용자가 문제를 성공적으로 풀었을 때: isSolved = true
          - 사용자가 문제를 풀지 못했을 때: isSolved = false
          
          Extension 에서 문제 시도 시 호출합니다.
          """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "문제 풀이 기록 등록/수정"
      )
  })
  public ResponseEntity<ApiResponse<SolveResponse>> solveProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody SolveRequest requestDTO);

  @Operation(
      summary = "오답노트 조회",
      description = "특정 사용자가 푼 문제의 오답노트를 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "오답노트 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<NoteResponse>> getNote(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Integer baekjoonId
  );

  @Operation(
      summary = "오답노트 작성/수정",
      description = "특정 사용자가 특정 문제에 대한 오답노트를 작성하거나 기존 노트를 수정합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "오답노트 작성/수정 성공"
      )
  })
  ResponseEntity<ApiResponse<NoteResponse>> writeNote(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Integer baekjoonId,
      @RequestBody NoteRequestDTO requestDTO
  );

  @Operation(
      summary = "랜덤 문제 뽑기",
      description = "특정 사용자의 조건에 맞는 랜덤 문제를 뽑습니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "랜덤 문제 뽑기 성공"
      )
  })
  ResponseEntity<ApiResponse<RandomProblemResponse>> getRandomProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute RandomProblemCriteria criteria
  );

  @Operation(
      summary = "사용자 알고리즘별 문제 풀이 통계 조회",
      description = "특정 사용자가 푼 문제를 알고리즘 태그별로 집계하여 조회합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "사용자 알고리즘별 문제 풀이 통계 조회 성공"
      )
  })
  ResponseEntity<ApiResponse<List<MemberProblemTagCountResponse>>> getMemberStatistics(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User);

}
