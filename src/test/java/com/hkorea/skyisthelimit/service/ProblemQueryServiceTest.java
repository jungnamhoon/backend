package com.hkorea.skyisthelimit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hkorea.skyisthelimit.common.exception.ProblemNotFoundException;
import com.hkorea.skyisthelimit.dto.problem.internal.ProblemBriefDTO;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.repository.ProblemRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProblemQueryServiceTest {

  @Mock
  private ProblemRepository problemRepository;

  @InjectMocks
  private ProblemQueryService problemQueryService;

  @Test
  @DisplayName("문제 ID로 조회 시 존재하는 문제라면 DTO로 변환하여 반환한다")
  void getProblemInfo() {

    // [Given]
    Integer targetId = 1000;
    ProblemTag tag = ProblemTag.builder().koName("다이나믹 프로그래밍").build();

    // 가짜 Problem 엔티티 생성
    Problem mockProblem = mock(Problem.class);
    given(mockProblem.getBaekjoonId()).willReturn(targetId);
    given(mockProblem.getTitle()).willReturn("A+B");
    given(mockProblem.getProblemTagList()).willReturn(List.of(tag));

    given(problemRepository.findById(targetId)).willReturn(Optional.of(mockProblem));

    // [When]
    ProblemBriefDTO result = problemQueryService.getProblemInfo(targetId);

    // [Then]
    assertThat(result).isNotNull();
    assertThat(result.baekjoonId()).isEqualTo(targetId);
    assertThat(result.title()).isEqualTo("A+B");
    assertThat(result.tags()).containsExactly("다이나믹 프로그래밍");

  }

  @Test
  @DisplayName("존재하지 않는 문제 ID로 조회 시 EntityNotFoundException이 발생한다")
  void getProblemInfo_NotFound() {
    // [Given]
    Integer nonExistentId = 9999;
    given(problemRepository.findById(nonExistentId)).willReturn(Optional.empty());

    // [When & Then]
    assertThatThrownBy(() -> problemQueryService.getProblemInfo(nonExistentId))
        .isInstanceOf(ProblemNotFoundException.class)
        .hasMessageContaining("문제를 찾을 수 없습니다.");
  }
}