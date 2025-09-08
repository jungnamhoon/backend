package com.hkorea.skyisthelimit.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GlobalSecurityConfig {

  @Bean
  @Order(5)
  public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
