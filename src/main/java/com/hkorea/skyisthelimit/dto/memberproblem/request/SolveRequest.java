package com.hkorea.skyisthelimit.dto.memberproblem.request;

import com.hkorea.skyisthelimit.service.enums.ResultCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class SolveRequest {

  @Schema(
      title = "제출 번호",
      description = "문제 제출당 고유 제출 번호",
      example = "98245328"
  )
  private Long submitId;

  @Schema(
      title = "백준 문제 ID",
      description = "회원이 푼 문제의 고유 ID",
      example = "1000"
  )
  private Integer baekjoonId;

  @Schema(
      title = "문제 풀이 여부",
      description = "회원이 문제를 맞췄는지 여부",
      example = "true"
  )
  private Boolean isSolved;

  @Schema(
      title = "문제 풀이 코드",
      description = "푼 문제의 코드",
      example="print(A+B)"
  )
  private String code;

  @Schema(
      title = "문제 풀이 결과",
      description = "맞았습니다,시간 초과, 메모리 초과 등을 표시",
      example = "ac"
  )
  private ResultCategory resultCategory;

  @Schema(
      title="문제 내용",
      description = "문제 내용을 표시",
      example="두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오."
  )
  private String problemDescription;

  @Schema(
      title="입력값 설명",
      description = "문제의 입력값을 설명",
      example="첫째 줄에 A와 B가 주어진다. (0 < A, B < 10)"
  )
  private String problemInput;

  @Schema(
      title="출력값 설명",
      description = "문제의 출력값을 설명",
      example="첫째 줄에 A+B를 출력한다."
  )
  private String problemOutput;

}