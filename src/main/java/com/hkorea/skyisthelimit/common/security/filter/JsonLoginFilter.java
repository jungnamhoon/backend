package com.hkorea.skyisthelimit.common.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.common.response.ApiResponse;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.response.SuccessCode;
import com.hkorea.skyisthelimit.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final RequestMatcher DEFAULT_REQUEST_MATCHER =
      PathPatternRequestMatcher.withDefaults()
          .matcher(HttpMethod.POST, "/auth/login");

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final MemberService memberService;

//  public JsonLoginFilter() {
//    super(DEFAULT_REQUEST_MATCHER);
//  }

  public JsonLoginFilter(AuthenticationManager authenticationManager, MemberService memberService) {
    super(DEFAULT_REQUEST_MATCHER, authenticationManager);
    this.memberService = memberService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException {

    // Json Body 읽기
    Map<String, String> loginData = objectMapper.readValue(request.getInputStream(),
        new TypeReference<>() {
        }
    );

    String username = loginData.get("username");
    String password = loginData.get("password");

    // 스프링 시큐리티 인증 토큰 생성
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        username, password);

    return this.getAuthenticationManager().authenticate(authRequest);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {

    // 인증 정보 SecurityContext에 저장
    SecurityContextHolder.getContext().setAuthentication(authResult);
    request.getSession(true)
        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());

    String username = authResult.getName();
    String profileImageUrl = memberService.getMember(username).getProfileImageUrl();

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("username", username);
    responseBody.put("profileImageUrl", profileImageUrl);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(),
        ApiResponse.of(SuccessCode.OK, responseBody).getBody());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException {

    // JSON 응답 (공통 ApiResponse 사용)
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    objectMapper.writeValue(response.getWriter(),
        ApiResponse.of(ErrorCode.UNAUTHORIZED).getBody());

  }
}
