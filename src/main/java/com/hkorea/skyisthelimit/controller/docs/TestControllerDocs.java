package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Test", description = "테스트용 API - /api/test/private만 JWT 토큰 필요")
public interface TestControllerDocs {

  @Operation(
      summary = "인증이 필요없는 테스트 API",
      description = "JWT 없이 접근 가능한 공개 API입니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "API 호출 성공"
      )
  })
  ResponseEntity<ApiResponse<String>> publicTest();

  @Operation(
      summary = "인증이 필요한 테스트 API",
      description = "JWT 토큰이 필요한 비공개 API입니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "API 호출 성공"
      )
  })
  ResponseEntity<ApiResponse<String>> privateTest();
}
