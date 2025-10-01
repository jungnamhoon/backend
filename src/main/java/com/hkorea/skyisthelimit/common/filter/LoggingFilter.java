package com.hkorea.skyisthelimit.common.filter;

import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkorea.skyisthelimit.common.CustomHttpRequestWrapper;
import com.hkorea.skyisthelimit.common.CustomHttpResponseWrapper;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Slf4j
public class LoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    // request
    if (request instanceof CustomHttpRequestWrapper requestWrapper) {
      String requestBody = new String(requestWrapper.getRequestBody());

      if (!requestBody.isEmpty()) {
        log.info("[{}] {} Body: [{}]",
            httpRequest.getMethod(),
            httpRequest.getRequestURI(),
            requestBody);
      }

      if (httpRequest.getParameterNames().hasMoreElements()) {
        log.info("[{}] {} Params: [{}]",
            httpRequest.getMethod(),
            httpRequest.getRequestURI(),
            getRequestParams(httpRequest));
      } else {
        log.info("[{}] {}",
            httpRequest.getMethod(),
            httpRequest.getRequestURI());
      }

      if (isMultipartRequest(httpRequest)) {
        logMultipartRequest(httpRequest);
      }

    }

    chain.doFilter(request, response);

    // response
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // 특정 URL에 대해서만 응답 헤더 로그를 출력
    if (httpRequest.getRequestURI().equals("/login/oauth2/code/google")) {
      logResponseHeaders(httpResponse);
    }

    if (response instanceof CustomHttpResponseWrapper responseWrapper) {
      byte[] responseData = responseWrapper.getResponseData();

      if (responseData != null && responseData.length > 0) {

        String responseBody = new String(responseData);

        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(responseBody, Object.class);
        String prettyBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        log.info("Response Status: [{}] URL: [{}] Body: [{}]",
            httpResponse.getStatus(),
            httpRequest.getRequestURI(), prettyBody);
      } else {
        log.info("Response Status: [{}] URL: [{}] Body: [Empty]",
            httpResponse.getStatus(),
            httpRequest.getRequestURI());
      }
    } else {
      log.info("Response Status: [{}] URL: [{}] Not a CustomHttpResponseWrapper",
          httpResponse.getStatus(),
          httpRequest.getRequestURI());
    }

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
    Map<String, Collection<String>> headers = new HashMap<>();
    Collection<String> headerNames = response.getHeaderNames();

    // 모든 헤더를 순회
    for (String headerName : headerNames) {
      // 각 헤더의 값을 리스트로 받아옴
      Collection<String> headerValues = response.getHeaders(headerName);
      headers.put(headerName, headerValues);
    }

    // 출력
    try {
      ObjectMapper mapper = new ObjectMapper();
      String jsonHeaders = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(headers);
      log.info("Response Headers: {}", jsonHeaders);
    } catch (JsonProcessingException e) {
      log.error("Error processing response headers to JSON", e);
    }
  }

  private void logMultipartRequest(HttpServletRequest request) {

    StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
    MultipartHttpServletRequest multipart = multipartResolver.resolveMultipart(request);

    multipart.getFileMap().forEach((paramName, file) -> {
      log.info("File Parameter Name: {}, Original File Name: {}, Size: {} bytes",
          paramName,
          file.getOriginalFilename(),
          file.getSize());
    });

    multipart.getParameterMap().forEach((paramName, value) -> {
      log.info("Form Field - Name: {}, Value: {}",
          paramName,
          value);
    });
  }
}
