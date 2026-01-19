package com.hkorea.skyisthelimit.service;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.*;

import com.hkorea.skyisthelimit.common.utils.PromptUtil;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.AnalysisProblemMapper;
import com.hkorea.skyisthelimit.dto.ai.AiRecommendationProblem;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.prompt.ProblemRecommendDTO;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.Weakness;
import com.hkorea.skyisthelimit.entity.WrongReason;
import com.hkorea.skyisthelimit.repository.WeaknessRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisProblemService {

  private final QueryDSLHelper queryDSLHelper;
  private final OpenAiService openAiService;
  private final WeaknessSelector weaknessSelector;
  private final AnalysisProblemMapper analysisProblemMapper;
  private final WeaknessRepository weaknessRepository;

  @Transactional
  public List<AiRecommendationProblem> getRecommendedProblem(String username,
      Criteria<QMemberProblem> criteria) {

    // 1. MemberProblems 가져오기
    QMemberProblem memberProblem = QMemberProblem.memberProblem;
    BooleanExpression predicate = criteria.toPredicate().and(usernameEq(username));
    List<MemberProblem> selectedMemberProblems = queryDSLHelper.fetchEntities(memberProblem, predicate);

    List<Weakness> topFiveWeaknesses = weaknessRepository.findTopFiveWeaknesses(selectedMemberProblems);
    Weakness selectedWeakness = weaknessSelector.selectByWeight(topFiveWeaknesses);

    List<WrongReason> wrongReasons = selectedWeakness.getWrongReasons();
    int randomIndex = new Random().nextInt(wrongReasons.size());
    Problem selectedProblem = wrongReasons.get(randomIndex).getMemberProblem().getProblem();

    ProblemRecommendDTO problemRecommendDTO = analysisProblemMapper.toProblemRecommendDTO(selectedWeakness,selectedProblem);

    Prompt problemRecommendPrompt = PromptUtil.createProblemRecommendPrompt(problemRecommendDTO);
    String rawJson = openAiService.generate(problemRecommendPrompt);

    BeanOutputConverter<List<AiRecommendationProblem>> converter =
        new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});

    return converter.convert(rawJson);
  }

}
