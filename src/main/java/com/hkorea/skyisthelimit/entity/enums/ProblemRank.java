package com.hkorea.skyisthelimit.entity.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemRank {

  UNRATED("UNRATED", 0, "Unrated / Not Ratable"),
  BRONZE_V("BRONZE", 1, "Bronze V"),
  BRONZE_IV("BRONZE", 2, "Bronze IV"),
  BRONZE_III("BRONZE", 3, "Bronze III"),
  BRONZE_II("BRONZE", 4, "Bronze II"),
  BRONZE_I("BRONZE", 5, "Bronze I"),
  SILVER_V("SILVER", 6, "Silver V"),
  SILVER_IV("SILVER", 7, "Silver IV"),
  SILVER_III("SILVER", 8, "Silver III"),
  SILVER_II("SILVER", 9, "Silver II"),
  SILVER_I("SILVER", 10, "Silver I"),
  GOLD_V("GOLD", 11, "Gold V"),
  GOLD_IV("GOLD", 12, "Gold IV"),
  GOLD_III("GOLD", 13, "Gold III"),
  GOLD_II("GOLD", 14, "Gold II"),
  GOLD_I("GOLD", 15, "Gold I"),
  PLATINUM_V("PLATINUM", 16, "Platinum V"),
  PLATINUM_IV("PLATINUM", 17, "Platinum IV"),
  PLATINUM_III("PLATINUM", 18, "Platinum III"),
  PLATINUM_II("PLATINUM", 19, "Platinum II"),
  PLATINUM_I("PLATINUM", 20, "Platinum I"),
  DIAMOND_V("DIAMOND", 21, "Diamond V"),
  DIAMOND_IV("DIAMOND", 22, "Diamond IV"),
  DIAMOND_III("DIAMOND", 23, "Diamond III"),
  DIAMOND_II("DIAMOND", 24, "Diamond II"),
  DIAMOND_I("DIAMOND", 25, "Diamond I"),
  RUBY_V("RUBY", 26, "Ruby V"),
  RUBY_IV("RUBY", 27, "Ruby IV"),
  RUBY_III("RUBY", 28, "Ruby III"),
  RUBY_II("RUBY", 29, "Ruby II"),
  RUBY_I("RUBY", 30, "Ruby I");

  private static final Map<Integer, ProblemRank> LEVEL_MAP = new HashMap<>();
  private static final Map<String, ProblemRank[]> TIER_MAP = new HashMap<>();

  static {

    // Level 기반 map
    for (ProblemRank pl : values()) {
      LEVEL_MAP.put(pl.level, pl);
    }
  }

  private final String tier;
  private final Integer level;
  private final String description;

  /**
   * 레벨로 ProblemRank 가져오기
   */
  public static ProblemRank fromLevel(Integer level) {

    if (level == null) {
      throw new IllegalArgumentException("Level cannot be null");
    }

    return LEVEL_MAP.get(level);
  }

}
