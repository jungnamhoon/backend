package com.hkorea.skyisthelimit.analysis.service;

import com.hkorea.skyisthelimit.analysis.dto.internal.ProblemRecommendContext;
import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.analysis.entity.WrongReason;
import com.hkorea.skyisthelimit.analysis.exception.RecommendationNotFoundException;
import com.hkorea.skyisthelimit.analysis.repository.WeaknessRepository;
import com.hkorea.skyisthelimit.common.exception.ProblemNotFoundException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.problem.internal.ProblemBriefDTO;
import com.hkorea.skyisthelimit.entity.QProblem;
import com.hkorea.skyisthelimit.service.ProblemQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationDataFetcher {

  private final WeaknessRepository weaknessRepository;
  private final ProbabilityPicker probabilityPicker;
  private final WrongReasonSelector wrongReasonSelector;
  private final ProblemQueryService problemQueryService;

  @Transactional(readOnly = true)
  public ProblemRecommendContext fetchContext(String username, Criteria<QProblem> criteria) {

    List<Weakness> topWeaknesses = weaknessRepository.findTopWeaknesses(username, criteria, 5);

    if (topWeaknesses == null || topWeaknesses.isEmpty()) {
      throw new RecommendationNotFoundException(ErrorCode.RECOMMENDATION_DATA_NOT_FOUND);
    }

    Weakness selectedWeakness = probabilityPicker.selectWeaknessByWeight(topWeaknesses);
    WrongReason reason = wrongReasonSelector.selectRandomWrongReason(selectedWeakness);

    ProblemBriefDTO problemInfo;
    try {
      problemInfo = problemQueryService.getProblemInfo(reason.getProblemId());
    } catch (ProblemNotFoundException e) {
      log.error("[CRITICAL] 추천 데이터 정합성 오류: 사용자의 약점에 기록된 문제 번호({})가 DB에 존재하지 않음", reason.getProblemId());
      throw new RuntimeException("추천 로직 실행 중 시스템 무결성 오류 발생",e);
    }

    return new ProblemRecommendContext(
        problemInfo.baekjoonId(),
        problemInfo.tags(),
        reason.getReason()
    );
  }
}
