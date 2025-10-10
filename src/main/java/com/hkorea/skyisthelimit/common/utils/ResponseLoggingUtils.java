package com.hkorea.skyisthelimit.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseLoggingUtils {

  private ResponseLoggingUtils() {
  }

  public static void logResponse(String requestId, HttpServletRequest request,
      HttpServletResponse response, String body) {

    log.info("[RESPONSE] [{}] [{} {}] [{}]\nBody: [{}]",
        requestId,
        request.getMethod(),
        request.getRequestURI(),
        response.getStatus(),
        (body == null || body.isEmpty()) ? "{}" : body);

  }

  public static void logAccessTokenResponse(String requestId, HttpServletRequest request,
      HttpServletResponse response) {

    log.info("[RESPONSE] [{}] [{} {}] [{}]\nBody: [{}]",
        requestId,
        request.getMethod(),
        request.getRequestURI(),
        response.getStatus(),
        "{ \"accessToken\" : \"[PROTECTED]\" }");
  }


  public static void logSseSubscription(String requestId, HttpServletRequest request,
      HttpServletResponse response) {

    int status = response.getStatus();

    log.info("[RESPONSE] [{}] [{} {}] - SSE (Status: {})",
        requestId,
        request.getMethod(),
        request.getRequestURI(),
        status
    );
  }

  public static void logResponseHeaders(HttpServletResponse response) {
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

}
