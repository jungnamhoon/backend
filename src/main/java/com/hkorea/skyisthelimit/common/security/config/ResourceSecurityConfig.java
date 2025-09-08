package com.hkorea.skyisthelimit.common.security.config;

import com.hkorea.skyisthelimit.common.security.CustomAuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

  @Bean
  @Order(4)
  public SecurityFilterChain resourceFilterChain(HttpSecurity http,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

    http
        .oauth2ResourceServer(resource -> resource
            .jwt(Customizer.withDefaults())
            .authenticationEntryPoint(customAuthenticationEntryPoint));

    http
        .securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/test/public").permitAll()
            .anyRequest().authenticated());

    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    try {
      // 모든 키 선택
      JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());
      List<JWK> keys = jwkSource.get(jwkSelector, null);

      if (keys.isEmpty()) {
        throw new IllegalStateException("No JWK keys available for JwtDecoder");
      }

      JWK jwk = keys.get(0);

      if (!(jwk instanceof RSAKey rsaKey)) {
        throw new IllegalStateException("Expected RSAKey for JwtDecoder");
      }

      return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to create JwtDecoder from JWKSource", e);
    }
  }
}
