package com.hkorea.skyisthelimit.analysis.service;

import com.hkorea.skyisthelimit.analysis.dto.internal.ProblemRecommendContext;
import com.hkorea.skyisthelimit.analysis.entity.QWeakness;
import com.hkorea.skyisthelimit.common.infrastructure.ai.MockOpenAiClient;
import com.hkorea.skyisthelimit.common.infrastructure.ai.OpenAiClient;
import com.hkorea.skyisthelimit.analysis.util.PromptUtil;
import com.hkorea.skyisthelimit.analysis.dto.response.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.entity.QProblem;
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
public class RecommendationService {

  private final RecommendationDataFetcher dataFetcher;
  private final OpenAiClient openAiClient;
//  private final MockOpenAiClient mockOpenAiClient;
  private final Executor aiTaskExecutor;

  public CompletableFuture<List<AiRecommendationProblem>> getRecommendationProblemsAsync(String username, Criteria<QProblem> criteria) {

    return CompletableFuture.supplyAsync(() -> getRecommendationProblems(username, criteria),
        aiTaskExecutor);
  }

  public List<AiRecommendationProblem> getRecommendationProblems(String username, Criteria<QProblem> criteria) {

    ProblemRecommendContext problemRecommendContext = dataFetcher.fetchContext(username, criteria);

    Prompt problemRecommendPrompt = PromptUtil.createProblemRecommendPrompt(problemRecommendContext);
    String rawJson = openAiClient.generateText(problemRecommendPrompt);
//    String rawJson = mockOpenAiClient.generate(problemRecommendPrompt);

    List<AiRecommendationProblem> recommendationProblems = parseJson(rawJson);

    return addProblemUrl(recommendationProblems);
  }

  private List<AiRecommendationProblem> parseJson(String rawJson) {
    BeanOutputConverter<List<AiRecommendationProblem>> converter = new BeanOutputConverter<>(
        new ParameterizedTypeReference<>() {});
    return converter.convert(rawJson);
  }

  private List<AiRecommendationProblem> addProblemUrl(List<AiRecommendationProblem> recommendationProblems) {
    return recommendationProblems.stream()
        .map(p -> new AiRecommendationProblem(
            p.problemId(),
            "https://www.acmicpc.net/problem/" + p.problemId(),
            p.reason()
        ))
        .toList();
  }

}
