package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth")
public interface AuthControllerDocs {

  @Operation(
      summary = "Access Token 재발급",
      description = "Refresh Token 쿠키를 이용하여 새로운 Access Token을 발급합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "Access Token 재발급 성공"
      )
  })
  @GetMapping("/auth/reissue/access")
  ResponseEntity<ApiResponse<JwtResponse>> reissueAccessToken(
      @CookieValue(value = "refreshAuthorization") String refreshToken
  );
}
