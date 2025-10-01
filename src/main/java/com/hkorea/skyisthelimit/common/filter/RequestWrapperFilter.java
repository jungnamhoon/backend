package com.hkorea.skyisthelimit.common.filter;

import com.hkorea.skyisthelimit.common.CustomHttpRequestWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestWrapperFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest httpRequest) {
      CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(httpRequest);
      chain.doFilter(requestWrapper, response);
    } else {
      chain.doFilter(request, response);
    }
  }
}
