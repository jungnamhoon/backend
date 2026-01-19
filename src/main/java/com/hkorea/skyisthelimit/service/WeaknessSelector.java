package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.entity.Weakness;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class WeaknessSelector {

  private final Random random = new Random();

  /**
   * Weakness 리스트에서 frequency(빈도)를 가중치로 하여 하나의 약점을 무작위로 선택합니다.
   */
  public Weakness selectByWeight(List<Weakness> weaknesses) {
    if (weaknesses == null || weaknesses.isEmpty()) {
      return null;
    }

    // 1. 전체 가중치(frequency 합계) 계산
    int totalWeight = weaknesses.stream()
        .mapToInt(Weakness::getFrequency)
        .sum();

    // 모든 가중치가 0인 경우 방어 로직
    if (totalWeight <= 0) {
      return weaknesses.get(random.nextInt(weaknesses.size()));
    }

    // 2. 가중치 범위 내에서 난수 생성
    int r = random.nextInt(totalWeight);
    int cumulative = 0;

    // 3. 가중치 누적 합을 이용한 선택
    for (Weakness weakness : weaknesses) {
      cumulative += weakness.getFrequency();
      if (r < cumulative) {
        return weakness;
      }
    }

    // 4. 부동소수점 오차나 예외 상황을 대비한 안전장치
    return weaknesses.get(weaknesses.size() - 1);
  }
}
