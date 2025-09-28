package com.hkorea.skyisthelimit.common.security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.hkorea.skyisthelimit.common.security.filter.JsonLoginFilter;
import com.hkorea.skyisthelimit.common.security.handler.CustomAuthenticationEntryPoint;
import com.hkorea.skyisthelimit.common.security.service.CustomUserDetailsService;
import com.hkorea.skyisthelimit.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain authFilterChain(HttpSecurity http)
      throws Exception {

    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()
        .oidc(withDefaults());

    http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .with(authorizationServerConfigurer, withDefaults())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

    return http.build();
  }

  /*
      1. /auth/login
      2. /auth/signup
      3. /auth/token
   */
  @Bean
  @Order(2)
  public SecurityFilterChain jsonAuthFilterChain(HttpSecurity http,
      AuthenticationManager authenticationManager,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
      MemberService memberService)
      throws Exception {

    JsonLoginFilter jsonLoginFilter = new JsonLoginFilter(authenticationManager, memberService);

    http
        .securityMatcher("auth/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/auth/token").authenticated().anyRequest().permitAll())
        .addFilterAt(jsonLoginFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            ex -> ex.
                authenticationEntryPoint(customAuthenticationEntryPoint));

    return http.build();
  }

  /*
      1. /login
      2. /signup
   */
  @Bean
  @Order(3)
  public SecurityFilterChain formAuthFilterChain(HttpSecurity http) throws Exception {

    http
        .securityMatcher("/login", "/signup")
        .formLogin(form -> form
            .loginPage("/login"))
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll());

    return http.build();
  }

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
  @Order(5)
  public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      CustomUserDetailsService customUserDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(customUserDetailsService);
    provider.setPasswordEncoder(bCryptPasswordEncoder);

    return new ProviderManager(provider);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
  }

  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setSameSite("None");
    serializer.setUseSecureCookie(true);
    return serializer;
  }

}
