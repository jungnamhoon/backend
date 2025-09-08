package com.hkorea.skyisthelimit.common.utils.mapper;

import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse.ProblemTagDTO;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import java.util.List;

public class ProblemMapper {

  private ProblemMapper() {
  }

  public static List<ProblemTagDTO> toProblemTagDTOList(List<ProblemTag> problemTagList) {

    return problemTagList.stream()
        .map(problemTag -> ProblemTagDTO.builder()
            .enName(problemTag.getEnName())
            .koName(problemTag.getKoName())
            .build())
        .toList();
  }

}
