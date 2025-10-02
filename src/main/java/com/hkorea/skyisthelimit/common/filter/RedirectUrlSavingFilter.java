package com.hkorea.skyisthelimit.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class RedirectUrlSavingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (request.getRequestURI().contains("/oauth2/authorization")) {
      String redirectUrl = request.getParameter("redirect");

      if (redirectUrl != null && !redirectUrl.isEmpty()) {
        request.getSession().setAttribute("redirectUrl", redirectUrl);
      }
    }

    filterChain.doFilter(request, response);
  }
}
