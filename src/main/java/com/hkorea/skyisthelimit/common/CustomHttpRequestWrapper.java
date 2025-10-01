package com.hkorea.skyisthelimit.common;

import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Slf4j
public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] requestBody;

  public CustomHttpRequestWrapper(HttpServletRequest request) throws IOException {

    super(request);

    if (isMultipartRequest(request)) {

      StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
      multipartResolver.resolveMultipart(request);
    }

    InputStream is = request.getInputStream();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = is.read(buffer)) != -1) {
      byteArrayOutputStream.write(buffer, 0, length);
    }
    this.requestBody = byteArrayOutputStream.toByteArray();
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(this.requestBody);

    return new ServletInputStream() {
      @Override
      public int read() throws IOException {
        return byteInputStream.read();
      }

      @Override
      public boolean isFinished() {
        return byteInputStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener listener) {

      }
    };

  }

  public byte[] getRequestBody() {
    return this.requestBody;
  }
}
