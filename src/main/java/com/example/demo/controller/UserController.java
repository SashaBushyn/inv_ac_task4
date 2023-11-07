package com.example.demo.controller;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
@Validated
public class UserController {
  private final UserService userService;

  @GetMapping("{email}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto findUserByEmail(@PathVariable() @Email String email) {
    return userService.getByEmail(email);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> getUsers() {
    return userService.getAllUsers();
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO updatedCreateUserDto) {
    return userService.updateUser(id, updatedCreateUserDto);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable UUID id) {
    userService.deleteById(id);
  }
}
