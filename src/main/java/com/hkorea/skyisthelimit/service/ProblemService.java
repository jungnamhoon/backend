package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.dto.problem.internal.SolvedAcProblemResponseDTO;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.repository.ProblemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProblemService {

  private static final String SOLVEDAC_API_URL = "https://solved.ac/api/v3/problem/show?problemId={problemId}";
  private final RestTemplate restTemplate;
  private final ProblemRepository problemRepository;

  @Transactional
  public Problem getOrRegisterProblem(Integer baekjoonId) {
    return problemRepository.findById(baekjoonId)
        .orElseGet(() -> registerProblem(baekjoonId));
  }

  private Problem registerProblem(Integer baekjoonId) {

    SolvedAcProblemResponseDTO solvedAcProblemResponseDTO = fetchProblemFromSolvedAc(baekjoonId);

    Problem problem = solvedAcProblemResponseDTO.toEntity();

    validateProblem(problem);

    return problemRepository.save(problem);

  }

  private SolvedAcProblemResponseDTO fetchProblemFromSolvedAc(Integer baekjoonId) {
    try {
      return restTemplate.getForObject(SOLVEDAC_API_URL, SolvedAcProblemResponseDTO.class,
          baekjoonId);
    } catch (HttpClientErrorException.NotFound e) {
      throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
    } catch (RestClientException e) {
      throw new BusinessException(ErrorCode.EXTERNAL_API_ERROR);
    }
  }

  private void validateProblem(Problem problem) {
    if (problem == null) {
      throw new BusinessException(ErrorCode.PROBLEM_NOT_FOUND);
    }
  }


}
