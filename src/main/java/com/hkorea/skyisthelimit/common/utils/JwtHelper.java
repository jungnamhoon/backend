package com.hkorea.skyisthelimit.common.utils;

import io.jsonwebtoken.Jwts;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtHelper {

  private static final Long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60L * 3; // 3시간
  private static final Long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7L; // 7일

  private final KeyPair keyPair;

  public String generateAccessToken(String username,
      Collection<? extends GrantedAuthority> authorities) {

    String roles = extractRoles(authorities);

    return Jwts.builder().claim("username", username).claim("roles", roles).issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
        .signWith((RSAPrivateKey) keyPair.getPrivate()).compact();

  }

  public String generateRefreshToken(String username,
      Collection<? extends GrantedAuthority> authorities) {

    String roles = extractRoles(authorities);

    return Jwts.builder().claim("username", username).claim("roles", roles).issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
        .signWith(keyPair.getPrivate()).compact();

  }

  private String extractRoles(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
  }
}
