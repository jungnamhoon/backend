package com.hkorea.skyisthelimit.common.security.filter;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import com.hkorea.skyisthelimit.common.utils.ResponseUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    return path.equals("/api/auth/access-token") ||
        path.startsWith("/swagger-ui/") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/swagger-ui.html") ||
        path.equals("/api/test/token");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String accessToken = extractToken(request, response);

    if (accessToken == null) {
      ResponseUtils.sendUnauthorized(response, "Authorization header missing or invalid");
      return;
    }

    try {
      jwtHelper.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      ResponseUtils.sendUnauthorized(response, "Token has expired. Please refresh");
      return;
    }

    String category = jwtHelper.getCategory(accessToken);

    if (!category.equals("access")) {
      ResponseUtils.sendUnauthorized(response, "Not a access token");
      return;
    }

    setAuthentication(accessToken);

    filterChain.doFilter(request, response);
  }

  @Nullable
  private String extractToken(HttpServletRequest request, HttpServletResponse response) {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      return null;
    }

    return authorizationHeader.substring(7);
  }

  private void setAuthentication(String accessToken) {

    UserDTO userDTO = UserDTO.builder()
        .username(jwtHelper.getUsername(accessToken))
        .email(jwtHelper.getEmail(accessToken))
        .realName(jwtHelper.getName(accessToken))
        .role(jwtHelper.getRole(accessToken))
        .build();

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

    Authentication authToken = new UsernamePasswordAuthenticationToken(
        customOAuth2User, null, customOAuth2User.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

}
