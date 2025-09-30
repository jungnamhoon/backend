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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    // Request Param이 존재한다면
    if (httpRequest.getParameterNames().hasMoreElements()) {
      log.info("Request Method: [{}] URL: [{}] Params: [{}]",
          httpRequest.getMethod(),
          httpRequest.getRequestURI(),
          getRequestParams(httpRequest));
    } else {
      log.info("Request Method: [{}] URL: [{}]",
          httpRequest.getMethod(),
          httpRequest.getRequestURI());
    }

    chain.doFilter(request, response);

    HttpServletResponse httpResponse = (HttpServletResponse) response;
    log.info("Response Status: [{}]", httpResponse.getStatus());
    logResponseHeaders(httpResponse);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  private Map<String, String> getRequestParams(HttpServletRequest request) {

    Map<String, String> paramMap = new HashMap<>();
    Enumeration<String> parameterNames = request.getParameterNames();

    while (parameterNames.hasMoreElements()) {
      String paramName = parameterNames.nextElement();
      paramMap.put(paramName, request.getParameter(paramName));
    }

    return paramMap;
  }

  private void logResponseHeaders(HttpServletResponse response) {
    Collection<String> headerNames = response.getHeaderNames();

    if (headerNames != null) {
      for (String headerName : headerNames) {
        String headerValue = response.getHeader(headerName);
        log.info("Response Header: [{}] = [{}]", headerName, headerValue);
      }
    }
  }
}
