package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.prompt.ProblemRecommendDTO;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.Weakness;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AnalysisProblemMapper {


  public ProblemRecommendDTO toProblemRecommendDTO(Weakness weakness, Problem problem) {

    List<String> tagNames = problem.getProblemTagList().stream()
        .map(ProblemTag::getKoName)
        .collect(Collectors.toList());

    return new ProblemRecommendDTO(
        problem.getBaekjoonId(),
        tagNames,
        weakness.getWeaknessSummary()
    );

  }

}
