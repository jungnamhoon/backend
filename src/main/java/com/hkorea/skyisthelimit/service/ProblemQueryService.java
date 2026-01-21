package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.ProblemNotFoundException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.dto.problem.internal.ProblemBriefDTO;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.repository.ProblemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemQueryService {

  private final ProblemRepository problemRepository;

  public ProblemBriefDTO getProblemInfo(Integer baekjoonId) {
    return problemRepository.findById(baekjoonId)
        .map(p -> new ProblemBriefDTO(
            p.getBaekjoonId(),
            p.getTitle(),
            p.getProblemTagList().stream().map(ProblemTag::getKoName).toList()
        ))
        .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));
  }
}
