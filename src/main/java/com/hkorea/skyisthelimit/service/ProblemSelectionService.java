package com.hkorea.skyisthelimit.service;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.usernameEq;

import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.AnalysisProblemMapper;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.prompt.ProblemRecommendDTO;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.Weakness;
import com.hkorea.skyisthelimit.entity.WrongReason;
import com.hkorea.skyisthelimit.repository.WeaknessRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemSelectionService {

  private final QueryDSLHelper queryDSLHelper;
  private final WeaknessRepository weaknessRepository;
  private final WeaknessSelector weaknessSelector;
  private final AnalysisProblemMapper analysisProblemMapper;

  @Transactional(readOnly = true)
  public ProblemRecommendDTO getRecommendationData(String username, Criteria<QMemberProblem> criteria) {
    // 1. 유저의 문제 기록 조회
    QMemberProblem memberProblem = QMemberProblem.memberProblem;
    BooleanExpression predicate = criteria.toPredicate().and(usernameEq(username));
    List<MemberProblem> selectedMemberProblems = queryDSLHelper.fetchEntities(memberProblem, predicate);

    // 2. 상위 5개 약점 집계 및 선택
    List<Weakness> topFiveWeaknesses = weaknessRepository.findTopFiveWeaknesses(selectedMemberProblems);

    if (topFiveWeaknesses.isEmpty()) {
      return null;
    }

    Weakness selectedWeakness = weaknessSelector.selectByWeight(topFiveWeaknesses);

    // 지연 로딩 발생 지점: 트랜잭션 안이므로 안전하게 로드됨
    List<WrongReason> wrongReasons = selectedWeakness.getWrongReasons();
    int randomIndex = new Random().nextInt(wrongReasons.size());
    Problem selectedProblem = wrongReasons.get(randomIndex).getMemberProblem().getProblem();

    // 엔티티를 DTO로 변환 (트랜잭션 밖으로 나갈 준비)
    return analysisProblemMapper.toProblemRecommendDTO(selectedWeakness, selectedProblem);
  }
}
