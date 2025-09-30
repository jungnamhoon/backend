package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtHelper jwtHelper;

  public JwtResponse reissueAccessToken(String refreshToken) {

    if (refreshToken == null) {
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    if (jwtHelper.isExpired(refreshToken)) {
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    String category = jwtHelper.getCategory(refreshToken);
    if (!"refresh".equals(category)) {
      throw new BusinessException(ErrorCode.REFRESH_TOKEN_CATEGORY_INVALID);
    }

    String username = jwtHelper.getUsername(refreshToken);
    String email = jwtHelper.getEmail(refreshToken);
    String profileImageUrl = jwtHelper.getProfileImageUrl(refreshToken);
    String role = jwtHelper.getRole(refreshToken);

    return new JwtResponse(jwtHelper.createAccessToken(username, email, profileImageUrl, role));

  }

}
