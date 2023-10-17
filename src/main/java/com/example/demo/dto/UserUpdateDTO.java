package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateDTO {
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotEmpty(message = "email mustn't be blank")
  @Email
  private String email;

}
