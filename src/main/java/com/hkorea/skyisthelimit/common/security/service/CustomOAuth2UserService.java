package com.hkorea.skyisthelimit.common.security.service;

import com.hkorea.skyisthelimit.common.security.CustomOAuth2User;
import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import com.hkorea.skyisthelimit.common.security.oauth2.GoogleResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    GoogleResponse oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

    String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    Optional<Member> existMemberOpt = memberRepository.findByUsername(username);

    if (existMemberOpt.isEmpty()) {
      Member member = new Member();
      member.setUsername(username);
      member.setEmail(oAuth2Response.getEmail());
      member.setName(oAuth2Response.getName());
      member.setRole("ROLE_USER");

      memberRepository.save(member);

      UserDTO userDTO = new UserDTO();
      userDTO.setUsername(username);
      userDTO.setName(oAuth2Response.getName());
      userDTO.setRole("ROLE_USER");

      return new CustomOAuth2User(userDTO);
    } else {

      Member existMember = existMemberOpt.get();
      existMember.setEmail(oAuth2Response.getEmail());
      existMember.setName(oAuth2Response.getName());

      memberRepository.save(existMember);

      UserDTO userDTO = new UserDTO();
      userDTO.setUsername(existMember.getUsername());
      userDTO.setName(oAuth2Response.getName());
      userDTO.setRole(existMember.getRole());

      return new CustomOAuth2User(userDTO);
    }
  }
}
