package com.hkorea.skyisthelimit.analysis.service;

import com.hkorea.skyisthelimit.analysis.entity.Weakness;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class ProbabilityPicker {

  private final Random random = new Random();

  public Weakness selectWeaknessByWeight(List<Weakness> weaknesses) {

    if (weaknesses == null || weaknesses.isEmpty()) {
      return null;
    }

    int totalWeight = weaknesses.stream()
        .mapToInt(Weakness::getFrequency)
        .sum();

    int r = random.nextInt(totalWeight);
    int cumulative = 0;

    for (Weakness w : weaknesses) {
      cumulative += w.getFrequency();
      if (r < cumulative) {
        return w;
      }
    }
    return weaknesses.get(weaknesses.size() - 1);
  }
}
