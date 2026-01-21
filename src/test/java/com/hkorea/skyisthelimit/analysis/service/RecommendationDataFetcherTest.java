package com.hkorea.skyisthelimit.analysis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendationDataFetcherTest {

  @Mock
  private WeaknessRepository weaknessRepository;
  @Mock
  private ProbabilityPicker probabilityPicker;
  @Mock
  private WrongReasonSelector wrongReasonSelector;
  @Mock
  private ProblemQueryService problemQueryService;

  @InjectMocks
  private RecommendationDataFetcher fetcher;

  @Test
  @DisplayName("성공 시나리오: 모든 데이터가 존재하면 추천 컨텍스트를 반환한다")
  void fetchContext_Success() {

    // [Given]
    Weakness mockWeakness = mock(Weakness.class);
    WrongReason mockReason = mock(WrongReason.class);
    ProblemBriefDTO mockDto = new ProblemBriefDTO(1000, "A+B", List.of("수학"));
    Criteria<QProblem> mockCriteria = mock(Criteria.class);

    given(weaknessRepository.findTopWeaknesses(anyString(), any(), anyInt()))
        .willReturn(List.of(mockWeakness));
    given(probabilityPicker.selectWeaknessByWeight(any())).willReturn(mockWeakness);
    given(wrongReasonSelector.selectRandomWrongReason(mockWeakness)).willReturn(mockReason);
    given(mockReason.getProblemId()).willReturn(1000);
    given(mockReason.getReason()).willReturn("단순 계산 실수");
    given(problemQueryService.getProblemInfo(1000)).willReturn(mockDto);

    // [When]
    ProblemRecommendContext result = fetcher.fetchContext("user1", mockCriteria);

    // [Then]
    assertThat(result).isNotNull();
    assertThat(result.baekjoonId()).isEqualTo(1000);
    assertThat(result.tags()).isEqualTo(List.of("수학"));
    assertThat(result.wrongReason()).isEqualTo("단순 계산 실수");
  }

  @Test
  @DisplayName("비즈니스 예외: 취약점 데이터가 조회되지 않으면 RecommendationNotFoundException이 발생한다")
  void fetchContext_NoData_ThrowsRecommendationNotFoundException() {

    // [Given] 리포지토리에서 빈 리스트 반환 시뮬레이션
    given(weaknessRepository.findTopWeaknesses(anyString(), any(), anyInt()))
        .willReturn(List.of());

    // [When & Then]
    assertThatThrownBy(() -> fetcher.fetchContext("user1", mock(Criteria.class)))
        .isInstanceOf(RecommendationNotFoundException.class)
        .hasMessageContaining(ErrorCode.RECOMMENDATION_DATA_NOT_FOUND.getMessage());
  }

  @Test
  @DisplayName("시스템 무결성 예외: 약점 정보는 있으나 실제 문제 정보가 DB에 없으면 RuntimeException(500)이 발생한다")
  void fetchContext_IntegrityError_ThrowsRuntimeException() {
    // [Given]
    Weakness mockWeakness = mock(Weakness.class);
    WrongReason mockReason = mock(WrongReason.class);

    given(weaknessRepository.findTopWeaknesses(anyString(), any(), anyInt())).willReturn(List.of(mockWeakness));
    given(probabilityPicker.selectWeaknessByWeight(any())).willReturn(mockWeakness);
    given(wrongReasonSelector.selectRandomWrongReason(any())).willReturn(mockReason);
    given(mockReason.getProblemId()).willReturn(999);

    // ProblemQueryService에서 404 성격의 예외를 던지도록 설정
    given(problemQueryService.getProblemInfo(999))
        .willThrow(new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

    // [When & Then]
    // Fetcher 내부에서 이를 catch하여 RuntimeException으로 래핑했는지 검증
    assertThatThrownBy(() -> fetcher.fetchContext("user1", mock(Criteria.class)))
        .isExactlyInstanceOf(RuntimeException.class)
        .hasMessageContaining("추천 로직 실행 중 시스템 무결성 오류 발생")
        .hasCauseInstanceOf(ProblemNotFoundException.class);
  }
}

