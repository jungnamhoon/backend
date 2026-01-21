package com.hkorea.skyisthelimit.analysis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.analysis.entity.WrongReason;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WrongReasonSelectorTest {

  private final WrongReasonSelector selector = new WrongReasonSelector();

  @Test
  @DisplayName("약점이 주어지면 해당 약점에 속한 오답 원인 중 하나를 반환해야 한다")
  void selectRandomWrongReason_Success() {

    // [Given]
    // Weakness와 연결된 WrongReason들을 가짜(Mock) 객체로 준비
    Weakness weakness = mock(Weakness.class);
    WrongReason wr1 = mock(WrongReason.class);
    WrongReason wr2 = mock(WrongReason.class);
    List<WrongReason> wrongReasons = List.of(wr1, wr2);

    // weakness.getWrongReasons() 호출 시 준비한 리스트를 반환하도록 설정
    given(weakness.getWrongReasons()).willReturn(wrongReasons);

    // [When]
    WrongReason selected = selector.selectRandomWrongReason(weakness);

    // [Then]
    assertThat(selected).isNotNull();
    assertThat(wrongReasons).contains(selected);
  }

  @Test
  @DisplayName("전달된 약점이 null이면 null을 반환한다")
  void selectRandomWrongReason_WhenWeaknessIsNull() {
    // [When]
    WrongReason result = selector.selectRandomWrongReason(null);

    // [Then]
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("약점에 연결된 오답 원인이 하나뿐이라면 반드시 그 하나가 선택되어야 한다")
  void selectRandomWrongReason_SingleCase() {
    // [Given]
    Weakness weakness = mock(Weakness.class);
    WrongReason onlyOne = mock(WrongReason.class);
    given(weakness.getWrongReasons()).willReturn(List.of(onlyOne));

    // [When]
    WrongReason result = selector.selectRandomWrongReason(weakness);

    // [Then]
    assertThat(result).isEqualTo(onlyOne);
  }

}