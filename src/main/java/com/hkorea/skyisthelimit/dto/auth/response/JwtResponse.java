package com.hkorea.skyisthelimit.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

  @Schema(
      title = "새로 발급 받은 accessToken",
      description = "새로 발급 받은 accessToken",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
          + "eyJ1c2VybmFtZSI6InNvdXRoZXJubGlnaHQzODE2Iiwi"
          + "cm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTY5NjE2MjQwMCwi"
          + "ZXhwIjoxNjk2MTY2MDAwfQ."
          + "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk")
  private String accessToken;
}
