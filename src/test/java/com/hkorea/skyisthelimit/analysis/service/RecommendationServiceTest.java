package com.hkorea.skyisthelimit.analysis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hkorea.skyisthelimit.analysis.dto.internal.ProblemRecommendContext;
import com.hkorea.skyisthelimit.analysis.dto.response.AiRecommendationProblem;
import com.hkorea.skyisthelimit.common.infrastructure.ai.OpenAiClient;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.entity.QProblem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.prompt.Prompt;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

  @Mock
  private RecommendationDataFetcher dataFetcher;

  @Mock
  private OpenAiClient openAiClient;

  @InjectMocks
  private RecommendationService recommendationService;

  @Test
  @DisplayName("성공: AI가 준 JSON 결과를 파싱하여 문제 리스트와 URL을 생성해야 한다")
  void getRecommendationProblems_Success() {

    // [Given]
    String username = "testUser";
    Criteria<QProblem> criteria = mock(Criteria.class);

    // 1. Fetcher가 넘겨줄 가짜 컨텍스트 (내용은 중요하지 않음)
    ProblemRecommendContext mockContext = mock(ProblemRecommendContext.class);
    given(dataFetcher.fetchContext(eq(username), any())).willReturn(mockContext);

    // 2. AI가 반환할 가짜 JSON (실제 BeanOutputConverter가 파싱할 형식)
    String mockRawJson = """
            [
                {"problemId": 1000, "reason": "그리디 기초를 다지기 좋습니다."},
                {"problemId": 1001, "reason": "구현 능력을 키울 수 있습니다."}
            ]
            """;

    given(openAiClient.generateText(any(Prompt.class))).willReturn(mockRawJson);

    // [When]
    List<AiRecommendationProblem> result = recommendationService.getRecommendationProblems(username, criteria);

    // [Then]
    assertThat(result).hasSize(2);

    // 첫 번째 문제 검증 (URL이 제대로 붙었는지 확인)
    assertThat(result.get(0).problemId()).isEqualTo(1000);
    assertThat(result.get(0).problemUrl()).isEqualTo("https://www.acmicpc.net/problem/1000");
    assertThat(result.get(0).reason()).isEqualTo("그리디 기초를 다지기 좋습니다.");

    // 두 번째 문제 검증
    assertThat(result.get(1).problemId()).isEqualTo(1001);
    assertThat(result.get(1).problemUrl()).isEqualTo("https://www.acmicpc.net/problem/1001");

  }

  @Test
  @DisplayName("실패: AI가 JSON 형식이 아닌 잘못된 문자열을 반환하면 RuntimeException이 발생한다")
  void getRecommendationProblems_ParsingFailure() {

    // [Given]
    String username = "testUser";
    Criteria<QProblem> criteria = mock(Criteria.class);

    // 1. Fetcher 정상 응답 설정
    ProblemRecommendContext mockContext = mock(ProblemRecommendContext.class);
    given(dataFetcher.fetchContext(eq(username), any())).willReturn(mockContext);

    // 2. AI가 JSON이 아닌 일반 텍스트나 깨진 JSON을 반환하는 상황 시뮬레이션
    String invalidJson = "응답이 JSON 형식이 아닙니다. { 깨진 데이터 ]";
    given(openAiClient.generateText(any(Prompt.class))).willReturn(invalidJson);

    // [When & Then]
    org.assertj.core.api.Assertions.assertThatThrownBy(() ->
            recommendationService.getRecommendationProblems(username, criteria))
        .isInstanceOf(RuntimeException.class);
  }

}