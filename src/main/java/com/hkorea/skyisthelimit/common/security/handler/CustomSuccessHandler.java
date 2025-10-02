package com.hkorea.skyisthelimit.common.security.handler;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtHelper jwtHelper;
  private final MemberService memberService;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

    String oauth2Username = customUserDetails.getOauth2Username();
    String role = extractOAuthRole(authentication);

    Member member = memberService.getMemberByOauth2Username(oauth2Username);

    String username = member.getUsername();
    String email = member.getEmail();
    String profileImageUrl = member.getProfileImageUrl();

    String refreshToken = jwtHelper.createRefreshToken(username, email, profileImageUrl, role);

    HttpSession session = request.getSession();
    String env = null;

    if (session != null) {
      env = (String) session.getAttribute("redirectUrl");
      session.removeAttribute("redirectUrl");
      log.info("env {}", env);
    }

    String redirectUrl;
    if (env.equals("skyisthelimit")) {
      redirectUrl = "https://skyisthelimit.cloud?redirectedFromSocialLogin=true";
    } else {
      redirectUrl = "http://localhost:3000?redirectedFromSocialLogin=true";
    }
    addCookieWithSameSite(response, "refreshAuthorization", refreshToken);

    response.sendRedirect(redirectUrl);
  }

  private String extractOAuthRole(Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    return auth.getAuthority();
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60 * 60 * 60);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);

    return cookie;
  }

  private void addCookieWithSameSite(HttpServletResponse response, String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60 * 60 * 60); // 쿠키의 유효 기간을 설정
    cookie.setSecure(true); // HTTPS에서만 전송되도록 설정
    cookie.setPath("/"); // 쿠키의 유효 범위를 루트 경로로 설정
    cookie.setHttpOnly(true); // 클라이언트에서 접근 불가능하도록 설정

    String cookieHeader = key + "=" + value + "; Max-Age=" + (60 * 60 * 60)
        + "; Path=/; HttpOnly; Secure; SameSite=None";

    response.addHeader("Set-Cookie", cookieHeader); // Set-Cookie 헤더로 추가
  }
}


