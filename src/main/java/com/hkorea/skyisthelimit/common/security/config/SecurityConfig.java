package com.hkorea.skyisthelimit.common.security.config;

import com.hkorea.skyisthelimit.common.security.filter.JwtFilter;
import com.hkorea.skyisthelimit.common.security.handler.CustomSuccessHandler;
import com.hkorea.skyisthelimit.common.security.service.CustomOAuth2UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JwtFilter jwtFilter;

  @Value("${cors.allowed-origins.list}")
  private List<String> allowedOrigins;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      CorsConfigurationSource corsConfigurationSource) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable);

    http
        .formLogin(AbstractHttpConfigurer::disable);

    http
        .httpBasic(AbstractHttpConfigurer::disable);

    http
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    http
        .oauth2Login((oauth2) -> oauth2
            .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                .userService(customOAuth2UserService))
            .successHandler(customSuccessHandler));

    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/api/auth/access-token", "/v3/api-docs/**", "/swagger-ui.html",
                "/swagger-ui/**", "/api/test/token").permitAll()
            .anyRequest().authenticated());

    http
        .sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(allowedOrigins);
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
      configuration.setAllowedHeaders(List.of("*"));
      configuration.setAllowCredentials(true);
      configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
      configuration.setMaxAge(3600L);
      return configuration;
    };
  }

}
