package com.hkorea.skyisthelimit.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.common.CustomHttpRequestWrapper;
import com.hkorea.skyisthelimit.common.CustomHttpResponseWrapper;
import com.hkorea.skyisthelimit.common.utils.RequestLoggingUtils;
import com.hkorea.skyisthelimit.common.utils.ResponseLoggingUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter implements Filter {

  private static final List<String> EXCLUDE_PREFIXES = List.of(
      "/v3",
      "/swagger-ui",
      "/favicon"
  );

  private static final String SSE_PATH = "/api/notifications/stream";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestId = UUID.randomUUID().toString();
    httpRequest.setAttribute("X-Request-ID", requestId);
    httpResponse.addHeader("X-Request-ID", requestId);

    logRequest(requestId, httpRequest);

    chain.doFilter(request, response);

    logResponse(requestId, httpRequest, httpResponse);

  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  private void logRequest(String requestId, HttpServletRequest httpRequest) {

    String uri = httpRequest.getRequestURI();

    if (isExcludedUri(uri)) {
      return;
    }

    if (httpRequest instanceof CustomHttpRequestWrapper requestWrapper) {

      String body = new String(requestWrapper.getRequestBody());

      RequestLoggingUtils.logRequest(requestId, httpRequest, body);

    }
  }

  private void logResponse(String requestId, HttpServletRequest httpRequest,
      HttpServletResponse httpResponse) throws IOException, ServletException {

    String uri = httpRequest.getRequestURI();

    if (isExcludedUri(uri)) {
      return;
    }

    if (uri.equals(SSE_PATH)) {
      ResponseLoggingUtils.logSseSubscription(requestId, httpRequest, httpResponse);
      return;
    }

    if (uri.equals("/api/auth/access-token")) {
      ResponseLoggingUtils.logAccessTokenResponse(requestId, httpRequest, httpResponse);
      return;
    }

    if (httpResponse instanceof CustomHttpResponseWrapper responseWrapper) {

      String body = new String(responseWrapper.getResponseData());

      ResponseLoggingUtils.logResponse(requestId, httpRequest, httpResponse,
          toPretty(body));
    }
  }

  private boolean isExcludedUri(String uri) {
    return EXCLUDE_PREFIXES.stream().anyMatch(uri::startsWith);
  }

  private String toPretty(String responseBody) {

    if (responseBody == null || responseBody.isEmpty()) {
      return null;
    }

    try {
      Object json = objectMapper.readValue(responseBody, Object.class);
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    } catch (JsonProcessingException e) {
      return null;
    }
  }
}
