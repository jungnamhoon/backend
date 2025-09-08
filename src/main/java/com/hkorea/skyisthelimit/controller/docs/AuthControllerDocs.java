package com.hkorea.skyisthelimit.controller.docs;

import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.dto.auth.request.LoginRequest;
import com.hkorea.skyisthelimit.dto.auth.request.RegisterRequest;
import com.hkorea.skyisthelimit.dto.auth.request.SignUpRequest;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import com.hkorea.skyisthelimit.dto.auth.response.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth", description = "인증 관련(oauth2 포함) API - API마다 인증 방법이 다르므로 확인")
public interface AuthControllerDocs {

  @Operation(
      summary = "회원 가입 - No Authentication Required",
      description = "새로운 사용자를 등록합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "회원 가입 성공"
      )
  })
  ResponseEntity<ApiResponse<Void>> signUp(SignUpRequest signUpDTO);

  @Operation(
      summary = "로그인 - No Authentication Required",
      description = "사용자 로그인 요청입니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "로그인 성공. 세션을 쿠키로 제공",
          headers = {
              @Header(
                  name = "Set-Cookie",
                  description = "세션 쿠키(JSESSIONID)",
                  schema = @Schema(type = "string", example = "JSESSIONID=12345ABCDE; Path=/; HttpOnly")
              )
          }
      )
  })
  ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest loginDTO);

  @Operation(
      summary = "JWT 발급 - Session Required",
      description = "세션 쿠키(JSESSIONID)를 기반으로 JWT 토큰을 발급합니다. api에 접근하려면 토큰이 필요합니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "로그인 성공. 세션을 쿠키로 제공"
      )
  })
  ResponseEntity<ApiResponse<JwtResponse>> getToken(
      @Parameter(
          in = ParameterIn.COOKIE,
          name = "JSESSIONID",
          description = "로그인 후 발급받은 세션 쿠키",
          required = true,
          schema = @Schema(type = "string", example = "JSESSIONID=12345ABCDE")
      )
      @AuthenticationPrincipal UserDetails userDetails);

  @Operation(
      summary = "클라이언트 등록 - No Authentication Required",
      description = "OAuth2 클라이언트를 등록합니다. 클라이언트는 클라이언트 ID, 클라이언트 시크릿 등을 제공하여 인증 및 토큰 요청을 할 수 있습니다."
  )
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200",
          description = "클라이언트 등록 성공"
      ),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "400",
          description = "잘못된 요청"
      )
  })
  ResponseEntity<ApiResponse<RegisterResponse>> register(RegisterRequest registerDTO);


  @Operation(
      summary = "Code Grant 방식으로 JWT 토큰 발급 받기 - BasicAuth",
      description = "Code Grant 방식으로 JWT 토큰을 발급 받습니다."
  )
  ResponseEntity<ApiResponse<Void>> getOauthToken(
      @Parameter(
          name = "grant_type",
          description = "authorization_code로 설정",
          required = true,
          example = "authorization_code"
      )
      @RequestParam(defaultValue = "authorization_code") String grant_type,

      @Parameter(
          name = "code",
          description = "OAuth2 Authorization Code",
          required = true,
          example = "hsNL4408Deg5r3zxi6TdVseap_6bOxaboO2fPX8eKFxmjpkNGVgrfgBdrAUwExbxDqBNeJqx41VtoxlU3UVC4OXWMqW2jTENII79wh7GWVCfo1Bkyzq-HmTBvqZXVBpG"
      )
      @RequestParam String code,

      @Parameter(
          name = "redirect_uri",
          description = "리디렉션 URI, 클라이언트 등록 시 사용한 값",
          required = true,
          example = "http://localhost:5000/callback.html"
      )
      @RequestParam String redirect_uri,

      @Parameter(
          name = "scope",
          description = "요청할 Scope",
          required = true,
          example = "profile"
      )
      @RequestParam(defaultValue = "profile") String scope
  );
}
