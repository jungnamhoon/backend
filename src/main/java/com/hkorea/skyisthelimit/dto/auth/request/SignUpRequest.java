package com.hkorea.skyisthelimit.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotBlank(message = "아이디는 필수 입력입니다.")
  @Schema(description = "사용자 아이디", example = "user1")
  private String username;

  @NotBlank(message = "비밀번호는 필수 입력입니다.")
  @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?!.*(.)\\1{2,}).*$",
      message = "비밀번호는 영어 소문자 1개 이상, 숫자 1개 이상, 특수문자 1개 이상 포함해야 하며, 같은 문자를 3번 이상 반복할 수 없습니다."
  )
  @Schema(description = "비밀번호", example = "dkssud77@")
  private String password;

  @NotBlank(message = "닉네임은 필수 입력입니다.")
  @Schema(description = "사용자 닉네임", example = "monkey1")
  private String nickname;


}
