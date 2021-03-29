package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipleOauth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    // registrationId로 어떤 OAuth로 로그인했는지 확인할 수 있다
    System.out.println("getClientRegistration : "+userRequest.getClientRegistration());
    System.out.println("getAccessToken : "+userRequest.getAccessToken());

    OAuth2User oAuth2User = super.loadUser(userRequest);
    System.out.println("getAttributes : "+ oAuth2User.getAttributes());

    // 회원가입 진행
    // username, password 둘 다 필요 없는데 그냥 만들어 주는 거다
    String provider = userRequest.getClientRegistration().getClientId();
    String providerId = oAuth2User.getAttribute("sub");
    String username = provider+"_"+providerId;
    String password = bCryptPasswordEncoder.encode("갯인데어");
    String email = oAuth2User.getAttribute("email");
    String role = "ROLE_USER";

    User userEntity = userRepository.findByUsername(username);

    if(userEntity==null) {
      System.out.println("구글 로그인이 최초입니다");
      userEntity = User.builder()
          .username(username)
          .password(password)
          .email(email)
          .role(role)
          .provider(provider)
          .providerId(providerId)
          .build();

      userRepository.save(userEntity);
    }else{
      System.out.println("구글 로그인을 이미 한적이 있습니다. 자동회원가입이 되어있습니다");
    }

    return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
  }
}
