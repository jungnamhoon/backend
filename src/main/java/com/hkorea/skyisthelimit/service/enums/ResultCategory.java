package com.hkorea.skyisthelimit.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCategory {

  AC("맞았습니다", "ac"),
  PE("출력 형식 에러", "pe"),
  WA("틀렸습니다", "wa"),
  TLE("시간 초과", "tle"),
  MLE("메모리 초과", "mle"),
  RTE("런타임 에러", "rte"),
  CE("컴파일 에러", "ce");

  private final String description;
  private final String code;

  @JsonCreator
  public static ResultCategory fromCode(String code) {
    for (ResultCategory category : values()) {
      if (category.code.equalsIgnoreCase(code)) {
        return category;
      }
    }
    throw new IllegalArgumentException("Invalid ResultCategory code: " + code);
  }
}
