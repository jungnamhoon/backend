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
    
    String redirect = request.getParameter("redirect");

    if (redirect == null || redirect.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setContentType("text/html;charset=utf-8");
      response.getWriter().write("{\"error\":\"redirect 파라미터가 필요합니다.\"}");
      return;
    }

    request.getSession().setAttribute("redirect", redirect);

    filterChain.doFilter(request, response);
  }
}
