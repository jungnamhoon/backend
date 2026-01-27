package com.hkorea.skyisthelimit.common.config;

import static org.zalando.logbook.core.Conditions.exclude;
import static org.zalando.logbook.core.Conditions.requestTo;
import static org.zalando.logbook.core.HeaderFilters.replaceHeaders;
import static org.zalando.logbook.core.QueryFilters.replaceQuery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.DispatcherType;
import java.util.function.Predicate;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.core.StreamHttpLogWriter;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.servlet.LogbookFilter;

@Configuration
public class LogbookConfig {

  // 1. 로컬, 개발, 테스트 환경용: Pretty Print 적용
  @Bean
  @Profile({"local", "dev", "test"})
  public Logbook logbookForLocal() {
    ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    return Logbook.builder()
        .condition(commonConditions())
        .sink(new DefaultSink(new JsonHttpLogFormatter(mapper), new StreamHttpLogWriter(System.out)))
        .build();
  }

  // 2. 운영(prod) 환경용: 한 줄로 출력 (성능 및 용량 최적화)
  @Bean
  @Profile("prod")
  public Logbook logbookForProd() {
    return Logbook.builder()
        .condition(commonConditions())
        .sink(new DefaultSink(new JsonHttpLogFormatter(), new StreamHttpLogWriter(System.out)))
        .build();
  }

  // 공통 제외 조건들
  private Predicate<HttpRequest> commonConditions() {
    return exclude(
        requestTo("/actuator/**"),
        requestTo("/swagger-ui/**"),
        requestTo("/v3/api-docs/**"),
        requestTo("/favicon.ico")
    );
  }

  @Bean(name = "logbookFilter")
  public FilterRegistrationBean<LogbookFilter> logbookFilterRegistration(Logbook logbook) {
    LogbookFilter filter = new LogbookFilter(logbook);
    FilterRegistrationBean<LogbookFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setOrder(Integer.MIN_VALUE);
    registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
    return registration;
  }

}
