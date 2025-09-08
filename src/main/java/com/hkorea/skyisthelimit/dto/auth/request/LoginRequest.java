package com.hkorea.skyisthelimit.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

  @Schema(description = "사용자 아이디", example = "user1")
  private String username;

  @Schema(description = "비밀번호", example = "dkssud77@")
  private String password;
}
