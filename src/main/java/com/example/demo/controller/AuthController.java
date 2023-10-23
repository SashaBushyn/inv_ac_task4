package com.example.demo.controller;

import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public TokenResponse login(@Valid @RequestBody UserRequestDto userRequestDto) {
    return authenticationService.authenticate(userRequestDto);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public TokenResponse registerUser(@RequestBody @Valid UserRequestDto userRequestDTO) {
    return authenticationService.registerUser(userRequestDTO);
  }
}
