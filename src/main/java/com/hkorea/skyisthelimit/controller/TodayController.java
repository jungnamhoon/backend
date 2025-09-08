package com.hkorea.skyisthelimit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/day")
@Tag(name = "Today", description = "날짜 관련 API - JWT 토큰 필요/프론트 엔드는 건들지 말 것")
public class TodayController {

  private static LocalDate today = LocalDate.now();

  public static LocalDate getToday() {
    return today;
  }

  @Operation(
      summary = "오늘 날짜 조회 (테스트용)",
      description = "단순히 API 테스트용으로 제공하며, 프론트엔드에서 사용하지 않습니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "오늘 날짜 조회 성공")
  })
  @GetMapping
  public LocalDate getTodayApi() {
    return today;
  }

  @Operation(
      summary = "오늘 날짜 설정 (테스트용)",
      description = "단순히 API 테스트용으로 제공하며, 프론트엔드에서 사용하지 않습니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "오늘 날짜 설정 성공")
  })
  @PostMapping
  public LocalDate setTodayApi(
      @Parameter(description = "설정할 날짜(yyyy-MM-dd)", example = "2025-09-03")
      @RequestParam String date) {
    today = LocalDate.parse(date);
    return today;
  }

  @Operation(
      summary = "오늘 날짜 초기화 (테스트용)",
      description = "단순히 API 테스트용으로 제공하며, 프론트엔드에서 사용하지 않습니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "오늘 날짜를 현재 날짜로 초기화 성공")
  })
  @PostMapping("/reset")
  public LocalDate resetTodayApi() {
    today = LocalDate.now();
    return today;
  }

}
