package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view return
public class IndexController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping("/test/login")
  public @ResponseBody String testLogin(Authentication authentication,
      @AuthenticationPrincipal PrincipalDetails userDetails){
    System.out.println("/test/login ========");
    // Authentication 객체 안에 principal 객체가 있다.
    // principal을 리턴 타임이 object이므로 PrincipalDetails로 down casting
    PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
    System.out.println("authentication : "+ principalDetails.getUser());

    System.out.println("userDeatils : "+userDetails.getUser());
    return "세션 정보 확인하기";
  }

  @GetMapping("/test/oauth/login")
  public @ResponseBody String testOAuthLogin(Authentication authentication,
                                             @AuthenticationPrincipal OAuth2User oAuth){
    System.out.println("/test/oauth/login ========");
    OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
    System.out.println("authentication : " + oAuth2User.getAttributes());
    System.out.println("oauth2User : " + oAuth.getAttributes());

    return "OAuth 세션 정보 확인하기";
  }


  // mustache 기본 폴더 src/main/resources/
  // view resolver : templates (prefix), .mustache (suffix) 생략가능
  @GetMapping({"", "/"})
  public String index(){
    return "index"; // src/main/resources/templates/index.mustache
  }


  @GetMapping("/user")
  public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    System.out.println("principalDetails : " + principalDetails.getUser());
    return "user";
  }

  @GetMapping("/admin")
  public @ResponseBody String admin() {
    return "admin";
  }

  @GetMapping("/manager")
  public @ResponseBody String manager() {
    return "manager";
  }

//  spring security가 해당 주소를 낚아챈다(스프링 시큐리티의 로그인 페이지로 이동됨)
//  -> SecurityConfig 파일 생성 후 스프링 시큐리티가 낚아채지 않는다
//    (권한이 없는 페이지에 접속할 때,로그인 페이지가 아닌 403에러 페이지가 뜬다)
  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @GetMapping("/joinForm") //회원가입
  public String joinForm() {
    return "joinForm";
  }

  @PostMapping("/join")
  public String join(User user) {
    System.out.println(user);
    user.setRole("ROLE_USER");
    String rawPassword = user.getPassword();
    String encodingPassword = bCryptPasswordEncoder.encode(rawPassword);
    user.setPassword(encodingPassword);
    userRepository.save(user);
    return "redirect:/loginForm"; // '/loginForm'이라는 함수 호출
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/info")
  public @ResponseBody String info(){
    return "개인정보";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
  @GetMapping("/data")
  public @ResponseBody String data(){
    return "개인정보";
  }
}
