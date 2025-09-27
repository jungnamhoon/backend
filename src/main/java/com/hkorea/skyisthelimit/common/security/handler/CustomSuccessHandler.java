package com.hkorea.skyisthelimit.common.security.handler;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtHelper jwtHelper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

    String username = customUserDetails.getUsername();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    String accessToken = jwtHelper.createJwt(username, role, 60 * 60 * 25 * 14L);
    String refreshToken = jwtHelper.createJwt(username, role, 60 * 60 * 24 * 14L);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String jsonResponse =
        "{\"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshToken + "\"}";

    response.getWriter().write(jsonResponse);
  }
}


