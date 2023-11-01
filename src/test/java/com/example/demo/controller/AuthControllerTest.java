package com.example.demo.controller;

import com.example.demo.config.security.JwtProvider;
import com.example.demo.dto.TokenResponse;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JwtProvider jwtProvider;
  @MockBean
  private AuthenticationService authenticationService;

  @Test
  void ValidUserLoginTest() throws Exception {
    UserRequestDto userRequestDto = getTestRequestUserDto();

    TokenResponse tokenResponse =
        new TokenResponse(jwtProvider.generateToken(userRequestDto.getEmail(), Role.ROLE_USER.name()));
    when(authenticationService.authenticate(userRequestDto)).thenReturn(tokenResponse);

    ObjectMapper objectMapper = new ObjectMapper();
    String requestBody = objectMapper.writeValueAsString(userRequestDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(tokenResponse)));
  }

  @Test
  void InvalidUserLoginTest() throws Exception {
    UserRequestDto userRequestDto = getTestRequestUserDto();

    when(authenticationService.authenticate(userRequestDto)).thenThrow(
        new EntityNotFoundException("user was not found"));

    ObjectMapper objectMapper = new ObjectMapper();
    String requestBody = objectMapper.writeValueAsString(userRequestDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  @Test
  void registerValidUser() throws Exception {
    UserRequestDto userRequestDto = getTestRequestUserDto();
    TokenResponse tokenResponse =
        new TokenResponse(jwtProvider.generateToken(userRequestDto.getEmail(), Role.ROLE_USER.name()));

    when(authenticationService.registerUser(userRequestDto)).thenReturn(tokenResponse);

    ObjectMapper objectMapper = new ObjectMapper();
    String requestBody = objectMapper.writeValueAsString(userRequestDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(tokenResponse)));
  }

  private UserRequestDto getTestRequestUserDto() {
    return new UserRequestDto()
        .setEmail("test@email.com")
        .setFirstName("firstName1")
        .setLastName("lastName1")
        .setPassword("pass1");
  }
}