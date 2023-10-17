package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class RequestUserDto {

  @Null
  private UUID id;

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @NotEmpty(message = "email mustn't be blank")
  @Email
  private String email;

  @NotEmpty(message = "password mustn't be blank")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
}
