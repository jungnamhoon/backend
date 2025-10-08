package com.hkorea.skyisthelimit.common.utils;


import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;


@Slf4j
public class RequestLoggingUtils {

  private RequestLoggingUtils() {
  }

  public static void logRequest(String requestId, HttpServletRequest request, String body) {

    if (isMultipartRequest(request)) {
      log.info("[REQUEST] [{}] [{} {}]\nParams: [{}]\nBody: [{}]",
          requestId,
          request.getMethod(),
          request.getRequestURI(),
          getRequestParams(request),
          (body == null || body.isEmpty()) ? "{}" : body);
      return;
    }

    log.info("[REQUEST] [{}] [{} {}]\nParams: [{}]\nBody: [{}]",
        requestId,
        request.getMethod(),
        request.getRequestURI(),
        getRequestParams(request),
        (body == null || body.isEmpty()) ? "{}" : body);
  }

  private static Map<String, String> getRequestParams(HttpServletRequest request) {

    Map<String, String> paramMap = new HashMap<>();
    Enumeration<String> parameterNames = request.getParameterNames();

    while (parameterNames.hasMoreElements()) {
      String paramName = parameterNames.nextElement();
      paramMap.put(paramName, request.getParameter(paramName));
    }

    return paramMap;
  }

  private static void logMultipartRequest(HttpServletRequest request) {

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
