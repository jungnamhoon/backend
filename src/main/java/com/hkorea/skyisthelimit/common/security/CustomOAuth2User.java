package com.hkorea.skyisthelimit.common.security;

import com.hkorea.skyisthelimit.common.security.dto.UserDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

  private final UserDTO userDTO;

  public CustomOAuth2User(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add((GrantedAuthority) userDTO::getRole);

    return collection;

  }

  @Override
  public String getName() {
    return userDTO.getRealName();
  }

  public String getOauth2Username() {
    return userDTO.getOauth2Username();
  }

  public String getUsername() {
    return userDTO.getUsername();
  }

  public String getEmail() {
    return userDTO.getEmail();
  }
}
