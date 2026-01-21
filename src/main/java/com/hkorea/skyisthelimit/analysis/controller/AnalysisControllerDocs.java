package com.hkorea.skyisthelimit.analysis.controller;

import com.hkorea.skyisthelimit.analysis.dto.request.AnalysisCriteria;
import com.hkorea.skyisthelimit.analysis.dto.response.AiRecommendationProblem;
import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(
    name = "Analysis",
    description = "문제 풀이 기록을 기반으로 한 AI 문제 추천 API - JWT 토큰 필요"
)
public interface AnalysisControllerDocs {

  @Operation(
      summary = "AI 추천 문제 조회",
      description = """
          특정 사용자의 문제 풀이 기록을 분석하여
          AI 기반 추천 문제 목록을 조회합니다.
          """
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "AI 추천 문제 조회 성공"
      )
  })
  CompletableFuture<ResponseEntity<ApiResponse<List<AiRecommendationProblem>>>> getAnalysisProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute AnalysisCriteria criteria);
}