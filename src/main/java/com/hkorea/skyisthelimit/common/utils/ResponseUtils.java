package com.hkorea.skyisthelimit.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

  public static void sendError(HttpServletResponse response, int status, String message)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write("{\"error\":\"" + message + "\"}");
  }

  public static void sendUnauthorized(HttpServletResponse response, String message)
      throws IOException {
    sendError(response, HttpServletResponse.SC_UNAUTHORIZED, message);
  }

  public static void sendBadRequest(HttpServletResponse response, String message)
      throws IOException {
    sendError(response, HttpServletResponse.SC_BAD_REQUEST, message);
  }
}
