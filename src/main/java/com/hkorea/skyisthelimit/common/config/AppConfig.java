package com.hkorea.skyisthelimit.common.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @PersistenceContext
  private EntityManager em;

  @Bean
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(em);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public OpenAPI api() {
    // JWT Bearer Authorization 설정
    String bearerSchemeName = "BearerAuth";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(bearerSchemeName);

    // Basic Authentication 설정
    String basicSchemeName = "BasicAuth";
    SecurityRequirement basicSecurityRequirement = new SecurityRequirement().addList(
        basicSchemeName);

    Components components = new Components()
        // Bearer JWT 인증 스키마 추가
        .addSecuritySchemes(bearerSchemeName, new SecurityScheme()
            .name(bearerSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"))
        // Basic 인증 스키마 추가
        .addSecuritySchemes(basicSchemeName, new SecurityScheme()
            .name(basicSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("basic"));

    return new OpenAPI()
        .components(components)
        // Bearer 인증과 Basic 인증 요구 사항 추가
        .addSecurityItem(securityRequirement)
        .addSecurityItem(basicSecurityRequirement);
  }

}