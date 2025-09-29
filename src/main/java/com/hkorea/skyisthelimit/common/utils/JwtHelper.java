package com.hkorea.skyisthelimit.common.utils;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {

  private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 14; // 10초
  private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 14; //14일
  private final SecretKey secretKey;

  public JwtHelper(@Value("${spring.jwt.secret}") String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getUsername(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("username", String.class);
  }

  public String getEmail(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("email", String.class);
  }

  public String getName(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("name", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public String getCategory(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("category", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .getExpiration().before(new Date());
  }

  public String createAccessToken(String username, String email, String profileImageUrl,
      String role) {

    return Jwts.builder()
        .claim("username", username)
        .claim("email", email)
        .claim("name", profileImageUrl)

        .claim("category", "access")
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
        .signWith(secretKey)
        .compact();
  }

  public String createRefreshToken(String username, String email, String profileImageUrl,
      String role) {

    return Jwts.builder()
        .claim("username", username)
        .claim("email", email)
        .claim("profileImageUrl", profileImageUrl)

        .claim("category", "refresh")
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(secretKey)
        .compact();
  }
}
