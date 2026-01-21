package com.hkorea.skyisthelimit.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCategory {

  AC("맞았습니다", "ac",null),
  PE("출력 형식 에러", "pe",1L),
  WA("틀렸습니다", "wa",null),
  TLE("시간 초과", "tle",2L),
  MLE("메모리 초과", "mle",3L),
  RTE("런타임 에러", "rte",4L),
  CE("컴파일 에러", "ce",5L);

  private final String description;
  private final String code;
  private final Long weaknessId;

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
