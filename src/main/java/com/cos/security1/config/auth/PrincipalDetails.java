package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
// 로그인 진행이 완료가 되면 security session을 만들어준다.
// 세션 공간은 똑같은데 시큐리티가 자신만의 세션 공간을 가진다.(Security Context Holder)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

  private User user;
  private Map<String,Object> attributes;

  // 일반 로그인 할 때 사용하는 생성자
  public PrincipalDetails(User user){
    this.user = user;
  }

  // oauth 로그인 할 때 사용하는 생성자
  public PrincipalDetails(User user, Map<String,Object> attributes){
    this.user = user;
    this.attributes = attributes;
  }

  // 해당 user의 권한을 리턴
  // return 타입이 Collection으로 정해져있기 때문에 다음과 같이 구현
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collect = new ArrayList<>();
    collect.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return user.getRole();
      }
    });
    return collect;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public String getName() {
    return null;
  }
}
