package com.hkorea.skyisthelimit.analysis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProbabilityPickerTest {

  private final ProbabilityPicker picker = new ProbabilityPicker();

  @Test
  @DisplayName("약점 리스트가 주어지면 null이 아닌 약점을 반환해야 한다")
  void selectWeaknessByWeight() {

    // [Given]
    Weakness w1 = new Weakness("약점1", 10, new float[1536]);
    Weakness w2 = new Weakness("약점2", 1, new float[1536]);
    List<Weakness> weaknesses = List.of(w1, w2);

    // [When]
    Weakness selected = picker.selectWeaknessByWeight(weaknesses);

    // [Then]
    assertThat(selected).isNotNull();

  }

  @Test
  @DisplayName("가중치가 0인 항목은 선택되지 않아야 하고, 가중치가 100%인 항목은 반드시 선택되어야 한다")
  void selectWeaknessByWeight_EdgeCase() {
    // [Given] 하나는 0, 하나는 100인 극단적인 상황
    Weakness zeroWeight = new Weakness("0%약점", 0, new float[1536]);
    Weakness fullWeight = new Weakness("100%약점", 100, new float[1536]);
    List<Weakness> weaknesses = List.of(zeroWeight, fullWeight);

    // [When & Then] 여러 번 반복해도 100% 약점만 나와야 함 (결정론적 테스트)
    for (int i = 0; i < 50; i++) {
      Weakness selected = picker.selectWeaknessByWeight(weaknesses);
      assertThat(selected.getWeaknessSummary()).isEqualTo("100%약점");
    }
  }

  @Test
  @DisplayName("조건에 맞는 약점 데이터가 조회되지 않은 경우(null 또는 빈 리스트) null을 반환한다")
  void selectWeaknessByWeight_WhenNoDataFound() {
    // 1. 리포지토리에서 조회가 안 되어 null이 넘어온 상황 가정
    assertThat(picker.selectWeaknessByWeight(null))
        .as("조회된 약점 리스트가 null이면 결과도 null이어야 함")
        .isNull();

    // 2. 검색 조건에 맞는 데이터가 없어 빈 리스트가 넘어온 상황 가정
    assertThat(picker.selectWeaknessByWeight(List.of()))
        .as("조회된 약점이 하나도 없으면 결과도 null이어야 함")
        .isNull();
  }
}