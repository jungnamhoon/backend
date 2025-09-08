package com.hkorea.skyisthelimit.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String encode(String rawPassword) {
    return encoder.encode(rawPassword);
  }
}
