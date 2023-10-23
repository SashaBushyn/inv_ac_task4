package com.example.demo.service;

import com.example.demo.config.security.JwtProvider;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtProvider jwtProvider;

  public TokenResponse registerUser(UserRequestDto userRequestDTO) {
    userService.save(userRequestDTO);
    return authenticate(userRequestDTO);
  }

  public TokenResponse authenticate(UserRequestDto userRequestDto) {
    log.info("Start authenticating the user: {}", userRequestDto.getEmail());
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword());
    UserResponseDto user = userService.getByEmail(userRequestDto.getEmail());
    authenticationManager.authenticate(authToken);

    log.info("Finish authenticating the user: {}", user.getFirstName());
    return new TokenResponse(jwtProvider.generateToken(user.getEmail(), user.getRole()));
  }
}
