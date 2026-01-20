package com.hkorea.skyisthelimit.service;


import com.hkorea.skyisthelimit.common.utils.PromptUtil;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.AnalysisProblemMapper;
import com.hkorea.skyisthelimit.dto.ai.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.prompt.ProblemRecommendDTO;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.repository.WeaknessRepository;
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
    String rawJson = mockOpenAiService.generate(problemRecommendPrompt);


    // 3. Json 문자열을 파싱하여 최종 객체 리스트로 변환
    BeanOutputConverter<List<AiRecommendationProblem>> converter =
        new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});

    return converter.convert(rawJson);
  }

}
