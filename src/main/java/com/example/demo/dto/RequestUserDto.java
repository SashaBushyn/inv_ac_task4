package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
  @Valid
  private String email;

  @NotEmpty(message = "password mustn't be blank")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
}
