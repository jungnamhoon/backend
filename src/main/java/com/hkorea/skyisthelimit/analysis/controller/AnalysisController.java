package com.hkorea.skyisthelimit.analysis.controller;

import com.hkorea.skyisthelimit.analysis.dto.request.AnalysisCriteria;
import com.hkorea.skyisthelimit.analysis.service.RecommendationService;
import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.analysis.dto.response.AiRecommendationProblem;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class AnalysisController {

  private final RecommendationService recommendationService;

  @GetMapping("/me/recommended-problems")
  public CompletableFuture<ResponseEntity<ApiResponse<List<AiRecommendationProblem>>>> getAnalysisProblem(
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
      @ModelAttribute AnalysisCriteria criteria
  ) {
    return recommendationService.getRecommendationProblemsAsync(customOAuth2User.getUsername(), criteria)
        .thenApply(result -> ApiResponse.of(SuccessCode.OK, result));
  }
}
