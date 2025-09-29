package com.hkorea.skyisthelimit.common.security.filter;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    return path.equals("/auth/reissue/access") ||
        path.startsWith("/swagger-ui/") ||
        path.startsWith("/v3/api-docs") ||
        path.equals("/swagger-ui.html");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Authorization header is missing or invalid\"}");
      return;
    }

    String accessToken = authorizationHeader.substring(7);

    try {
      jwtHelper.isExpired(accessToken);
    } catch (ExpiredJwtException e) {

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Access token expired\"}");

      return;
    }

    String category = jwtHelper.getCategory(accessToken);

    if (!category.equals("access")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Not Access Token\"}");
    }

    String username = jwtHelper.getUsername(accessToken);
    String email = jwtHelper.getEmail(accessToken);
    String name = jwtHelper.getName(accessToken);
    String role = jwtHelper.getRole(accessToken);

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(username);
    userDTO.setEmail(email);
    userDTO.setRealName(name);
    userDTO.setRole(role);

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
        customOAuth2User.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
