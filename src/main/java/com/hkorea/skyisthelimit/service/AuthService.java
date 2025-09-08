package com.hkorea.skyisthelimit.service;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.JwtHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.AuthMapper;
import com.hkorea.skyisthelimit.dto.auth.request.RegisterRequest;
import com.hkorea.skyisthelimit.dto.auth.request.SignUpRequest;
import com.hkorea.skyisthelimit.dto.auth.response.JwtResponse;
import com.hkorea.skyisthelimit.dto.auth.response.RegisterResponse;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.Register;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import com.hkorea.skyisthelimit.repository.RegisterRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final RegisterRepository registerRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtHelper jwtUtil;

  public void signUp(SignUpRequest requestDTO) {

    if (memberRepository.existsByUsername(requestDTO.getUsername())) {
      throw new BusinessException(ErrorCode.DUPLICATE_MEMBER);
    }

    if (memberRepository.existsByNickname(requestDTO.getNickname())) {
      throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
    }

    Member member = Member.create(requestDTO);

    memberRepository.save(member);
  }

  public RegisterResponse register(RegisterRequest requestDTO) {

    Register register = requestDTO.toEntity(bCryptPasswordEncoder);
    register = registerRepository.save(register);

    return AuthMapper.toRegisterResponse(register);

  }

  public JwtResponse getToken(UserDetails userDetails) {

    String username = userDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    String accessToken = jwtUtil.generateAccessToken(username, authorities);
    String refreshToken = jwtUtil.generateRefreshToken(username, authorities);

    return JwtResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }
}
