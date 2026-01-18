package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.WeaknessStat;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class WeaknessSelector {

  public WeaknessStat selectByWeight(List<WeaknessStat> stats) {
    if (stats.isEmpty()) {
      return null;
    }

    int totalWeight = stats.stream()
        .mapToInt(WeaknessStat::count)
        .sum();

    int r = new Random().nextInt(totalWeight);
    int cumulative = 0;

    for (WeaknessStat stat : stats) {
      cumulative += stat.count();
      if (r < cumulative) {
        return stat;
      }
    }
    return stats.get(0); // 안전장치
  }
}
