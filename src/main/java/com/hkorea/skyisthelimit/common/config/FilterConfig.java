package com.hkorea.skyisthelimit.common.config;

import com.hkorea.skyisthelimit.common.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<LoggingFilter> filterRegistrationBean() {

    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/api/*");
    registrationBean.setOrder(Integer.MIN_VALUE);

    return registrationBean;

  }
}
