package com.example.demo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private static final String TOKEN_PREFIX = "Bearer ";
  private final JwtProvider provider;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  @NonNull HttpServletResponse resp,
                                  @NonNull FilterChain chain) throws ServletException, IOException {

    final String bearer = req.getHeader("Authorization");
    if (!headerWithTokenIsValid(bearer)) {
      log.info("JWT FILTER: Token not found: " + req.getRemoteAddr());
      chain.doFilter(req, resp);
      return;
    }

    final String token = extractToken(bearer);
    log.info("JWT FILTER: Token found: " + token);

    if (provider.validateToken(token)) {
      UsernamePasswordAuthenticationToken authentication = authenticateUserByJWT(token);
      authentication.setDetails(new WebAuthenticationDetails(req));
      SecurityContext context = SecurityContextHolder.getContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);
      log.info("JWT FILTER: User has been authenticated: " + authentication.getName());
    }
    chain.doFilter(req, resp);
    log.info("JWT FILTER: Proceed filter chain");
  }

  private boolean headerWithTokenIsValid(String header) {
    return StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX);
  }

  private String extractToken(String bearer) {
    return bearer.substring(TOKEN_PREFIX.length());
  }

  private UsernamePasswordAuthenticationToken authenticateUserByJWT(String token) {
    String email = provider.getLoginFromToken(token);
    Date expiration = provider.getExpirationDate(token);
    UserDetails user = userDetailsService.loadUserByUsername(email);
    log.info("JWT FILTER: Token data: {} ,  {} ", email, expiration.toString());
    return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
  }
}
