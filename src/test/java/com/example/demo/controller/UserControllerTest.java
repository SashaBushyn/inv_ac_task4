package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.Role;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UserService userService;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void findUserByEmailTestReturnUser() throws Exception {
    UserResponseDto userResponseDto = getTestUserResponseDto();

    when(userService.getByEmail(anyString())).thenReturn(userResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/test@email.com"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userResponseDto)));
  }

  @Test
  void UnauthorizedFindUserByEmailTestReturnError() throws Exception {
    UserResponseDto userResponseDto = getTestUserResponseDto();

    when(userService.getByEmail(anyString())).thenReturn(userResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/test@email.com"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser
  void getUsersTest() throws Exception {
    UUID uuid = UUID.randomUUID();
    Page<UserResponseDto> page = new PageImpl<>(List.of());

    when(userService.getAllUsers(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(page)));
  }

  @Test
  @WithMockUser
  void updateUserReturnCorrectUser() throws Exception {
    UserResponseDto userResponseDto = getTestUserResponseDto();
    UserUpdateDTO userUpdateDTO = new UserUpdateDTO()
        .setFirstName("firsName")
        .setLastName("LastName")
        .setEmail("testEmail@email.com");
    UUID uuid = UUID.randomUUID();
    String requestBody = objectMapper.writeValueAsString(userUpdateDTO);

    when(userService.updateUser(any(UUID.class), any(UserUpdateDTO.class))).thenReturn(userResponseDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + uuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(userResponseDto)));
  }

  @Test
  @WithMockUser
  void updateNonExistentUser() throws Exception {
    UserUpdateDTO userUpdateDTO = new UserUpdateDTO()
        .setFirstName("firsName")
        .setLastName("LastName")
        .setEmail("testEmail@email.com");
    UUID uuid = UUID.randomUUID();
    String requestBody = objectMapper.writeValueAsString(userUpdateDTO);

    when(userService.updateUser(any(UUID.class), any(UserUpdateDTO.class))).thenThrow(
        new EntityNotFoundException("user not found"));

    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + uuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void deleteUserTest() throws Exception {
    UUID userId = UUID.randomUUID();

    doNothing().when(userService).deleteById(any(UUID.class));

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId)).andExpect(status().isNoContent());
    verify(userService).deleteById(userId);
  }

  private UserResponseDto getTestUserResponseDto() {
    return new UserResponseDto()
        .setRole(Role.ROLE_USER.name())
        .setEmail("test@email.com")
        .setFirstName("firstName")
        .setLastName("lastName")
        .setId(UUID.randomUUID());
  }
}