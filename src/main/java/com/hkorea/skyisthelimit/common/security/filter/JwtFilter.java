package com.hkorea.skyisthelimit.common.security.filter;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;

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

    String token = authorizationHeader.substring(7);

    if (jwtHelper.isExpired(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Token expired\"}");
      return;
    }

    String username = jwtHelper.getUsername(token);
    String role = jwtHelper.getRole(token);

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(username);
    userDTO.setRole(role);

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
        customOAuth2User.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}
