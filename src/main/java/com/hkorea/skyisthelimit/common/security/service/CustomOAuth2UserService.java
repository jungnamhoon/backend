package com.hkorea.skyisthelimit.common.security.service;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import com.hkorea.skyisthelimit.common.security.oauth2.GoogleResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Value("${minio.endpoint}")
  private String minioEndpoint;


  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    GoogleResponse oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

    String oauth2Username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    Optional<Member> existMemberOpt = memberRepository.findByOauth2Username(oauth2Username);

    if (existMemberOpt.isEmpty()) {
      Member member = new Member();

      member.setOauth2Username(oauth2Username);
      member.setUsername(oAuth2Response.getEmail().split("@")[0]);
      member.setRealName(oAuth2Response.getName());
      member.setEmail(oAuth2Response.getEmail());
      member.setRole("ROLE_USER");
      member.setProfileImageUrl(minioEndpoint + "/" + "profile" + "/" + "basic-profile.png");
      member.setNickname("닉네임");

      memberRepository.save(member);

      UserDTO userDTO = new UserDTO();
      userDTO.setOauth2Username(oauth2Username);
      userDTO.setUsername(oAuth2Response.getEmail().split("@")[0]);
      userDTO.setRealName(oAuth2Response.getName());
      userDTO.setEmail(oAuth2Response.getEmail());
      userDTO.setRole("ROLE_USER");

      return new CustomOAuth2User(userDTO);
    } else {

      Member existMember = existMemberOpt.get();

      memberRepository.save(existMember);

      UserDTO userDTO = new UserDTO();

      userDTO.setOauth2Username(existMember.getOauth2Username());
      userDTO.setUsername(existMember.getUsername());
      userDTO.setRealName(existMember.getRealName());
      userDTO.setEmail(existMember.getEmail());
      userDTO.setRole(existMember.getRole());

      return new CustomOAuth2User(userDTO);
    }
  }
}
