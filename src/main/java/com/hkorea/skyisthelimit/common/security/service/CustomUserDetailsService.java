package com.hkorea.skyisthelimit.common.security.service;

import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User.builder()
        .username(member.getUsername())
        .password(member.getPassword())
        .authorities(member.getRole())
        .build();
  }
}
