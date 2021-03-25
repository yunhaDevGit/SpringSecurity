package com.cos.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  // view resolver를 재설정
  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    MustacheViewResolver resolver = new MustacheViewResolver();
    resolver.setCharset("UTF-8");
    resolver.setContentType("text/html;charset=UTF-8"); // 던지는 data는 html이고, utf-8이다
    resolver.setPrefix("classpath:/templates/"); // classpath=프로젝트 경로
    resolver.setSuffix(".html"); // .html 파일을 만들어도 mustache가 인식을 한다.

    registry.viewResolver(resolver); // registry로 view resolver 등록
  }
}
