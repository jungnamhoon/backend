package com.hkorea.skyisthelimit.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String oauth2Username; // google 116434965362857403208
  private String username; // southernlight3816
  private String realName; // 김남훈
  private String email; // southernlight3816@gmail.com
  private String role; // ROLE_USER
  private boolean isFirstLogin;

}
