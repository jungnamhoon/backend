package com.hkorea.skyisthelimit.dto.memberproblem.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NoteRequestDTO {

  @Schema(description = "오답 노트내용", example = "dp방식으로 풀었습니다.")
  private String content;
}
