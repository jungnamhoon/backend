package com.hkorea.skyisthelimit.service;


import com.hkorea.skyisthelimit.common.utils.PromptUtil;
import com.hkorea.skyisthelimit.dto.ai.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.prompt.ProblemRecommendDTO;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisProblemService {

  private final MockOpenAiService mockOpenAiService;
  private final Executor aiTaskExecutor;
  private final ProblemSelectionService problemSelectionService;
  private final OpenAiService openAiService;

  public CompletableFuture<List<AiRecommendationProblem>> getRecommendedProblemAsync(String username, Criteria<QMemberProblem> criteria) {

    return CompletableFuture.supplyAsync(() -> {
      // 이 블록 안의 코드는 이제 'AI-Worker-' 스레드에서 돌아갑니다.
      return getRecommendedProblem(username, criteria);
    }, aiTaskExecutor);
  }

  public List<AiRecommendationProblem> getRecommendedProblem(String username,
      Criteria<QMemberProblem> criteria) {

    // 1. 추천에 필요한 데이터 가져오기
    ProblemRecommendDTO problemRecommendDTO = problemSelectionService.getRecommendationData(
        username, criteria);

    // 2. 수집된 데이터를 바탕으로 AI 호출
    Prompt problemRecommendPrompt = PromptUtil.createProblemRecommendPrompt(problemRecommendDTO);
//    String rawJson = openAiService.generate(problemRecommendPrompt);
    String rawJson = mockOpenAiService.generate(problemRecommendPrompt);

    BeanOutputConverter<List<AiRecommendationProblem>> converter =
        new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});

    List<AiRecommendationProblem> recommendedList = converter.convert(rawJson);
    return recommendedList.stream()
        .map(p -> new AiRecommendationProblem(
            p.problemId(),
            "https://www.acmicpc.net/problem/" + p.problemId(),
            p.reason()
        ))
        .toList();
  }

}
