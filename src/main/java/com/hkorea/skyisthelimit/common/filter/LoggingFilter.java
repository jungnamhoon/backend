package com.hkorea.skyisthelimit.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    log.info("Incoming request: URI={} Method={} Remote IP={}",
        httpRequest.getRequestURI(),
        httpRequest.getMethod(),
        httpRequest.getRemoteAddr());

    chain.doFilter(request, response);

    HttpServletResponse httpResponse = (HttpServletResponse) response;
    log.info("Outgoing response: Status={}", httpResponse.getStatus());
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }
}
