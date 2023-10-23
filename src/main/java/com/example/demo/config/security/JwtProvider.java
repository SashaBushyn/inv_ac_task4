package com.example.demo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  public String generateToken(String login, String role) {
    return Jwts.builder()
        .setSubject(login)
        .claim("role", role)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(jwtSecret)
          .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.error("invalid token");
    }
    return false;
  }

  public String getLoginFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody().getSubject();
  }

  public Date getExpirationDate(String token) {
    Claims claims = Jwts
        .parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();
    return claims.getExpiration();
  }
}
