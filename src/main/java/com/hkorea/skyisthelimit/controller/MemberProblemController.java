package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.controller.docs.MemberProblemControllerDocs;
import com.hkorea.skyisthelimit.dto.ai.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.criteria.MemberProblemCriteria;
import com.hkorea.skyisthelimit.dto.criteria.RandomProblemCriteria;
import com.hkorea.skyisthelimit.dto.memberproblem.request.MemberProblemTagCountResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.request.NoteRequestDTO;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveRequest;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.NoteResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.RandomProblemResponse;
import com.hkorea.skyisthelimit.service.AnalysisProblemService;
import com.hkorea.skyisthelimit.service.MemberProblemService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberProblemController implements MemberProblemControllerDocs {

  private final MemberProblemService memberProblemService;
  private final AnalysisProblemService analysisProblemService;

  @GetMapping("/me/problems")
  public ResponseEntity<ApiResponse<Page<MemberProblemResponse>>> getMemberProblemPage(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute MemberProblemCriteria criteria) {

    Page<MemberProblemResponse> responsePage = memberProblemService.getMemberProblemPage(
        customOAuth2User.getUsername(),
        criteria);
    return ApiResponse.of(SuccessCode.OK, responsePage);
  }

  @PostMapping("/me/problems")
  public ResponseEntity<ApiResponse<SolveResponse>> solveProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @RequestBody SolveRequest requestDTO) {

    SolveResponse responseDTO = memberProblemService.solveProblem(customOAuth2User.getUsername(), requestDTO);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/me/problems/{baekjoonId}/note")
  public ResponseEntity<ApiResponse<NoteResponse>> getNote(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Integer baekjoonId) {

    NoteResponse responseDTO = memberProblemService.getNote(
        customOAuth2User.getUsername(),
        baekjoonId);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @PostMapping("/me/problems/{baekjoonId}/note")
  public ResponseEntity<ApiResponse<NoteResponse>> writeNote(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @PathVariable Integer baekjoonId, @RequestBody NoteRequestDTO requestDTO) {

    NoteResponse responseDTO = memberProblemService.writeNote(
        customOAuth2User.getUsername(),
        baekjoonId,
        requestDTO.getContent());
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/me/random-problem")
  public ResponseEntity<ApiResponse<RandomProblemResponse>> getRandomProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute RandomProblemCriteria criteria) {

    RandomProblemResponse responseDTO = memberProblemService.getRandomProblem(
        customOAuth2User.getUsername(),
        criteria);
    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }

  @GetMapping("/me/analysis-problem")
  public CompletableFuture<ResponseEntity<ApiResponse<List<AiRecommendationProblem>>>> getAnalysisProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute RandomProblemCriteria criteria) {

//    System.out.println("1. [Tomcat] 요청 진입: " + customOAuth2User.getUsername());

    return analysisProblemService.getRecommendedProblemAsync(customOAuth2User.getUsername(), criteria)
        .thenApply(result -> {
//          System.out.println("2. [Async] 데이터 확보 완료: " + (result != null ? result.size() : "null"));
          return ApiResponse.of(SuccessCode.OK, result);
        })
        .exceptionally(ex -> {
          // 💡 여기서 비동기 스레드 내부의 에러를 잡습니다.
          System.err.println("🚨 [Async Error] 비동기 작업 중 예외 발생!");
          System.err.println("에러 메시지: " + ex.getMessage());
          System.err.println("에러 원인: " + ex.getCause());
          ex.printStackTrace();

          // 에러 발생 시 클라이언트에게 에러 응답을 반환하도록 설정
          // (ErrorCode는 본인의 프로젝트에 맞게 수정하세요)
          return ResponseEntity.status(500).body(null);
        });
  }
  @GetMapping("/me/statistics")
  public ResponseEntity<ApiResponse<List<MemberProblemTagCountResponse>>> getMemberStatistics(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    List<MemberProblemTagCountResponse> responseDTOList = memberProblemService.getMemberStatistics(
        customOAuth2User.getUsername());
    return ApiResponse.of(SuccessCode.OK, responseDTOList);
  }

}