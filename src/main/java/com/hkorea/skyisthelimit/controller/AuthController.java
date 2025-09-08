package com.hkorea.skyisthelimit.controller;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.controller.docs.AuthControllerDocs;
import com.hkorea.skyisthelimit.dto.auth.request.LoginRequest;
import com.hkorea.skyisthelimit.dto.auth.request.RegisterRequest;
import com.hkorea.skyisthelimit.dto.auth.request.SignUpRequest;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import com.hkorea.skyisthelimit.dto.auth.response.RegisterResponse;
import com.hkorea.skyisthelimit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @PostMapping("auth/signup")
  @ResponseBody
  public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid SignUpRequest requestDTO) {
    authService.signUp(requestDTO);
    return ApiResponse.of(SuccessCode.OK, null);
  }

  @PostMapping("auth/login")
  @ResponseBody
  public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest requestDTO) {
    return ApiResponse.of(SuccessCode.OK, null);
  }

  @GetMapping("auth/token")
  @ResponseBody
  public ResponseEntity<ApiResponse<JwtResponse>> getToken(
      @AuthenticationPrincipal UserDetails userDetails) {
    JwtResponse token = authService.getToken(userDetails);
    return ApiResponse.of(SuccessCode.OK, token);
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @PostMapping("oauth2/register")
  @ResponseBody
  public ResponseEntity<ApiResponse<RegisterResponse>> register(
      @RequestBody RegisterRequest requestDTO) {
    return ApiResponse.of(SuccessCode.OK, authService.register(requestDTO));
  }

  @PostMapping(value = "/oauth2/token", consumes = "application/x-www-form-urlencoded")
  @ResponseBody
  public ResponseEntity<ApiResponse<Void>> getOauthToken(
      @RequestParam String grant_type,
      @RequestParam String code,
      @RequestParam String redirect_uri,
      @RequestParam String scope) {

    return ApiResponse.of(SuccessCode.OK, null);
  }

}
