package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequestDto {
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
  @NotEmpty(message = "email mustn't be blank")
  @Email
  private String email;
  @NotEmpty(message = "password mustn't be blank")
  private String password;
}
