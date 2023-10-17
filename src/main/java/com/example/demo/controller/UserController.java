package com.example.demo.controller;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
@Validated
public class UserController {
  private final UserService userService;

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto saveUser(@RequestBody @Valid UserRequestDto userRequestDTO) {
    return userService.save(userRequestDTO);
  }

  @GetMapping("{email}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto findUserByEmail(@PathVariable() @Email String email) {
    return userService.getByEmail(email);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponseDto> getUsers(@PageableDefault Pageable pageable) {
    return userService.getAllUsers(pageable);
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO updatedCreateUserDto) {
    return userService.updateUser(id, updatedCreateUserDto);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable UUID id) {
    userService.deleteById(id);
  }
}
