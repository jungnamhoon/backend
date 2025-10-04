package com.hkorea.skyisthelimit.common.config;

import com.hkorea.skyisthelimit.common.filter.LoggingFilter;
import com.hkorea.skyisthelimit.common.filter.RedirectUrlSavingFilter;
import com.hkorea.skyisthelimit.common.filter.RequestWrapperFilter;
import com.hkorea.skyisthelimit.common.filter.ResponseWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<RequestWrapperFilter> requestWrapperFilterRegistrationBean() {

    FilterRegistrationBean<RequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new RequestWrapperFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(Integer.MIN_VALUE);

    return registrationBean;

  }

  @Bean
  public FilterRegistrationBean<ResponseWrapperFilter> responseWrapperFilterFilterRegistrationBean() {

    FilterRegistrationBean<ResponseWrapperFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new ResponseWrapperFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(Integer.MIN_VALUE + 1);

    return registrationBean;

  }

  @Bean
  public FilterRegistrationBean<LoggingFilter> loggingFilterFilterRegistrationBean() {

    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(Integer.MIN_VALUE + 2);

    return registrationBean;

  }

  @Bean
  public FilterRegistrationBean<RedirectUrlSavingFilter> redirectUrlSavingFilterRegistrationBean() {
    FilterRegistrationBean<RedirectUrlSavingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RedirectUrlSavingFilter());
    registrationBean.addUrlPatterns("/oauth2/authorization/*");
    registrationBean.setOrder(Integer.MIN_VALUE + 3);
    return registrationBean;
  }

}
