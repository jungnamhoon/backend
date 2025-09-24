package com.hkorea.skyisthelimit.common.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import io.minio.MinioClient;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @Value("${swagger.server-url}")
  private String swaggerServerUrl;

  @Value("${minio.url}")
  private String minioUrl;

  @Value("${minio.access-key}")
  private String minioAccessKey;

  @Value("${minio.secret-key}")
  private String minioSecretKey;

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

    Server server = new Server();
    server.setUrl(swaggerServerUrl);

    return new OpenAPI()
        .components(components)
        // Bearer 인증과 Basic 인증 요구 사항 추가
        .addSecurityItem(securityRequirement)
        .addSecurityItem(basicSecurityRequirement)
        .servers(List.of(server));
  }

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(minioUrl)
        .credentials(minioAccessKey, minioSecretKey)
        .build();
  }

}