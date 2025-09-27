package com.hkorea.skyisthelimit.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth")
public interface AuthControllerDocs {

  @Operation(
      summary = "Google OAuth2 로그인",
      description = "구글 OAuth2 로그인 프로세스를 시작합니다. 구글 로그인 페이지로 리다이렉트됩니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "302",
          description = "구글 로그인 페이지로 리다이렉트 성공"
      )
  })
  @GetMapping("/oauth2/authorization/google")
  String loginWithGoogle();
}
