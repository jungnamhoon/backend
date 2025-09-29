package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.AuthControllerDocs;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import com.hkorea.skyisthelimit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @GetMapping("/reissue/access")
  public ResponseEntity<ApiResponse<JwtResponse>> reissueAccessToken(
      @CookieValue(value = "refreshAuthorization") String refreshToken) {

    JwtResponse responseDTO = authService.reissueAccessToken(refreshToken);

    return ApiResponse.of(SuccessCode.OK, responseDTO);
  }
}
