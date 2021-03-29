package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipleOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록된다
// =SecurityConfig(내가 지금부터 등록할 필터)가 기본 필터 체인에 등록된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.(어디서든 사용 가능)
  @Bean
  public BCryptPasswordEncoder encodePwd(){
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public PrincipleOauth2UserService principleOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable();
      http.authorizeRequests()
        .antMatchers("/user/**").authenticated() // 인증이 필요하다(인증 된 사용자라면 허용)
        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //ROLE_ADMIN이나 ROLE_MANAGER 권한이 있어야 들어갈 수 있다
        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //ROLE_ADMIN 권한이 있어야 들어갈 수 있다.
        .anyRequest().permitAll() //다른 주소에 대해서는 모두 허용
        .and()
        .formLogin()
        .loginPage("/loginForm") // 권한이 없는 페이지로 요청이 들어갈 때, 로그인 페이지로 이동된다
        .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다
        .defaultSuccessUrl("/")
        .and()
        .oauth2Login()
        .loginPage("/loginForm")
        .userInfoEndpoint()
        .userService(principleOauth2UserService);
  }
}
